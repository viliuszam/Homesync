package me.vilius.homesync.validation.timezone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.TimeZone;

public class TimeZoneValidator implements ConstraintValidator<ValidTimeZone, String> {

    @Override
    public void initialize(ValidTimeZone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String timeZoneStr, ConstraintValidatorContext context) {
        if (timeZoneStr == null || timeZoneStr.isEmpty()) {
            return false;
        }

        String[] validTimeZones = TimeZone.getAvailableIDs();
        for (String timeZone : validTimeZones) {
            if (timeZone.equals(timeZoneStr)) {
                return true;
            }
        }
        return false;
    }
}
