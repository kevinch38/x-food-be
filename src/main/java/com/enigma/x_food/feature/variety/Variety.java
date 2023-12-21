package com.enigma.x_food.feature.variety;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "variety")
public class Variety {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "variety_id")
    private String varietyID;

    @Column(name = "variety_name", nullable = false, length = 64)
    private String varietyName;

    @Column(name = "is_required", nullable = false, length = 100)
    private Boolean isRequired;

    @Column(name = "is_multi_select", nullable = false, length = 15, unique = true)
    private Boolean isMultiSelect;
}
