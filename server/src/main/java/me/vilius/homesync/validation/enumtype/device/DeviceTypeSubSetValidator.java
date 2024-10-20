package me.vilius.homesync.validation.enumtype.device;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.vilius.homesync.model.Device;

import java.util.Arrays;

public class DeviceTypeSubSetValidator implements ConstraintValidator<DeviceTypeSubset, Device.DeviceType> {
    private Device.DeviceType[] subset;

    @Override
    public void initialize(DeviceTypeSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Device.DeviceType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
