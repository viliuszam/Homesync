package me.vilius.homesync.validation.enumtype.room;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.vilius.homesync.model.Room;

import java.util.Arrays;

public class RoomTypeSubSetValidator implements ConstraintValidator<RoomTypeSubset, Room.RoomType> {
    private Room.RoomType[] subset;

    @Override
    public void initialize(RoomTypeSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Room.RoomType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}