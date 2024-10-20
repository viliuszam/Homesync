package me.vilius.homesync.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.vilius.homesync.model.Room;
import me.vilius.homesync.validation.enumtype.room.RoomTypeSubset;

public class RoomDTO {

    @NotBlank(message = "Room name is required")
    @Size(min = 2, max = 50, message = "Room name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Room type is required")
    @RoomTypeSubset(anyOf = {Room.RoomType.BATHROOM, Room.RoomType.LIVING_ROOM, Room.RoomType.BEDROOM,
                                Room.RoomType.GARAGE,Room.RoomType.KITCHEN, Room.RoomType.OFFICE})
    private Room.RoomType roomType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room.RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(Room.RoomType roomType) {
        this.roomType = roomType;
    }
}