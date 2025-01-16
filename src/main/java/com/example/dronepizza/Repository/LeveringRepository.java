package com.example.dronepizza.Repository;

import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Levering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LeveringRepository extends JpaRepository<Levering, Long> {
    List<Levering> findByDroneIsNull();
    List<Levering> findByFaktiskLeveringIsNull();
    int countByDrone(Drone drone);
}