package com.test_back_end.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test_back_end.security.util.JwtTokenFactory;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Properties;

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

    @Bean
    public Properties mainProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "sandbox.smtp.mailtrap.io");
        return props;
    }

    @Bean
    public PasswordAuthentication passwordAuthentication() {
        return new PasswordAuthentication("6d7b0805389bf6", "2720e0195a02da");
    }

    @Bean
    public Session mailSession(@Qualifier("mainProperties") Properties mainProperties, PasswordAuthentication passwordAuthentication) {
        return Session.getInstance(mainProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return passwordAuthentication;
            }
        });
    }
}
