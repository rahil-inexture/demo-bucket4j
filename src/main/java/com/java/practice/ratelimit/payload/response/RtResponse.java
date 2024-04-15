package com.java.practice.ratelimit.payload.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RtResponse {
    private String key;
    private String value;
}
