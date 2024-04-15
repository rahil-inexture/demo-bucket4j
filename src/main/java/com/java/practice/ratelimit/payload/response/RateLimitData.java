package com.java.practice.ratelimit.payload.response;

import com.java.practice.ratelimit.entity.User;
import io.github.bucket4j.Bucket;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RateLimitData {
    private User user;
    private Bucket bucket;
}
