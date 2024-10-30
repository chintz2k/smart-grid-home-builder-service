package com.homebuilder.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author André Heinen
 */
@Documented
@Constraint(validatedBy = ProducerTypeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProducerType {

	String message() default "Invalid energy type configuration";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
