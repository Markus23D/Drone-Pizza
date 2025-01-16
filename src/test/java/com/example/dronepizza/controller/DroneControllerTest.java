package com.example.dronepizza.controller;

import com.example.dronepizza.Repository.DroneRepository;
import com.example.dronepizza.Repository.StationRepository;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Dronestatus;
import com.example.dronepizza.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DroneControllerTest {

    private DroneRepository droneRepository;
    private StationRepository stationRepository;
    private DroneController droneController;

    @BeforeEach
    void setUp() {
        droneRepository = mock(DroneRepository.class);
        stationRepository = mock(StationRepository.class);
        droneController = new DroneController(droneRepository, stationRepository);
    }

    @Test
    void testGetAllDrones() {
        when(droneRepository.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = droneController.getAllDrones();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Iterable);
    }

    @Test
    void testAddDrone() {
        Station station = new Station();
        station.setStationId(1L);
        when(stationRepository.findAll()).thenReturn(Collections.singletonList(station));
        when(droneRepository.countByStation(station)).thenReturn(0L);
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResponseEntity<?> response = droneController.addDrone();
        assertEquals(200, response.getStatusCodeValue());
        Drone drone = (Drone) response.getBody();
        assertNotNull(drone);
        assertEquals(Dronestatus.I_DRIFT, drone.getDriftsstatus());
        assertEquals(station, drone.getStation());
    }

    @Test
    void testEnableDrone() {
        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setDriftsstatus(Dronestatus.UDE_AF_DRIFT);
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResponseEntity<?> response = droneController.enableDrone(1L);
        assertEquals(200, response.getStatusCodeValue());
        Drone updatedDrone = (Drone) response.getBody();
        assertNotNull(updatedDrone);
        assertEquals(Dronestatus.I_DRIFT, updatedDrone.getDriftsstatus());
    }

    @Test
    void testDisableDrone() {

        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setDriftsstatus(Dronestatus.I_DRIFT);
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResponseEntity<?> response = droneController.disableDrone(1L);
        assertEquals(200, response.getStatusCodeValue());
        Drone updatedDrone = (Drone) response.getBody();
        assertNotNull(updatedDrone);
        assertEquals(Dronestatus.UDE_AF_DRIFT, updatedDrone.getDriftsstatus());
    }

    @Test
    void testRetireDrone() {
        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setDriftsstatus(Dronestatus.I_DRIFT);
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResponseEntity<?> response = droneController.retireDrone(1L);
        assertEquals(200, response.getStatusCodeValue());
        Drone retiredDrone = (Drone) response.getBody();
        assertNotNull(retiredDrone);
        assertEquals(Dronestatus.UDFASET, retiredDrone.getDriftsstatus());
    }

}
