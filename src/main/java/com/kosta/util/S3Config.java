package com.kosta.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


@Configuration
public class S3Config {
  
  @Value("${cloud.aws.credentials.access-key}")
  private String accessKey;
  
  @Value("${cloud.aws.credentials.secret-key}")
  private String secretKey;
  
  @Value("${cloud.aws.region.static}")
  private String region;
  
  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.AP_NORTHEAST_2)
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)
        ))
        .build();
  }
  
  @Bean
  public S3Presigner s3Presigner() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    
    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }
}