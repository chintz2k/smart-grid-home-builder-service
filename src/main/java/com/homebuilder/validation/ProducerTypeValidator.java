package com.homebuilder.validation;

import com.homebuilder.dto.ProducerRequest;
import com.homebuilder.entity.Producer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author André Heinen
 */
public class ProducerTypeValidator implements ConstraintValidator<ValidProducerType, Object> {

	@Override
	public void initialize(ValidProducerType constraintAnnotation) {}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		if (value instanceof Producer producer) {
			return hasValidEnergyType(producer, context);
		} else if (value instanceof ProducerRequest request) {
			return hasValidEnergyType(request, context);
		} else {
			throw new IllegalArgumentException("Unsupported type for validation: " + value.getClass().getName());
		}
	}

	private boolean hasValidEnergyType(Object instance, ConstraintValidatorContext context) {
		boolean hasAnyEnergyType = false;
		if (instance instanceof Producer producer) {
			hasAnyEnergyType = producer.isSolarPower() || producer.isWindPower()
					|| producer.isHydroPower() || producer.isGeothermalPower()
					|| producer.isBiomassPower() || producer.isCoalPower()
					|| producer.isNaturalGasPower() || producer.isOilPower()
					|| producer.isNuclearPower();
		} else if (instance instanceof ProducerRequest request) {
			hasAnyEnergyType = request.isSolarPower() || request.isWindPower()
					|| request.isHydroPower() || request.isGeothermalPower()
					|| request.isBiomassPower() || request.isCoalPower()
					|| request.isNaturalGasPower() || request.isOilPower()
					|| request.isNuclearPower();
		}
		if (!hasAnyEnergyType) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("At least one energy type must be selected")
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
