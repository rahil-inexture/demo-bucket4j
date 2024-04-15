package com.java.practice.ratelimit.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
public class RateLimitConfig {

    @Value("${com.practice.rate.limit.not.allowed.keys}")
    private List<String> notAllowedKeys;
    @Value("${com.practice.rate.limit.not.allowed.ips}")
    private List<String> notAllowedIps;
}
