package com.example.dronepizza.model;

import jakarta.persistence.*;
@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long droneId;
    private String serialUuid;
    @Enumerated(EnumType.STRING)
    private Dronestatus driftsstatus;

    @ManyToOne
    private Station station;

    public Drone(Dronestatus driftsstatus, Station station) {
        this.driftsstatus = driftsstatus;
        this.station = station;
    }

    // Default constructor
    public Drone() {
    }

    // Getters and setters
    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public String getSerialUuid() {
        return serialUuid;
    }

    public void setSerialUuid(String serialUuid) {
        this.serialUuid = serialUuid;
    }

    public Dronestatus getDriftsstatus() {
        return driftsstatus;
    }

    public void setDriftsstatus(Dronestatus driftsstatus) {
        this.driftsstatus = driftsstatus;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
