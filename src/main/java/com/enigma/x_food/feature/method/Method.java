package com.enigma.x_food.feature.method;

import com.enigma.x_food.constant.EMethod;
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
@Table(name = "method")
public class Method extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "method_id")
    private String methodID;

    @Enumerated(EnumType.STRING)
    @Column(name = "method_name", nullable = false, length = 50)
    private EMethod methodName;
}
