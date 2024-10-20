package me.vilius.homesync.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import me.vilius.homesync.validation.timezone.ValidTimeZone;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeDTO {

    @NotBlank(message = "Home name is required")
    @Size(min = 3, max = 100, message = "Home name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 255, message = "Address must be between 10 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s,.-]+$", message = "Address must contain only letters, numbers, spaces, commas, periods, and dashes")
    private String address;

    @NotBlank(message = "Time zone is required")
    @ValidTimeZone(message = "Invalid timezone provided. Please use a valid timezone ID")
    private String timeZone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
