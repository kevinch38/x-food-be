package com.enigma.x_food.feature.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserProfilePhotoRequest {
    @NotBlank(message = "ID is required")
    private String accountID;
    @NotNull(message = "Profile Photo is required")
    private MultipartFile profilePhoto;
}
