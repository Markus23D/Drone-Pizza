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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LeveringControllerTest {

    private LeveringRepository leveringRepository;
    private PizzaRepository pizzaRepository;
    private DroneRepository droneRepository;
    private LeveringController leveringController;

    @BeforeEach
    void setUp() {
        leveringRepository = mock(LeveringRepository.class);
        pizzaRepository = mock(PizzaRepository.class);
        droneRepository = mock(DroneRepository.class);
        leveringController = new LeveringController(leveringRepository, pizzaRepository, droneRepository);
    }

    @Test
    void getAllDeliveries() {
        List<Levering> mockDeliveries = List.of(new Levering());
        when(leveringRepository.findByFaktiskLeveringIsNull()).thenReturn(mockDeliveries);
        ResponseEntity<?> response = leveringController.getAllDeliveries();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockDeliveries, response.getBody());
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
        assertEquals(pizza, levering.getPizza());
    }

    @Test
    void getDeliveriesWithoutDrone() {
        List<Levering> mockDeliveries = List.of(new Levering());
        when(leveringRepository.findByDroneIsNull()).thenReturn(mockDeliveries);
        ResponseEntity<?> response = leveringController.getDeliveriesWithoutDrone();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockDeliveries, response.getBody());
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
    }
}
