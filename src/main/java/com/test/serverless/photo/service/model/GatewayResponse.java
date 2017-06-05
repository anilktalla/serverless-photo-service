package com.test.serverless.photo.service.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GatewayResponse {

	private String body;
	private int statusCode;
	private Map<String, String> headers;
	private boolean isBase64Encoded;

	public static Map<String, String> headersMap(String contentType) {
		return Collections.unmodifiableMap(new HashMap<String, String>() {

			private static final long serialVersionUID = 1L;

			{
				put("Content-Type", contentType);
			}
		});
	}
}
