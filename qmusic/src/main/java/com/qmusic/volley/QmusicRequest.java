/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qmusic.volley;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;


/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * @param <T>
 *            JSON type of response expected
 */
public abstract class QmusicRequest<T> extends Request<T> {
	/** Charset for request. */
	private static final String PROTOCOL_CHARSET = "utf-8";

	/** Content type for request. */
	private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);
	private static String userAgent = "qmusic_1.0";
	private final Listener<T> mListener;

	private String body;
	private HttpEntity entity;
	private Map<String, String> headers;
	private Map<String, String> params;

	public QmusicRequest(int method, String url, Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		this.headers = new HashMap();

	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

//	@Override
//	public String getBodyContentType() {
//		return PROTOCOL_CONTENT_TYPE;
//	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		try {
			ByteArrayOutputStream bos;
			if (params != null) {
				List<NameValuePair> pairs = new ArrayList();
				for (Map.Entry<String, String> e : params.entrySet()) {
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(key, value));
					}
				}
				bos = new ByteArrayOutputStream();
				new UrlEncodedFormEntity(pairs, PROTOCOL_CHARSET).writeTo(bos);
				return bos.toByteArray();
			} else if (entity != null) {
				bos = new ByteArrayOutputStream();
				entity.writeTo(bos);
				return bos.toByteArray();
			} else {
				if (!TextUtils.isEmpty(body)) {
					StringEntity reqEntity = new StringEntity(body, PROTOCOL_CHARSET);
					bos = new ByteArrayOutputStream();
					reqEntity.writeTo(bos);
					return bos.toByteArray();
				}
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public static String getUserAgent() {
		return userAgent;
	}

	public static final void setUserAgent(String agent) {
		userAgent = agent;
	}

	public static Cache.Entry parseCacheHeaders(String url, NetworkResponse response) {
		Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
		// Note modify the cache policy here
		if ("about:blank".equals(url)) {
			entry.ttl = entry.ttl + 60 * 60 * 1000;// 60 mins
			// entry.softTtl: get the cache if not expired, but will refresh
		}
		return entry;
	}

	@Override
	public RetryPolicy getRetryPolicy() {
		return new DefaultRetryPolicy(AsyncHttpClient.DEFAULT_SOCKET_TIMEOUT, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	}

}
