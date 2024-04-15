package com.java.practice.ratelimit.configs;

import com.java.practice.ratelimit.interceptor.ApiKeyRateLimitRequestInterceptor;
import com.java.practice.ratelimit.interceptor.IpRateLimitRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebRtConfig implements WebMvcConfigurer {

    private final ApiKeyRateLimitRequestInterceptor apiKeyRateLimitRequestInterceptor;
    private final IpRateLimitRequestInterceptor ipRateLimitRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(apiKeyRateLimitRequestInterceptor).addPathPatterns("/api/v1/api-key");

        registry.addInterceptor(ipRateLimitRequestInterceptor).addPathPatterns("/api/v1/sign-in", "/api/v1/register", "/api/v1/sendOtp");

    }
}