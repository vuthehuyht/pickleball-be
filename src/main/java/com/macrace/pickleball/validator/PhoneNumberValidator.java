package com.macrace.pickleball.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return false;

        String reqex = "(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b";
        return s.matches(reqex);
    }
}
