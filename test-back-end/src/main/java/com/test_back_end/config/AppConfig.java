package com.test_back_end.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test_back_end.security.util.JwtTokenFactory;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.security.Key;

@Configuration
public class AppConfig {

    @Bean
    public SecretKey secretKey() {
        byte[] keyBytes = Decoders.BASE64.decode("NnFpNnl1U2V5R2tqaUxlRFRhd2tzblR4MHp2UjR4WXpTWXhVdnpzbGZsVT0=");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        return minioClient;
    }

    @Bean
    public JwtTokenFactory jwtTokenFactory(Key secret) {
        return new JwtTokenFactory(secret);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
