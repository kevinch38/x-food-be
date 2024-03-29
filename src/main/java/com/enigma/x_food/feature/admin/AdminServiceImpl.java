package com.enigma.x_food.feature.admin;

import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.feature.admin.dto.request.NewAdminRequest;
import com.enigma.x_food.feature.admin.dto.request.UpdateAdminRequest;
import com.enigma.x_food.feature.admin.dto.response.AdminResponse;
import com.enigma.x_food.feature.role.Role;
import com.enigma.x_food.feature.role.RoleService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService, UserDetailsService {
    private final AdminRepository adminRepository;
    private final ValidationUtil validationUtil;
    private final RoleService roleService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse createNew(NewAdminRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        Role role = roleService.getByRole(ERole.valueOf(request.getRole().toUpperCase()));
        Admin admin = Admin.builder()
                .adminName(request.getAdminName())
                .adminEmail(request.getAdminEmail())
                .isSuperAdmin(request.getIsSuperAdmin())
                .password(request.getPassword())
                .role(role)
                .build();

        return mapToResponse(adminRepository.saveAndFlush(admin));
    }

    @Transactional(readOnly = true)
    @Override
    public AdminResponse findById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Admin getById(String id) {
        return findByIdOrThrowNotFound(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse update(UpdateAdminRequest request) {
        Admin admin = findByIdOrThrowNotFound(request.getAdminID());
        admin.setAdminName(request.getAdminName());
        admin.setAdminEmail(request.getAdminEmail());

        return mapToResponse(adminRepository.saveAndFlush(admin));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findAdminByUsernameOrThrowException(username);
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return findAdminByUsernameOrThrowException(email);
    }

    private AdminResponse mapToResponse(Admin admin) {
        return AdminResponse.builder()
                .adminID(admin.getAdminID())
                .adminName(admin.getAdminName())
                .adminEmail(admin.getAdminEmail())
                .isSuperAdmin(admin.getIsSuperAdmin())
                .role(admin.getRole().getRole().name())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }

    private Admin findByIdOrThrowNotFound(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "admin not found"));
    }

    private Admin findAdminByUsernameOrThrowException(String username) {
        return adminRepository.findByAdminEmail(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }
}
