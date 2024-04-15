package com.java.practice.ratelimit.service;

import com.java.practice.ratelimit.entity.User;

public interface UserService {
    User getById(String id);
    User getByApiKey(String apikey);
    User getByUsername(String username);
}
