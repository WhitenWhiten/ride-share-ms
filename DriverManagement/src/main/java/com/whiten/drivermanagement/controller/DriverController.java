package com.whiten.drivermanagement.controller;

import com.whiten.drivermanagement.entity.Driver;
import com.whiten.drivermanagement.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all-drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok((List<Driver>) driverService.getAllDrivers());
    }

    @GetMapping("/free-drivers")
    public ResponseEntity<List<Driver>> freeDrivers() {
        List<Driver> ret = new ArrayList<>();
        ArrayList<Driver> all =  driverService.getAllDrivers();
        for (Driver d : all) {
            if (d.is_free())
                ret.add(d);
        }
        return ResponseEntity.ok(ret);
    }


    @GetMapping("/{id}")
    public Driver getDriverById(@PathVariable long id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<String> updateDriverLocation(@PathVariable long id, @RequestBody String newLocation) {
        driverService.updateDriverLocation(id, newLocation);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/{id}/finish-order")
    public ResponseEntity<String> finishOrder(@PathVariable long id) {
        driverService.finishOrder(id);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/{id}/new-order")
    public ResponseEntity<String> finishOrder(@PathVariable long id, @RequestBody long new_order) {
        driverService.newOrder(id, new_order);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{id}/accept")
    public ResponseEntity<String> acceptOrder(@PathVariable long id, @RequestBody long waiting_order) {
        driverService.acceptOrder(id, waiting_order);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/new-driver")
    public ResponseEntity<String> addDriver(@RequestBody String name, @RequestBody String vehicleType) {
        long id = driverService.nextId();
        driverService.saveDriver(new Driver(id, name, vehicleType, ""));
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{id}/waiting-orders")
    public ResponseEntity<ArrayList<Long>> getWaitingOrders(@PathVariable long id) {
        return ResponseEntity.ok(driverService.getWaitingOrders(id));
    }

}