package me.vilius.homesync.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Device {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    private String name;

    private DeviceType deviceType;

    private String manufacturer;

    private boolean state;

    private Double powerConsumption;

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(Double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }


    public enum DeviceType {
        LIGHT,
        THERMOSTAT,
        SPEAKER,
        SECURITY_CAMERA,
        DOOR_LOCK,
        SMOKE_DETECTOR,
        VACUUM_CLEANER,
        REFRIGERATOR,
        OVEN,
        DISHWASHER,
        WASHING_MACHINE
    }

}