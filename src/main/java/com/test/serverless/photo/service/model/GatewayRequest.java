package com.test.serverless.photo.service.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GatewayRequest {

	private String resource;
	private String path;
	private String httpMethod;
	private Map<String, String> headers;
	private Map<String, String> queryStringParameters;
	private Map<String, String> pathParameters;
	private Map<String, String> stageVariables;
	private String body;
	private boolean base64Encoded;
}
