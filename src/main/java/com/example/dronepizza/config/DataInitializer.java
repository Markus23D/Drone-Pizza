package com.example.dronepizza.config;

import com.example.dronepizza.Repository.DroneRepository;
import com.example.dronepizza.Repository.PizzaRepository;
import com.example.dronepizza.Repository.StationRepository;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Dronestatus;
import com.example.dronepizza.model.Pizza;
import com.example.dronepizza.model.Station;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {
    private final PizzaRepository pizzaRepository;
    private final StationRepository stationRepository;
    private final DroneRepository droneRepository;

    public DataInitializer(PizzaRepository pizzaRepository, StationRepository stationRepository, DroneRepository droneRepository) {
        this.pizzaRepository = pizzaRepository;
        this.stationRepository = stationRepository;
        this.droneRepository = droneRepository;
    }

    @Override
    public void run(String... args) {
        //opretter jeg 5 pizzaer
        pizzaRepository.saveAll(List.of(
                new Pizza("Margherita", 75),
                new Pizza("Pepperoni", 85),
                new Pizza("Hawaii", 90),
                new Pizza("Vegetariana", 80),
                new Pizza("Capricciosa", 95)
        ));

        // Opret jeg 3 stationer
        stationRepository.saveAll(List.of(
                new Station(45.41, 9.34),
                new Station(55.42, 12.35),
                new Station(65.40, 20.33)
        ));

        // Opretter jeg 3 droner
        Drone drone1 = new Drone(Dronestatus.I_DRIFT, stationRepository.findAll().get(0));
        drone1.setSerialUuid(UUID.randomUUID().toString());
        Drone drone2 = new Drone(Dronestatus.I_DRIFT, stationRepository.findAll().get(1));
        drone2.setSerialUuid(UUID.randomUUID().toString());
        Drone drone3 = new Drone(Dronestatus.I_DRIFT, stationRepository.findAll().get(2));
        drone3.setSerialUuid(UUID.randomUUID().toString());
        droneRepository.saveAll(List.of(drone1, drone2, drone3));
    }
}
