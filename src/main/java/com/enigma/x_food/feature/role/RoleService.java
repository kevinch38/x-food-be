package com.enigma.x_food.feature.role;

import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.feature.role.response.RoleResponse;

import java.util.List;

public interface RoleService {
    Role getByRole(ERole status);
    List<RoleResponse> getAll();
}
