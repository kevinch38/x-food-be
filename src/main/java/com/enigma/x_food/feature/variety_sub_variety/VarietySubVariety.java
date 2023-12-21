package com.enigma.x_food.feature.variety_sub_variety;

import com.enigma.x_food.feature.sub_variety.SubVariety;
import com.enigma.x_food.feature.variety.Variety;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "variety_sub_variety")
public class VarietySubVariety {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "variety_sub_variety_id")
    private String varietySubVarietyID;

    @ManyToOne
    @JoinColumn(name = "sub_variety_id", nullable = false)
    private SubVariety subVariety;

    @ManyToOne
    @JoinColumn(name = "variety_id", nullable = false)
    private Variety variety;
}
