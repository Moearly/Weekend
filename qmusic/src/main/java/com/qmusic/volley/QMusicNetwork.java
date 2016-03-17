package com.qmusic.volley;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


import android.os.SystemClock;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ByteArrayPool;
import com.android.volley.toolbox.HttpStack;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.utils.DateUtils;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

public class QMusicNetwork implements Network {
	protected static final boolean DEBUG = VolleyLog.DEBUG;
	private static int SLOW_REQUEST_THRESHOLD_MS = 3000;

	private static int DEFAULT_POOL_SIZE = 4096;

	protected final HttpStack mHttpStack;

	protected final ByteArrayPool mPool;

	/**
	 * @param httpStack
	 *            HTTP stack to be used
	 */
	public QMusicNetwork(HttpStack httpStack) {
		// If a pool isn't passed in, then build a small default pool that will
		// give us a lot of
		// benefit and not use too much memory.
		this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
	}

	/**
	 * @param httpStack
	 *            HTTP stack to be used
	 * @param pool
	 *            a buffer pool that improves GC performance in copy operations
	 */
	public QMusicNetwork(HttpStack httpStack, ByteArrayPool pool) {
		mHttpStack = httpStack;
		mPool = pool;
	}

	@Override
	public NetworkResponse performRequest(Request<?> request) throws VolleyError {
		long requestStart = SystemClock.elapsedRealtime();
		while (true) {
			HttpResponse httpResponse = null;
			HttpEntity responseContents = null;
			Map<String, String> responseHeaders = Collections.emptyMap();
			try {
				// Gather headers.
				Map<String, String> headers = new HashMap<String, String>();
				addCacheHeaders(headers, request.getCacheEntry());
				httpResponse = mHttpStack.performRequest(request, headers);
				StatusLine statusLine = httpResponse.getStatusLine();
				int statusCode = statusLine.getStatusCode();

				responseHeaders = convertHeaders(httpResponse.getAllHeaders());
				// Handle cache validation.
				if (statusCode == HttpStatus.SC_NOT_MODIFIED) {

					Entry entry = request.getCacheEntry();
					if (entry == null) {
						return new QMusicNetworkResponse(HttpStatus.SC_NOT_MODIFIED, null, responseHeaders, true, SystemClock.elapsedRealtime() - requestStart);
					}

					// A HTTP 304 response does not have all header fields. We
					// have to use the header fields from the cache entry plus
					// the new ones from the response.
					// http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.5
					entry.responseHeaders.putAll(responseHeaders);
					return new QMusicNetworkResponse(HttpStatus.SC_NOT_MODIFIED, null, entry.responseHeaders, true, SystemClock.elapsedRealtime()
							- requestStart);
				}

				// Some responses such as 204s do not have content. We must
				// check.
				if (httpResponse.getEntity() != null) {
					// TODO:
					responseContents = httpResponse.getEntity();
				} else {
					// Add 0 byte response as a way of honestly representing a
					// no-content request.
					// responseContents = new byte[0];
				}

				// if the request is slow, log it.
				long requestLifetime = SystemClock.elapsedRealtime() - requestStart;
				logSlowRequests(requestLifetime, request, responseContents, statusLine);

				if (statusCode < 200 || statusCode > 299) {
					throw new IOException();
				}
				return new QMusicNetworkResponse(statusCode, responseContents, responseHeaders, false, SystemClock.elapsedRealtime() - requestStart);
			} catch (SocketTimeoutException e) {
				attemptRetryOnException("socket", request, new TimeoutError());
			} catch (ConnectTimeoutException e) {
				attemptRetryOnException("connection", request, new TimeoutError());
			} catch (MalformedURLException e) {
				throw new RuntimeException("Bad URL " + request.getUrl(), e);
			} catch (IOException e) {
				int statusCode = 0;
				NetworkResponse networkResponse = null;
				if (httpResponse != null) {
					statusCode = httpResponse.getStatusLine().getStatusCode();
				} else {
					throw new NoConnectionError(e);
				}
				VolleyLog.e("Unexpected response code %d for %s", statusCode, request.getUrl());
				if (responseContents != null) {
					networkResponse = new QMusicNetworkResponse(statusCode, responseContents, responseHeaders, false, SystemClock.elapsedRealtime()
							- requestStart);
					if (statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN) {
						attemptRetryOnException("auth", request, new AuthFailureError(networkResponse));
					} else {
						// TODO: Only throw ServerError for 5xx status codes.
						throw new ServerError(networkResponse);
					}
				} else {
					throw new NetworkError(networkResponse);
				}
			}
		}
	}

	/**
	 * Logs requests that took over SLOW_REQUEST_THRESHOLD_MS to complete.
	 */
	private void logSlowRequests(long requestLifetime, Request<?> request, HttpEntity responseContents, StatusLine statusLine) {
		if (DEBUG || requestLifetime > SLOW_REQUEST_THRESHOLD_MS) {
			VolleyLog.d("HTTP response for request=<%s> [lifetime=%d], [size=%s], " + "[rc=%d], [retryCount=%s]", request, requestLifetime,
					responseContents != null ? responseContents.getContentLength() : "null", statusLine.getStatusCode(), request.getRetryPolicy()
							.getCurrentRetryCount());
		}
	}

	/**
	 * Attempts to prepare the request for a retry. If there are no more
	 * attempts remaining in the request's retry policy, a timeout exception is
	 * thrown.
	 * 
	 * @param request
	 *            The request to use.
	 */
	private static void attemptRetryOnException(String logPrefix, Request<?> request, VolleyError exception) throws VolleyError {
		RetryPolicy retryPolicy = request.getRetryPolicy();
		int oldTimeout = request.getTimeoutMs();

		try {
			retryPolicy.retry(exception);
		} catch (VolleyError e) {
			request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", logPrefix, oldTimeout));
			throw e;
		}
		request.addMarker(String.format("%s-retry [timeout=%s]", logPrefix, oldTimeout));
	}

	private void addCacheHeaders(Map<String, String> headers, Cache.Entry entry) {
		// If there's no cache entry, we're done.
		if (entry == null) {
			return;
		}

		if (entry.etag != null) {
			headers.put("If-None-Match", entry.etag);
		}

		if (entry.serverDate > 0) {
			Date refTime = new Date(entry.serverDate);
			headers.put("If-Modified-Since", DateUtils.formatDate(refTime));
		}
	}

	protected void logError(String what, String url, long start) {
		long now = SystemClock.elapsedRealtime();
		VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, (now - start), url);
	}

	/**
	 * Converts Headers[] to Map<String, String>.
	 */
	protected static Map<String, String> convertHeaders(Header[] headers) {
		Map<String, String> result = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < headers.length; i++) {
			result.put(headers[i].getName(), headers[i].getValue());
		}
		return result;
	}
}
