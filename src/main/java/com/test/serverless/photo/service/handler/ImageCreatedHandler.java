package com.test.serverless.photo.service.handler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageCreatedHandler implements RequestHandler<S3Event, String> {

	@Override
	public String handleRequest(S3Event event, Context context) {

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();

		for (S3EventNotificationRecord record : event.getRecords()) {
			String s3Key = record.getS3().getObject().getKey();
			String s3Bucket = record.getS3().getBucket().getName();
			context.getLogger().log("found id: " + s3Bucket + " " + s3Key);
			// retrieve s3 object

			S3Object object = s3Client.getObject(new GetObjectRequest(s3Bucket, s3Key));
			InputStream objectData = object.getObjectContent();

			try {

				
				BufferedImage resizedImage = Scalr.resize(ImageIO.read(objectData), 200, 200);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(resizedImage, "png", baos);
				baos.flush();
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(imageInByte.length);

				s3Client.putObject("anilktalla-serverless-photo-service-resized", s3Key,
						new ByteArrayInputStream(imageInByte), object.getObjectMetadata());
			} catch (IOException e) {
				log.error("Exception occurred", e);
			}

		}
		return null;
	}

}
