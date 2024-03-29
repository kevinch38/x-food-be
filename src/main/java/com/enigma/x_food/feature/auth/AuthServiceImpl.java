package com.enigma.x_food.feature.auth;

import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.admin.AdminRepository;
import com.enigma.x_food.feature.admin.AdminService;
import com.enigma.x_food.feature.admin.dto.request.NewAdminRequest;
import com.enigma.x_food.feature.auth.dto.request.AdminAuthRequest;
import com.enigma.x_food.feature.auth.dto.request.AdminTokenRequest;
import com.enigma.x_food.feature.auth.dto.request.AuthRequest;
import com.enigma.x_food.feature.auth.dto.response.LoginResponse;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.security.JwtUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ValidationUtil validationUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BCryptUtil bCryptUtil;
    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    @Value("${app.x-food.super-admin-email}")
    String superAdminEmail;
    @Value("${app.x-food.super-admin-password}")
    String superAdminPassword;
    @Value("${app.x-food.temporary-admin-email}")
    String temporaryAdminEmail;
    @Value("${app.x-food.temporary-admin-password}")
    String temporaryAdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminEmail(superAdminEmail);

        if (optionalAdmin.isEmpty()){
            NewAdminRequest request = NewAdminRequest.builder()
                    .adminName("superadmin")
                    .adminEmail(superAdminEmail)
                    .password(bCryptUtil.hash(superAdminPassword))
                    .isSuperAdmin(true)
                    .role(ERole.ROLE_SUPER_ADMIN.name())
                    .build();
            adminService.createNew(request);
        }

        Optional<Admin> optionalAdmin2 = adminRepository.findByAdminEmail(temporaryAdminEmail);

        if (optionalAdmin2.isEmpty()){
            NewAdminRequest request = NewAdminRequest.builder()
                    .adminName("temporaryAdmin")
                    .adminEmail(temporaryAdminEmail)
                    .password(bCryptUtil.hash(temporaryAdminPassword))
                    .isSuperAdmin(false)
                    .role(ERole.ROLE_USER.name())
                    .build();
            adminService.createNew(request);
        }

        Optional<Admin> optionalAdmin3 = adminRepository.findByAdminEmail("partnershipstaff@gmail.com");

        if (optionalAdmin3.isEmpty()){
            NewAdminRequest request = NewAdminRequest.builder()
                    .adminName("Staff")
                    .adminEmail("partnershipstaff@gmail.com")
                    .password(bCryptUtil.hash("123"))
                    .isSuperAdmin(false)
                    .role(ERole.ROLE_PARTNERSHIP_STAFF.name())
                    .build();
            adminService.createNew(request);
        }

        Optional<Admin> optionalAdmin4 = adminRepository.findByAdminEmail("partnershiphead@gmail.com");

        if (optionalAdmin4.isEmpty()){
            NewAdminRequest request = NewAdminRequest.builder()
                    .adminName("Head")
                    .adminEmail("partnershiphead@gmail.com")
                    .password(bCryptUtil.hash("123"))
                    .isSuperAdmin(false)
                    .role(ERole.ROLE_PARTNERSHIP_HEAD.name())
                    .build();
            adminService.createNew(request);
        }

        Optional<Admin> optionalAdmin5 = adminRepository.findByAdminEmail("marketinghead@gmail.com");

        if (optionalAdmin5.isEmpty()){
            NewAdminRequest request = NewAdminRequest.builder()
                    .adminName("Head")
                    .adminEmail("marketinghead@gmail.com")
                    .password(bCryptUtil.hash("123"))
                    .isSuperAdmin(false)
                    .role(ERole.ROLE_MARKETING_HEAD.name())
                    .build();
            adminService.createNew(request);
        }

        Optional<Admin> optionalAdmin6 = adminRepository.findByAdminEmail("marketingstaff@gmail.com");

        if (optionalAdmin6.isEmpty()){
            NewAdminRequest request = NewAdminRequest.builder()
                    .adminName("Staff")
                    .adminEmail("marketingstaff@gmail.com")
                    .password(bCryptUtil.hash("123"))
                    .isSuperAdmin(false)
                    .role(ERole.ROLE_MARKETING_STAFF.name())
                    .build();
            adminService.createNew(request);
        }
    }
    @Override
    public LoginResponse loginUser(AuthRequest request) {
        log.info("Start login");
        validationUtil.validate(request);

        User user = userService.getUserByPhoneNumber2(request.getPhoneNumber());
        String token = jwtUtil.generateTokenUser(user);
        log.info("End login");

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public LoginResponse loginAdmin(AdminAuthRequest request) {
        log.info("Start login");
        validationUtil.validate(request);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getAdminEmail(),
                request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        Admin admin = (Admin) authenticate.getPrincipal();
        String token = jwtUtil.generateTokenAdmin(admin);
        log.info("End login");

        return LoginResponse.builder()
                .token(token)
                .role(admin.getRole().getRole().name())
                .build();
    }

    @Override
    public Boolean verifyAdmin(AdminTokenRequest request) {
        log.info("Start verify");
        Boolean aBoolean = jwtUtil.verifyJwtTokenAdmin(request.getToken());
        log.info("end verify");
        return aBoolean;
    }
}
