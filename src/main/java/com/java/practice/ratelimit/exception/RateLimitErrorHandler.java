package com.java.practice.ratelimit.exception;

import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RateLimitErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitErrorHandler.class);
    private RateLimitErrorHandler() {
        throw new RuntimeException();
    }

    public static void handleTooManyError(HttpServletResponse response, ConsumptionProbe consumptionProbe) {
        long waitForRefill = consumptionProbe.getNanosToWaitForRefill() / 1_000_000_000;
        response.addHeader(XHeader.X_RATE_LIMIT_RETRY_AFTER_SECONDS, String.valueOf(waitForRefill));
        handleResponseError(response, HttpStatus.TOO_MANY_REQUESTS, String.format("You have exhausted your API Request Quota, please try again in [%d] seconds.", waitForRefill));
    }

    public static void handleForbiddenError(HttpServletResponse response, String header, String headerValue) {
        response.addHeader(header, headerValue);
        handleResponseError(response, HttpStatus.FORBIDDEN, "Forbidden");
    }

    public static void handleNoApiKeyErrorForbiddenError(HttpServletResponse response) {
        handleResponseError(response, HttpStatus.FORBIDDEN, "There is no X-Api-Key in request");
    }

    private static void handleResponseError(HttpServletResponse response, HttpStatus httpStatus, String errorMessage) {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(errorMessage);
        } catch (Exception ex) {
            logger.error("Error {} ", ex.getMessage());
        }
    }

    public static void handleServerError(HttpServletResponse response, String s) {
        handleResponseError(response, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    private enum HttpStatus {
        TOO_MANY_REQUESTS(429),
        FORBIDDEN(403),
        INTERNAL_SERVER_ERROR(500);

        private final int value;

        HttpStatus(int value) {
            this.value = value;
        }

        public int value() {return value;}
    }

    private static class XHeader {
        private static final String X_RATE_LIMIT_RETRY_AFTER_SECONDS = "X-Rate-Limit-Retry-After-Seconds";
    }
}