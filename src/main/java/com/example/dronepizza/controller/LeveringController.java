package com.example.dronepizza.controller;

import com.example.dronepizza.Repository.DroneRepository;
import com.example.dronepizza.Repository.LeveringRepository;
import com.example.dronepizza.Repository.PizzaRepository;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Dronestatus;
import com.example.dronepizza.model.Levering;
import com.example.dronepizza.model.Pizza;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deliveries")
public class LeveringController {
    private final LeveringRepository leveringRepository;
    private final PizzaRepository pizzaRepository;
    private final DroneRepository droneRepository;

    public LeveringController(LeveringRepository leveringRepository, PizzaRepository pizzaRepository, DroneRepository droneRepository) {
        this.leveringRepository = leveringRepository;
        this.pizzaRepository = pizzaRepository;
        this.droneRepository = droneRepository;
    }

    @GetMapping()
    public ResponseEntity<?> getAllDeliveries() {
        List<Levering> leveringer = leveringRepository.findByFaktiskLeveringIsNull();
        return ResponseEntity.ok(leveringer);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDelivery(@RequestParam Long pizzaId, @RequestParam String adresse) {
        Optional<Pizza> pizza = pizzaRepository.findById(pizzaId);
        if (pizza.isEmpty()) {
            return ResponseEntity.badRequest().body("Pizza med ID " + pizzaId + " findes ikke.");
        }

        Levering levering = new Levering();
        levering.setPizza(pizza.get());
        levering.setAdresse(adresse);
        levering.setForventetLevering(LocalDateTime.now().plusMinutes(30));
        leveringRepository.save(levering);
        return ResponseEntity.ok(levering);
    }

    @GetMapping("/queue")
    public ResponseEntity<?> getDeliveriesWithoutDrone() {
        return ResponseEntity.ok(leveringRepository.findByDroneIsNull());
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleDelivery(@RequestParam Long leveringId, @RequestParam(required = false) Long droneId) {
        if (leveringId == null) {
            return ResponseEntity.badRequest().body("Levering ID må ikke være null.");
        }

        Optional<Levering> levering = leveringRepository.findById(leveringId);
        if (levering.isEmpty()) {
            return ResponseEntity.badRequest().body("Levering ikke fundet.");
        }
        if (levering.get().getDrone() != null) {
            return ResponseEntity.badRequest().body("Leveringen er allerede skeduleret.");
        }

        Drone drone;
        if (droneId != null) {
            Optional<Drone> optionalDrone = droneRepository.findById(droneId);
            if (optionalDrone.isEmpty()) {
                return ResponseEntity.badRequest().body("Drone ikke fundet.");
            }
            drone = optionalDrone.get();
        } else {
            List<Drone> droner = droneRepository.findAll();
            if (droner.isEmpty()) {
                return ResponseEntity.badRequest().body("Ingen droner tilgængelige.");
            }
            drone = droner.stream()
                    .filter(d -> d.getDriftsstatus() == Dronestatus.I_DRIFT)
                    .min(Comparator.comparingInt(d -> leveringRepository.countByDrone(d)))
                    .orElse(droner.get(0));
        }

        if (drone.getDriftsstatus() != Dronestatus.I_DRIFT) {
            return ResponseEntity.badRequest().body("Dronen er ikke i drift.");
        }
        levering.get().setDrone(drone);
        leveringRepository.save(levering.get());
        return ResponseEntity.ok(levering.get());
    }

    @PostMapping("/finish")
    public ResponseEntity<?> finishDelivery(@RequestParam Long leveringId) {
        Optional<Levering> levering = leveringRepository.findById(leveringId);
        if (levering.isEmpty()) {
            return ResponseEntity.status(404).body("Levering ikke fundet.");
        }

        if (levering.get().getFaktiskLevering() != null) {
            return ResponseEntity.badRequest().body("Levering er allerede afsluttet.");
        }

        if (levering.get().getDrone() == null) {
            return ResponseEntity.badRequest().body("Levering har ingen drone tilknyttet.");
        }

        levering.get().setFaktiskLevering(LocalDateTime.now());
        leveringRepository.save(levering.get());

        return ResponseEntity.ok("Levering afsluttet!");
    }
}
