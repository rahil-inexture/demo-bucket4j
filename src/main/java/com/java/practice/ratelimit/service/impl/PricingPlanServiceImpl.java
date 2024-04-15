package com.java.practice.ratelimit.service.impl;

import com.java.practice.ratelimit.enumration.CustomPricePlan;
import com.java.practice.ratelimit.enumration.PricingPlan;
import com.java.practice.ratelimit.enumration.UserPlan;
import com.java.practice.ratelimit.service.PricingPlanService;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PricingPlanServiceImpl implements PricingPlanService {

    private final Map<UserPlan, Bucket> PLAN_BUCKETS = new ConcurrentHashMap<>();
    private final Map<String, Bucket> IP_BUCKETS = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucketByUserPlan(UserPlan userPlan) {
        return PLAN_BUCKETS.computeIfAbsent(userPlan, this::newBucket);
    }

    private Bucket newBucket(UserPlan userPlan) {
        final PricingPlan pricingPlan = PricingPlan.resolvePricingFromUserPlan(userPlan);
        return Bucket.builder()
                .addLimit(pricingPlan.getLimit()).build();
    }

    /*
    private Bucket newCustomBucket(UserPlan userPlan) {
        final CustomPricePlan customPricePlan = CustomPricePlan.resolvePricingFromUserPlan(userPlan);

        return Bucket.builder()
                    .addLimit(customPricePlan.getLimit()).build();
    }*/

    @Override
    public Bucket resolveBucketByIp(String ipAddress) {
        return IP_BUCKETS.computeIfAbsent(ipAddress, userPlan -> newBucket(UserPlan.FREE));
    }
}
