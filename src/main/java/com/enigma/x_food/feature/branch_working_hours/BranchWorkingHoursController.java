package com.enigma.x_food.feature.branch_working_hours;

import com.enigma.x_food.feature.branch_working_hours.dto.request.UpdateBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/branch/working-hours")
@RequiredArgsConstructor
public class BranchWorkingHoursController {
    private final BranchWorkingHoursService branchWorkingHoursService;

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createNew(@RequestBody NewBranchWorkingHoursRequest request) {
//        BranchWorkingHours branchWorkingHours = branchWorkingHoursService.createNew(request);
//        CommonResponse<BranchWorkingHours> response = CommonResponse.<BranchWorkingHours>builder()
//                .message("successfully create new branchWorkingHours")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(branchWorkingHours)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateBranchWorkingHoursRequest request) {
        BranchWorkingHoursResponse branchWorkingHoursResponse = branchWorkingHoursService.update(request);
        CommonResponse<BranchWorkingHoursResponse> response = CommonResponse.<BranchWorkingHoursResponse>builder()
                .message("successfully update branchWorkingHours")
                .statusCode(HttpStatus.OK.value())
                .data(branchWorkingHoursResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        BranchWorkingHoursResponse branchWorkingHoursResponse = branchWorkingHoursService.findById(id);
        CommonResponse<BranchWorkingHoursResponse> response = CommonResponse.<BranchWorkingHoursResponse>builder()
                .message("successfully get branchWorkingHours")
                .statusCode(HttpStatus.OK.value())
                .data(branchWorkingHoursResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
