package com.java.practice.ratelimit.interceptor;

import com.java.practice.ratelimit.configs.RateLimitConfig;
import com.java.practice.ratelimit.constant.XHeader;
import com.java.practice.ratelimit.entity.User;
import com.java.practice.ratelimit.exception.RateLimitErrorHandler;
import com.java.practice.ratelimit.payload.response.RateLimitData;
import com.java.practice.ratelimit.service.PricingPlanService;
import com.java.practice.ratelimit.service.UserService;
import com.java.practice.ratelimit.storage.RateLimitDataStorage;
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
public class ApiKeyRateLimitRequestInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyRateLimitRequestInterceptor.class);


    private final RateLimitConfig rateLimitConfig;
    private final PricingPlanService pricingPlanService;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String apiKey = request.getHeader(XHeader.X_API_KEY);
        logger.info("Received request with API key: {}", apiKey);

        if (Objects.isNull(apiKey)) {
            logger.warn("API key is null");
            RateLimitErrorHandler.handleForbiddenError(response, XHeader.X_RATE_LIMIT_IP_NOT_ALLOWED, apiKey);
            return false;
        }

        if (rateLimitConfig.getNotAllowedKeys().contains(apiKey)) {
            logger.warn("API key is not allowed: {}", apiKey);
            RateLimitErrorHandler.handleForbiddenError(response, XHeader.X_RATE_LIMIT_USER_NOT_ALLOWED, apiKey);
            return false;
        }
        final RateLimitData rateLimitData = getRateLimitData(apiKey);
        if (Objects.isNull(rateLimitData)) {
            logger.warn("Rate limit data is null for API key: {}", apiKey);
            RateLimitErrorHandler.handleForbiddenError(response, XHeader.X_RATE_LIMIT_USER_NOT_ALLOWED, apiKey);
            return false;
        }

        final ConsumptionProbe consumptionProbe = rateLimitData.getBucket().tryConsumeAndReturnRemaining(1);
        if (consumptionProbe.isConsumed()) {
            logger.info("Token consumed, remaining tokens: {}", consumptionProbe.getRemainingTokens());
            response.addHeader(XHeader.X_RATE_LIMIT_REMAINING, String.valueOf(consumptionProbe.getRemainingTokens()));
            return true;
        }
        logger.warn("Rate limit exceeded for API key: {}", apiKey);
        RateLimitErrorHandler.handleTooManyError(response, consumptionProbe);
        return false;
    }

    private RateLimitData getRateLimitData(String apiKey) {
        RateLimitData rateLimitData = RateLimitDataStorage.getRateLimitData(apiKey);
        if (Objects.isNull(rateLimitData)) {
            logger.info("Rate limit data not found for API key: {}", apiKey);
            final User userByApiKey = userService.getByApiKey(apiKey);
            if (Objects.isNull(userByApiKey)) {
                logger.warn("User not found for API key: {}", apiKey);
                return null;
            }
            final Bucket bucket = pricingPlanService.resolveBucketByUserPlan(userByApiKey.getUserPlan());
            if (Objects.isNull(bucket)) {
                logger.warn("Bucket not resolved for user plan: {}", userByApiKey.getUserPlan());
                return null;
            }
            rateLimitData = new RateLimitData().setUser(userByApiKey).setBucket(bucket);
            RateLimitDataStorage.addRateLimitData(apiKey, rateLimitData);
            logger.info("Rate limit data created and stored for API key: {}", apiKey);
        } else {
            logger.info("Rate limit data found for API key: {}", apiKey);
        }
        return rateLimitData;
    }
}