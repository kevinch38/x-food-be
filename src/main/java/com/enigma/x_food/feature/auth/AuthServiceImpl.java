package com.enigma.x_food.feature.auth;

import com.enigma.x_food.feature.auth.dto.request.AuthRequest;
import com.enigma.x_food.feature.auth.dto.response.LoginResponse;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.security.JwtUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ValidationUtil validationUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(AuthRequest request) {
        log.info("Start login");
        validationUtil.validate(request);
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                request.getPhoneNumber(),
//                null
//        ));
//        SecurityContextHolder.getContext().setAuthentication(authenticate);

        User user = userService.getUserByPhoneNumber2(request.getPhoneNumber());
        String token = jwtUtil.generateToken(user);
        log.info("End login");

        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
