package com.example.movie_service.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstant {
    public static String SECRET_KEY;
    public static String JWT_HEADER;

    @Value("${jwt.secretKey}")
    public void setSecretKey(String secretKey) {
        JwtConstant.SECRET_KEY = secretKey;
    }
    @Value("${jwt.header}")
    public void setJwtHeader(String header) {
        JwtConstant.JWT_HEADER = header;
    }
}
