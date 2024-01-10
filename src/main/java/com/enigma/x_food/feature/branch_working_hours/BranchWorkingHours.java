package com.enigma.x_food.feature.branch_working_hours;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "branch_working_hours")
public class BranchWorkingHours extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "branch_working_hours_id")
    private String branchWorkingHoursID;
    @Column(name = "open_hour")
    private LocalTime openHour;
    @Column(name = "close_hour")
    private LocalTime closeHour;
    @Column(name = "days")
    @Enumerated(EnumType.STRING)
    private DayOfWeek days;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private MerchantBranch merchantBranch;
}
