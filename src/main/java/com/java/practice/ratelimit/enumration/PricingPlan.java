package com.java.practice.ratelimit.enumration;

import io.github.bucket4j.Bandwidth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum PricingPlan {
    FREE(10),
    BASIC(100),
    PROFESSIONAL(1000);

    private final int bucketCapacity;

    public Bandwidth getLimit() {
        return Bandwidth.builder()
                .capacity(bucketCapacity)
                .refillIntervally(bucketCapacity, Duration.ofSeconds(1))
                .build();
    }

    public static PricingPlan resolvePricingFromUserPlan(UserPlan userPlan) {
        if (UserPlan.BASIC.equals(userPlan)) {
            return BASIC;
        }
        if (UserPlan.PRO.equals(userPlan)) {
            return PROFESSIONAL;
        }
        return FREE;
    }
}