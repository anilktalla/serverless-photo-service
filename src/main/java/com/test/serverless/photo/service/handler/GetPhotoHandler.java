package com.test.serverless.photo.service.handler;

import java.util.Base64;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.test.serverless.photo.service.model.GatewayRequest;
import com.test.serverless.photo.service.model.GatewayResponse;
import com.test.serverless.photo.service.model.Image;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetPhotoHandler implements RequestHandler<GatewayRequest, GatewayResponse> {

	@Override
	public GatewayResponse handleRequest(GatewayRequest request, Context context) {

		try {
			String userName = request.getPathParameters().get("userName");
			
			log.info("Get Photo request received " + request.getBody());

			AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();
			
			S3Object s3Object = s3client.getObject("anilktalla-serverless-photo-service", userName);
			
			log.info("User Metadata -> Username key value: " +s3Object.getObjectMetadata().getUserMetadata().get("userName"));
			return GatewayResponse.builder()
					.statusCode(200)
					.body(new Gson().toJson(Image.builder()
							.base64Image(Base64.getEncoder().encodeToString(IOUtils.toByteArray(s3Object.getObjectContent())))
							.build()))
					.build();
		} catch (Exception e) {
			log.error("Exception occurred",e);
			throw new RuntimeException(e);
		} 
		
	}

}
