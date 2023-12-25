package com.enigma.x_food.feature.friend;

import com.enigma.x_food.feature.user.User;
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
@Table(name = "friend")
public class Friend extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "friend_id")
    private String friendID;
    @ManyToOne
    @JoinColumn(name = "account_id_1", nullable = false)
    private User user1;
    @ManyToOne
    @JoinColumn(name = "account_id_2", nullable = false)
    private User user2;
}
