package com.enigma.x_food.feature.role;

import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.feature.role.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void init() {
        List<Role> role = new ArrayList<>();
        for (ERole value : ERole.values()) {
            Optional<Role> status = roleRepository.findByRole(value);
            if (status.isPresent()) continue;

            role.add(Role.builder()
                    .role(value)
                    .build());
        }
        roleRepository.saveAllAndFlush(role);
    }

    @Transactional(readOnly = true)
    @Override
    public Role getByRole(ERole status) {
        return findByStatusOrThrowNotFound(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAll() {
        log.info("Start getAll");

        List<Role> role = roleRepository.findAll();
        log.info("End getAll");
        return role.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private Role findByStatusOrThrowNotFound(ERole status) {
        return roleRepository.findByRole(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order status not found"));
    }

    private RoleResponse mapToResponse(Role role) {
        return RoleResponse.builder()
                .roleID(role.getRoleID())
                .role(role.getRole().toString())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}
