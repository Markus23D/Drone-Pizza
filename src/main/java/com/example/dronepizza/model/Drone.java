package com.example.dronepizza.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long droneId;
    private String serialUuid = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    private Dronestatus driftsstatus;

    @ManyToOne
    private Station station;

    // Tom konstruktør
    public Drone() {
    }

    // Konstruktør med felter
    public Drone(Dronestatus driftsstatus, Station station) {
        this.driftsstatus = driftsstatus;
        this.station = station;
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
