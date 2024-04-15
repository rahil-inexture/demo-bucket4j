package com.java.practice.ratelimit.interceptor;

import com.java.practice.ratelimit.configs.RateLimitConfig;
import com.java.practice.ratelimit.constant.XHeader;
import com.java.practice.ratelimit.exception.RateLimitErrorHandler;
import com.java.practice.ratelimit.service.PricingPlanService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class IpRateLimitRequestInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(IpRateLimitRequestInterceptor.class);

    private final RateLimitConfig rateLimitConfig;
    private final PricingPlanService pricingPlanService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String ipAddress = request.getRemoteAddr();
        logger.info("Received request from IP: {}", ipAddress);

        if (rateLimitConfig.getNotAllowedIps().contains(ipAddress)) {
            logger.warn("IP is not allowed: {}", ipAddress);
            RateLimitErrorHandler.handleForbiddenError(response, XHeader.X_RATE_LIMIT_IP_NOT_ALLOWED, ipAddress);
            return false;
        }

        final Bucket tokenBucket = pricingPlanService.resolveBucketByIp(ipAddress);
        if (Objects.isNull(tokenBucket)) {
            logger.error("Token bucket not resolved for IP: {}", ipAddress);
            RateLimitErrorHandler.handleServerError(response, "Failed to resolve token bucket for IP: " + ipAddress);
            return false;
        }

        final ConsumptionProbe consumptionProbe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (consumptionProbe.isConsumed()) {
            logger.info("Token consumed, remaining tokens: {}", consumptionProbe.getRemainingTokens());
            response.addHeader(XHeader.X_RATE_LIMIT_REMAINING, String.valueOf(consumptionProbe.getRemainingTokens()));
            return true;
        }

        logger.warn("Rate limit exceeded for IP: {}", ipAddress);
        RateLimitErrorHandler.handleTooManyError(response, consumptionProbe);
        return false;
    }
}