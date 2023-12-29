package com.enigma.x_food.feature.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String phoneNumber;
    private LocalDate startCreatedAt;
    private LocalDate startUpdatedAt;
    private LocalDate endCreatedAt;
    private LocalDate endUpdatedAt;
}
