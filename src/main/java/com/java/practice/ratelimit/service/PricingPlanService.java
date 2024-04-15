package com.java.practice.ratelimit.service;

import com.java.practice.ratelimit.enumration.UserPlan;
import io.github.bucket4j.Bucket;

public interface PricingPlanService {
    Bucket resolveBucketByUserPlan(UserPlan userPlan);
    Bucket resolveBucketByIp(String ipAddress);
}
