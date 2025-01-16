package com.example.dronepizza.controller;

import com.example.dronepizza.Repository.DroneRepository;
import com.example.dronepizza.Repository.StationRepository;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Dronestatus;
import com.example.dronepizza.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        verify(droneRepository, times(1)).findAll();
    }

    @Test
    void testAddDrone() {
        // Arrange
        Station station = new Station();
        station.setStationId(1L);

        when(stationRepository.findAll()).thenReturn(Collections.singletonList(station));
        when(droneRepository.countByStation(station)).thenReturn(0L);
        when(droneRepository.save(Mockito.any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<?> response = droneController.addDrone();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Drone);
        Drone drone = (Drone) response.getBody();
        assertNotNull(drone.getSerialUuid());
        assertEquals(Dronestatus.I_DRIFT, drone.getDriftsstatus());
        assertEquals(station, drone.getStation());
        verify(stationRepository, times(1)).findAll();
        verify(droneRepository, times(1)).save(any(Drone.class));
    }

    @Test
    void testEnableDrone() {
        // Arrange
        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setDriftsstatus(Dronestatus.UDE_AF_DRIFT);

        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = droneController.enableDrone(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Drone);
        assertEquals(Dronestatus.I_DRIFT, ((Drone) response.getBody()).getDriftsstatus());
        verify(droneRepository, times(1)).findById(1L);
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void testDisableDrone() {
        // Arrange
        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setDriftsstatus(Dronestatus.I_DRIFT);

        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<?> response = droneController.disableDrone(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Drone);
        assertEquals(Dronestatus.UDE_AF_DRIFT, ((Drone) response.getBody()).getDriftsstatus());
        verify(droneRepository, times(1)).findById(1L);
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void testRetireDrone() {
        // Arrange
        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setDriftsstatus(Dronestatus.I_DRIFT);

        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<?> response = droneController.retireDrone(1L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Drone);
        assertEquals(Dronestatus.UDFASET, ((Drone) response.getBody()).getDriftsstatus());
        verify(droneRepository, times(1)).findById(1L);
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void testChangeDroneStatusNotFound() {
        // Arrange
        when(droneRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = droneController.enableDrone(1L);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Drone ikke fundet.", response.getBody());
        verify(droneRepository, times(1)).findById(1L);
        verify(droneRepository, times(0)).save(any(Drone.class));
    }
}
