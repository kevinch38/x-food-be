package com.enigma.x_food.feature.admin;

import com.enigma.x_food.feature.admin.dto.request.NewAdminRequest;
import com.enigma.x_food.feature.admin.dto.request.UpdateAdminRequest;
import com.enigma.x_food.feature.admin.dto.response.AdminResponse;

public interface AdminService {
    AdminResponse createNew(NewAdminRequest request);
    AdminResponse findById(String id);
    Admin getById(String id);
    Admin findByEmail(String email);
    AdminResponse update(UpdateAdminRequest request);
}
