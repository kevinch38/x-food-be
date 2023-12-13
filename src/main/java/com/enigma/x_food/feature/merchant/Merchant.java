package com.enigma.x_food.feature.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "Merchant")
public class Merchant {
    @Id
    private String merchantID;
    @Column(name = "merchant_name", nullable = false, length = 64)
    private String merchantName;
    @Column(name = "pic_name", nullable = false)
    private String picName;
    @Column(name = "pic_number", nullable = false)
    private String picNumber;
    @Column(name = "pic_email", nullable = false)
    private String picEmail;
    @Column(name = "merchant_description", nullable = false)
    private String merchantDescription;
    @Column(name = "notes", nullable = false)
    private String notes;
    @Column(name = "join_date", nullable = false)
    private Timestamp joinDate;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}
