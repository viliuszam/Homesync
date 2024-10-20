package me.vilius.homesync.validation.enumtype.device;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.vilius.homesync.model.Device;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DeviceTypeSubSetValidator.class)
public @interface DeviceTypeSubset {
    Device.DeviceType[] anyOf();
    String message() default "must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
