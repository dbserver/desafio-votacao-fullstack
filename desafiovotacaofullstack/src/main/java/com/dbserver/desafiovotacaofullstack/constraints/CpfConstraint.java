package com.dbserver.desafiovotacaofullstack.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = CpfValidator.class) 
@Target({ ElementType.FIELD }) 
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfConstraint {
	
	String message() default "Cpf inválido"; 
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
