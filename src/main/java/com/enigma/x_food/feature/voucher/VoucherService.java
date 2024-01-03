package com.enigma.x_food.feature.voucher;

import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.feature.voucher.dto.request.NewVoucherRequest;
import com.enigma.x_food.feature.voucher.dto.request.SearchVoucherPromotionRequest;
import com.enigma.x_food.feature.voucher.dto.request.SearchVoucherRequest;
import com.enigma.x_food.feature.voucher.dto.response.VoucherResponse;
import com.enigma.x_food.feature.voucher.dto.request.UpdateVoucherRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VoucherService {
    VoucherResponse createNew(NewVoucherRequest request);
    Page<VoucherResponse> getAll(SearchVoucherRequest request);
    VoucherResponse findById(String id);
    Voucher getVoucherById(String id);
    List<Voucher> getVoucherByPromotion(Promotion promotion);
    List<VoucherResponse> getVoucherByPromotionId(SearchVoucherPromotionRequest request);
    VoucherResponse update(UpdateVoucherRequest request);
    void deleteById(String id);


}
