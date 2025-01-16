package com.example.dronepizza.controller;

import com.example.dronepizza.Repository.DroneRepository;
import com.example.dronepizza.Repository.LeveringRepository;
import com.example.dronepizza.Repository.PizzaRepository;
import com.example.dronepizza.model.Drone;
import com.example.dronepizza.model.Dronestatus;
import com.example.dronepizza.model.Levering;
import com.example.dronepizza.model.Pizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeveringControllerTest {

    @Mock
    private LeveringRepository leveringRepository;

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private DroneRepository droneRepository;

    @InjectMocks
    private LeveringController leveringController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDeliveries() {
        when(leveringRepository.findByFaktiskLeveringIsNull()).thenReturn(List.of(new Levering()));

        ResponseEntity<?> response = leveringController.getAllDeliveries();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(leveringRepository, times(1)).findByFaktiskLeveringIsNull();
    }

    @Test
    void addDelivery() {
        Pizza pizza = new Pizza();
        pizza.setPizzaId(1L);

        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));

        ResponseEntity<?> response = leveringController.addDelivery(1L, "Test Address");

        assertEquals(200, response.getStatusCodeValue());
        Levering levering = (Levering) response.getBody();
        assertNotNull(levering);
        assertEquals("Test Address", levering.getAdresse());
        verify(leveringRepository, times(1)).save(any(Levering.class));
    }

    @Test
    void getDeliveriesWithoutDrone() {
        when(leveringRepository.findByDroneIsNull()).thenReturn(List.of(new Levering()));

        ResponseEntity<?> response = leveringController.getDeliveriesWithoutDrone();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(leveringRepository, times(1)).findByDroneIsNull();
    }

    @Test
    void scheduleDelivery() {
        Levering levering = new Levering();
        levering.setLeveringId(1L);

        Drone drone = new Drone();
        drone.setDroneId(1L);
        drone.setDriftsstatus(Dronestatus.I_DRIFT);

        when(leveringRepository.findById(1L)).thenReturn(Optional.of(levering));
        when(droneRepository.findAll()).thenReturn(List.of(drone));

        ResponseEntity<?> response = leveringController.scheduleDelivery(1L, null);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(leveringRepository, times(1)).save(any(Levering.class));
    }

    @Test
    void finishDelivery() {
        Levering levering = new Levering();
        levering.setLeveringId(1L);
        levering.setDrone(new Drone());

        when(leveringRepository.findById(1L)).thenReturn(Optional.of(levering));

        ResponseEntity<?> response = leveringController.finishDelivery(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Levering afsluttet!", response.getBody());
        assertNotNull(levering.getFaktiskLevering());
        verify(leveringRepository, times(1)).save(any(Levering.class));
    }
}
