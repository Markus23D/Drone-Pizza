package com.example.dronepizza.controller;

import com.example.dronepizza.Repository.DroneRepository;
import com.example.dronepizza.Repository.StationRepository;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Dronestatus;
import com.example.dronepizza.model.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/drones")
public class DroneController {
    private final DroneRepository droneRepository;
    private final StationRepository stationRepository;

    public DroneController(DroneRepository droneRepository, StationRepository stationRepository) {
        this.droneRepository = droneRepository;
        this.stationRepository = stationRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllDrones() {
        return ResponseEntity.ok(droneRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDrone() {
        Optional<Station> station = stationRepository.findAll().stream()
                .min((s1, s2) -> Long.compare(
                        droneRepository.countByStation(s1),
                        droneRepository.countByStation(s2)
                ));
        if (station.isEmpty()) {
            return ResponseEntity.badRequest().body("Ingen stationer fundet.");
        }

        Drone drone = new Drone();
        drone.setSerialUuid(UUID.randomUUID().toString());
        drone.setDriftsstatus(Dronestatus.I_DRIFT);
        drone.setStation(station.get());
        droneRepository.save(drone);
        return ResponseEntity.ok(drone);
    }

    @PostMapping("/enable")
    public ResponseEntity<?> enableDrone(@RequestParam Long id) {
        return changeDroneStatus(id, Dronestatus.I_DRIFT);
    }

    @PostMapping("/disable")
    public ResponseEntity<?> disableDrone(@RequestParam Long id) {
        return changeDroneStatus(id, Dronestatus.UDE_AF_DRIFT);
    }

    @PostMapping("/retire")
    public ResponseEntity<?> retireDrone(@RequestParam Long id) {
        return changeDroneStatus(id, Dronestatus.UDFASET);
    }

    private ResponseEntity<?> changeDroneStatus(Long id, Dronestatus status) {
        Optional<Drone> drone = droneRepository.findById(id);
        if (drone.isEmpty()) {
            return ResponseEntity.badRequest().body("Drone ikke fundet.");
        }
        drone.get().setDriftsstatus(status);
        droneRepository.save(drone.get());
        return ResponseEntity.ok(drone.get());
    }
}
