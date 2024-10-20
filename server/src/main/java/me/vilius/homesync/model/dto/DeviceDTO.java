package me.vilius.homesync.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import me.vilius.homesync.model.Device;
import me.vilius.homesync.validation.enumtype.device.DeviceTypeSubset;

public class DeviceDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotNull(message = "Device type is required")
    @DeviceTypeSubset(anyOf = {Device.DeviceType.DISHWASHER, Device.DeviceType.LIGHT, Device.DeviceType.OVEN,
            Device.DeviceType.DOOR_LOCK, Device.DeviceType.REFRIGERATOR, Device.DeviceType.SECURITY_CAMERA,
            Device.DeviceType.SMOKE_DETECTOR, Device.DeviceType.SPEAKER, Device.DeviceType.THERMOSTAT, Device.DeviceType.VACUUM_CLEANER,
            Device.DeviceType.WASHING_MACHINE})
    private Device.DeviceType deviceType;

    @NotBlank(message = "Manufacturer is required")
    @Size(max = 100, message = "Manufacturer must be at most 100 characters")
    private String manufacturer;

    private boolean state;

    @PositiveOrZero(message = "Power consumption must be a positive number or zero")
    private Double powerConsumption;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Device.DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Device.DeviceType deviceType) {
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

    public Device toEntity() {
        Device device = new Device();
        device.setName(this.name);
        device.setDeviceType(this.deviceType);
        device.setManufacturer(this.manufacturer);
        device.setState(this.state);
        device.setPowerConsumption(this.powerConsumption);
        return device;
    }

    public static DeviceDTO fromEntity(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setName(device.getName());
        dto.setDeviceType(device.getDeviceType());
        dto.setManufacturer(device.getManufacturer());
        dto.setState(device.isState());
        dto.setPowerConsumption(device.getPowerConsumption());
        return dto;
    }
}