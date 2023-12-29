package com.enigma.x_food.feature.admin;

import com.enigma.x_food.feature.admin.dto.request.NewAdminRequest;
import com.enigma.x_food.feature.admin.dto.request.UpdateAdminRequest;
import com.enigma.x_food.feature.admin.dto.response.AdminResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping(path = "/register",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewAdmin(@RequestBody NewAdminRequest request) {
        AdminResponse adminResponse = adminService.createNew(request);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .message("successfully create new admin")
                .statusCode(HttpStatus.CREATED.value())
                .data(adminResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getAdminById(@PathVariable String id) {
//        AdminResponse adminResponse = adminService.getById(id);
//        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
//                .message("successfully get admin")
//                .statusCode(HttpStatus.OK.value())
//                .data(adminResponse)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(response);
//    }

    @PutMapping
    public ResponseEntity<?> updateAdmin(@RequestBody UpdateAdminRequest request) {
        AdminResponse adminResponse = adminService.update(request);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .message("successfully update admin")
                .statusCode(HttpStatus.OK.value())
                .data(adminResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
