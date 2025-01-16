package com.example.dronepizza.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pizzaId;
    private String titel;
    private int pris;

    // Tom konstruktør
    public Pizza() {
    }


    public Pizza(String titel, int pris) {
        this.titel = titel;
        this.pris = pris;
    }


    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }



    public void setTitel(String titel) {
        this.titel = titel;
    }

    public int getPris() {
        return pris;
    }

    public void setPris(int pris) {
        this.pris = pris;
    }
}
