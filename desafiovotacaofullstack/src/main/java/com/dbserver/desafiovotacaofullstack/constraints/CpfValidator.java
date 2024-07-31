package com.dbserver.desafiovotacaofullstack.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CpfConstraint, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (value == null) {
            return false; 
        }

        return value.matches("^[0-9.-]*$");
	}

}
