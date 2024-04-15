package com.java.practice.ratelimit.controller;

import com.java.practice.ratelimit.constant.XHeader;
import com.java.practice.ratelimit.payload.response.RtResponse;
import com.java.practice.ratelimit.utils.CommonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RtRestController {

    @GetMapping(value = "/api/v1/api-key")
    public ResponseEntity<RtResponse> apiKeyResponse() {
        return ResponseEntity.ok().body(rtResponse(XHeader.API_KEY_DASH));
    }

    @GetMapping(value = "/api/v1/sendOtp")
    public ResponseEntity<RtResponse> ipResponse() {
        return ResponseEntity.ok().body(rtResponse(XHeader.IP_DASH));
    }



    private RtResponse rtResponse(String prefix) {
        return new RtResponse()
                .setKey(prefix + CommonUtils.randomAlphanumeric(8))
                .setValue(prefix + CommonUtils.randomAlphanumeric(8));
    }
}
