package com.java.practice.ratelimit.service.impl;

import com.java.practice.ratelimit.entity.User;
import com.java.practice.ratelimit.repository.UserRepository;
import com.java.practice.ratelimit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(String id) {
        return userRepository.getById(id);
    }

    public User getByApiKey(String apiKey) {
        return userRepository.getByApiKey(apiKey);
    }

    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }
}
