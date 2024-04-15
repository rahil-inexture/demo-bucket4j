package com.java.practice.ratelimit.repository;

import com.java.practice.ratelimit.entity.User;
import com.java.practice.ratelimit.enumration.UserPlan;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Map<String, User> STORAGE = new ConcurrentHashMap<>();

    static {
        STORAGE.put(
                "00001",
                new User()
                        .setUsername("FreeUser")
                        .setApiKey("d0476978-free-4ad5-94e8-38ebb575f5c4")
                        .setUserPlan(UserPlan.FREE));
        STORAGE.put(
                "00002",
                new User()
                        .setUsername("BasicUser")
                        .setApiKey("d0476978-basic-4ad5-94e8-38ebb575f5c5")
                        .setUserPlan(UserPlan.BASIC));
        STORAGE.put(
                "00003",
                new User()
                        .setUsername("ProUser")
                        .setApiKey("d0476978-prof-4ad5-94e8-38ebb575f5c6")
                        .setUserPlan(UserPlan.PRO));
    }

    @Override
    public User getById(String id) {
        return STORAGE.values().stream()
                .filter(rtUser -> id.equalsIgnoreCase(rtUser.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("There is no user with id: " + id));
    }

    @Override
    public User getByApiKey(String apiKey) {
        return STORAGE.values().stream()
                .filter(rtUser -> apiKey.equalsIgnoreCase(rtUser.getApiKey()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("There is no user with api key: " + apiKey));
    }

    @Override
    public User getByUsername(String username) {
        return STORAGE.values().stream()
                .filter(rtUser -> username.equalsIgnoreCase(rtUser.getUsername()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("There is no user with username: " + username));
    }
}