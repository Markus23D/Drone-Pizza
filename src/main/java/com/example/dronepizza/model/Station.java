package com.example.dronepizza.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;
    private double latitude;
    private double longitude;

    // Tom konstruktør
    public Station() {
    }

    public Station(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }


    public Long getStationId() {
        return stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
