package com.java.practice.ratelimit.enumration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public enum CustomPricePlan {

    FREE {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(20, Refill.intervally(20, Duration.ofSeconds(1)));
        }
    },
    BASIC {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(40, Refill.intervally(40, Duration.ofSeconds(1)));
        }
    },
    PROFESSIONAL {
        @Override
        public
        Bandwidth getLimit() {
            return Bandwidth.classic(100, Refill.intervally(100, Duration.ofSeconds(1)));
        }
    };

    public abstract Bandwidth getLimit();

    public static CustomPricePlan resolvePricingFromUserPlan(UserPlan userPlan) {
        if (UserPlan.BASIC.equals(userPlan)) {
            return BASIC;
        }
        if (UserPlan.PRO.equals(userPlan)) {
            return PROFESSIONAL;
        }
        return FREE;
    }
}