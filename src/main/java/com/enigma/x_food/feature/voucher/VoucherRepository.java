package com.enigma.x_food.feature.voucher;

import com.enigma.x_food.feature.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
    Optional<List<Voucher>> findByPromotion(Promotion promotion);
    Optional<Voucher> findByVoucherCode(String voucherCode);

}
