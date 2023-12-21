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
    private final BranchWorkingHoursService balanceService;

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createNew(@RequestBody NewBranchWorkingHoursRequest request) {
//        BranchWorkingHours balance = balanceService.createNew(request);
//        CommonResponse<BranchWorkingHours> response = CommonResponse.<BranchWorkingHours>builder()
//                .message("successfully create new balance")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(balance)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateBranchWorkingHoursRequest request) {
        BranchWorkingHoursResponse balanceResponse = balanceService.update(request);
        CommonResponse<BranchWorkingHoursResponse> response = CommonResponse.<BranchWorkingHoursResponse>builder()
                .message("successfully update balance")
                .statusCode(HttpStatus.OK.value())
                .data(balanceResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        BranchWorkingHoursResponse balanceResponse = balanceService.findById(id);
        CommonResponse<BranchWorkingHoursResponse> response = CommonResponse.<BranchWorkingHoursResponse>builder()
                .message("successfully get balance")
                .statusCode(HttpStatus.OK.value())
                .data(balanceResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
