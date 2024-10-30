package com.homebuilder.validation;

import com.homebuilder.entity.Producer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author André Heinen
 */
public class ProducerTypeValidator implements ConstraintValidator<ValidProducerType, Producer> {

	@Override
	public void initialize(ValidProducerType constraintAnnotation) {
	}

	@Override
	public boolean isValid(Producer producer, ConstraintValidatorContext context) {
		boolean hasAnyEnergyType = producer.isSolarPower() || producer.isWindPower()
				|| producer.isHydroPower() || producer.isGeothermalPower()
				|| producer.isBiomassPower() || producer.isCoalPower()
				|| producer.isNaturalGasPower() || producer.isOilPower()
				|| producer.isNuclearPower();

		if (!hasAnyEnergyType) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("At least one energy type must be selected")
					.addConstraintViolation();
			return false;
		}

		return true;
	}
}
