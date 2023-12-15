package com.enigma.x_food.feature.pin;

import com.enigma.x_food.shared.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "Pin")
public class Pin extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "pin_id")
    private String pinID;

    @Column(name = "pin", nullable = false, length = 64)
    private String pin;}
