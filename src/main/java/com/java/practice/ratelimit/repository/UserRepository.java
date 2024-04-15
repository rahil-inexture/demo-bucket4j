package com.java.practice.ratelimit.repository;

import com.java.practice.ratelimit.entity.User;

public interface UserRepository {
    User getById(String uuid);
    User getByApiKey(String apiKey);
    User getByUsername(String username);
}
