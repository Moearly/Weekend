package com.qmusic.volley;

import java.util.Map;


import com.android.volley.NetworkResponse;

import org.apache.http.HttpEntity;


public class QMusicNetworkResponse extends NetworkResponse {
	HttpEntity entity;

	public QMusicNetworkResponse(HttpEntity entity) {
		super(new byte[0]);
		this.entity = entity;
	}

	public QMusicNetworkResponse(int statusCode, HttpEntity entity, Map<String, String> headers, boolean notModified, long networkTimeMs) {
		super(statusCode, new byte[0], headers, notModified, networkTimeMs);
		this.entity = entity;
	}
}
