package com.example.dronepizza.Repository;

import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    long countByStation(Station station);
}