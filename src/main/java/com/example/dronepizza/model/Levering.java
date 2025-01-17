package com.example.dronepizza.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Levering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leveringId;
    private String adresse;
    private LocalDateTime forventetLevering;
    private LocalDateTime faktiskLevering;

    @ManyToOne
    private Pizza pizza;

    @ManyToOne
    private Drone drone;


    public Long getLeveringId() {
        return leveringId;
    }

    public void setLeveringId(Long leveringId) {
        this.leveringId = leveringId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDateTime getForventetLevering() {
        return forventetLevering;
    }

    public void setForventetLevering(LocalDateTime forventetLevering) {
        this.forventetLevering = forventetLevering;
    }

    public LocalDateTime getFaktiskLevering() {
        return faktiskLevering;
    }

    public void setFaktiskLevering(LocalDateTime faktiskLevering) {
        this.faktiskLevering = faktiskLevering;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }
}
