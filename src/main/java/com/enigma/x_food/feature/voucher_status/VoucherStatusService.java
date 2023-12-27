package com.enigma.x_food.feature.voucher_status;

import com.enigma.x_food.constant.EVoucherStatus;
import com.enigma.x_food.feature.voucher_status.response.VoucherStatusResponse;

import java.util.List;

public interface VoucherStatusService {
    VoucherStatus getByStatus(EVoucherStatus status);
    List<VoucherStatusResponse> getAll();
}
