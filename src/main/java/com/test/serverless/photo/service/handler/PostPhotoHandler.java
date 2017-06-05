package com.test.serverless.photo.service.handler;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.gson.Gson;
import com.test.serverless.photo.service.model.GatewayRequest;
import com.test.serverless.photo.service.model.GatewayResponse;
import com.test.serverless.photo.service.model.Image;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostPhotoHandler implements RequestHandler<GatewayRequest, GatewayResponse> {

	@Override
	public GatewayResponse handleRequest(GatewayRequest request, Context context) {

		try {
			String userName = request.getPathParameters().get("userName");
			
			log.info("Photo upload request received with body " + request.getBody());
			log.info("Is the body encoded? ");
			
			Image image = new Gson().fromJson(request.getBody(), Image.class);

			byte[] bytes = Base64.getDecoder().decode(image.getBase64Image());
			
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(bytes.length);
			metadata.addUserMetadata("userName", userName);
			
			metadata.setContentType(contentType(image.getImageExtension()));

			AmazonS3 s3client = AmazonS3ClientBuilder.standard().build();

			s3client.putObject("anilktalla-serverless-photo-service", userName,
					new ByteArrayInputStream(bytes), metadata);
			 
			return GatewayResponse.builder()
					.statusCode(200)
					.body(request.getBody())
					.headers(GatewayResponse.headersMap(request.getHeaders().get("Content-Type")))
					.build();
			
		} catch (Exception e) {
			log.error("Exception occurred",e);
			throw new RuntimeException(e);
		} 
		
	}

	private String contentType(String imageExtension) {
		if("png".equalsIgnoreCase(imageExtension)){
			return "img/png";
		}else if("jpg".equalsIgnoreCase(imageExtension)){
			return "img/jpg";
		}else if("jpeg".equalsIgnoreCase(imageExtension)){
			return "img/jpeg";
		}
		return null;
	}

}
