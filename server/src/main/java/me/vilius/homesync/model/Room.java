package me.vilius.homesync.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private RoomType roomType;

    public void setId(Long id) {
        this.id = id;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "home_id")
    @JsonIgnore
    private Home home;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    public enum RoomType {
        LIVING_ROOM,
        KITCHEN,
        BEDROOM,
        BATHROOM,
        GARAGE,
        OFFICE
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Home getHome() {
        return home;
    }

    public List<Device> getDevices() {
        return devices;
    }
}