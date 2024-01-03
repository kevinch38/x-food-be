package com.enigma.x_food.feature.variety;

import com.enigma.x_food.feature.variety_sub_variety.VarietySubVariety;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "variety")
    private List<VarietySubVariety> varietySubVarieties;
}
