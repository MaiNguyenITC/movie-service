package com.example.movie_service.constant;

import org.springframework.beans.factory.annotation.Value;

public class CorsConstant {
    public static Long MAX_AGE;
    public static Boolean ALLOW_CREDENTIALS;
    public static String EXPOSED_HEADERS;

    @Value("${cors.max-age}")
    public void setMaxAge(Long maxAge) {
        CorsConstant.MAX_AGE = maxAge;
    }

    @Value("${cors.allow-credentials}")
    public void setAllowCredentials(Boolean allowCredentials) {
        CorsConstant.ALLOW_CREDENTIALS = allowCredentials;
    }

    @Value("${cors.exposed-headers}")
    public void setExposedHeaders(String exposedHeaders) {
        CorsConstant.EXPOSED_HEADERS = exposedHeaders;
    }
}
