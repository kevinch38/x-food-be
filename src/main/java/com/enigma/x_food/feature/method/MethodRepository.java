package com.enigma.x_food.feature.method;

import com.enigma.x_food.constant.EMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MethodRepository extends JpaRepository<Method, String> {
    Optional<Method> findByMethodName(EMethod name);
}
