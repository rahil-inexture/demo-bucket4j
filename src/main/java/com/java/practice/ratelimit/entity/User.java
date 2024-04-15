package com.java.practice.ratelimit.entity;

import com.java.practice.ratelimit.enumration.UserPlan;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private String id;
    private String username;
    private String apiKey;
    private UserPlan userPlan;
}
