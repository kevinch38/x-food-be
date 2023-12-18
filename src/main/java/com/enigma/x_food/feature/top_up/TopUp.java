package com.enigma.x_food.feature.top_up;

import com.enigma.x_food.feature.history.History;
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
@Table(name = "top_up")
public class TopUp extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "top_up_id")
    private String topUpID;
    @Column(name = "top_up_amount", nullable = false, length = 11)
    private Double topUpAmount;
    @Column(name = "method_id", nullable = false)
    private String methodID;
    @Column(name = "top_up_fee", nullable = false, length = 11)
    private Double topUpFee;
    @Column(name = "top_up_status_id", nullable = false)
    private String topUpStatusID;
    @Column(name = "balance_id", nullable = false)
    private String balanceID;
    @JoinColumn(name = "history_id", nullable = false)
    @OneToOne
    private History history;
}
