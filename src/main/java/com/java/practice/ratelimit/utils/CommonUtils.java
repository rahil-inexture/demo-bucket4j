package com.java.practice.ratelimit.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.stream.Collectors;

@UtilityClass
public class CommonUtils {

    public static String randomAlphanumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        return random.ints(length, 0, characters.length())
                .mapToObj(characters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
