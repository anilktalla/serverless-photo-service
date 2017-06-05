package com.test.serverless.photo.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class Image {

	private String imageExtension;
	private String base64Image;
}
