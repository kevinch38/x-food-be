package com.enigma.x_food.feature.voucher_status;

import com.enigma.x_food.constant.EVoucherStatus;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "voucher_status")
public class VoucherStatus extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "voucher_status_id")
    private String voucherStatusID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private EVoucherStatus status;
}
