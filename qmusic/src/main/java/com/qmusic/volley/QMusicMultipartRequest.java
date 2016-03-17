package com.qmusic.volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;


public class QMusicMultipartRequest extends QmusicRequest<String> {
	MultipartEntityBuilder entity = MultipartEntityBuilder.create();
	HttpEntity httpentity;
	private static final String FILE_PART_NAME = "file";

	private final Response.Listener<String> mListener;
	private final File mFilePart;
	private final Map<String, String> mStringPart;

	public QMusicMultipartRequest(String url, Listener<String> listener, Response.ErrorListener errorListener, File file, Map<String, String> mStringPart) {
		super(Method.POST, url, listener, errorListener);
		mListener = listener;
		mFilePart = file;
		this.mStringPart = mStringPart;
		entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		buildMultipartEntity();
	}

	public void addStringBody(String param, String value) {
		mStringPart.put(param, value);
	}

	private void buildMultipartEntity() {
		entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
		for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
			entity.addTextBody(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getBodyContentType() {
		return httpentity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			httpentity = entity.build();
			httpentity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}