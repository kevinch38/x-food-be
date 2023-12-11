package com.enigma.x_food.util;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidationUtil {
    private final Validator validator;

    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object obj){
        Set<ConstraintViolation<Object>> validate = validator.validate(obj);
        if (!validate.isEmpty()){
            throw new ConstraintViolationException(validate);
        }
    }
}
