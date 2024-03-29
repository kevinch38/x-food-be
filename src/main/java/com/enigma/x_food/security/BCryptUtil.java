package com.enigma.x_food.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptUtil {
    private final PasswordEncoder passwordEncoder;
    public String hash(String plainText) {
        return passwordEncoder.encode(plainText);
    }

    public boolean check(String input, String hashed) {
        return passwordEncoder.matches(input, hashed);
    }
}
