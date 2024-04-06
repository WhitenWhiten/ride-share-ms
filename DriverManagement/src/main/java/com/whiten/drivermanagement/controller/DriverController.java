package com.whiten.drivermanagement.controller;

import com.whiten.drivermanagement.entity.Driver;
import com.whiten.drivermanagement.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class DriverController {

    private final DriverService driverService;
    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }


    @GetMapping("/drivers")
    public ResponseEntity<Driver[]> searchDriver(@RequestParam(required = false) String location, @RequestParam(required = false) String type) {
        return ResponseEntity.ok(driverService.selectDrivers(location, type));
    }


    @GetMapping("/")
    public Driver getDriverById(@RequestParam long id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/update-location")
    public ResponseEntity<String> updateDriverLocation(@RequestParam long id, @RequestParam String newLocation) {
        driverService.updateDriverLocation(id, newLocation);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/close-order")
    public ResponseEntity<String> finishOrder(@RequestParam long driverId, @RequestParam double profit) {
        driverService.finishOrder(driverId, profit);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/accept-order")
    public ResponseEntity<String> acceptOrder(@RequestParam long driverId, @RequestParam long orderId) {
        driverService.acceptOrder(driverId, orderId);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/new-driver")
    public ResponseEntity<String> addDriver(@RequestParam String name, @RequestParam String vehicleType) {
        long id = driverService.nextId();
        driverService.saveDriver(new Driver(id, name, vehicleType, ""));
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/waiting-orders")
    public ResponseEntity<ArrayList<Long>> getWaitingOrders(@RequestParam long id) {
        return ResponseEntity.ok(driverService.getWaitingOrders(id));
    }

    @PutMapping("/add-waiting-order")
    public ResponseEntity<String> addWaitingOrders(@RequestParam long driverId, @RequestParam long orderId) {
        driverService.addWaitingOrder(driverId,orderId);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/driver-balance")
    public ResponseEntity<Double> getDriverBalance(@RequestParam long id) {
        return ResponseEntity.ok(driverService.getDriverBalance(id));
    }

}