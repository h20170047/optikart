package com.svj.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyCharactersValidation implements ConstraintValidator<OnlyCharacters, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value!= null && value.matches("^[a-zA-Z]+$") ){
            return true;
        }
        return false;
    }
}
