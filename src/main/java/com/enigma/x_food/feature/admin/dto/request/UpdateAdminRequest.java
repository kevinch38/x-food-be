package com.enigma.x_food.feature.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAdminRequest {
    @NotBlank(message = "Admin ID is required")
    private String adminID;
    @NotNull(message = "Admin name is required")
    private String adminName;
    @NotNull(message = "Admin email is required")
    private String adminEmail;
    @NotNull(message = "Is super admin is required")
    private String isSuperAdmin;
    @NotNull(message = "Role is required")
    private String role;
}
