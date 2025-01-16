package com.example.dronepizza.Repository;

import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    long countByStation(Station station);
}