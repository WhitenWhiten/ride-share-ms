package com.whiten.drivermanagement.controller;

import com.whiten.drivermanagement.entity.Driver;
import com.whiten.drivermanagement.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Driver> getAllDrivers() {
        return (List<Driver>) driverService.getAllDrivers();
    }

    @GetMapping("/free-drivers")
    public List<Driver> freeDrivers() {
        List<Driver> ret = new ArrayList<>();
        ArrayList<Driver> all =  driverService.getAllDrivers();
        for (Driver d : all) {
            if (d.is_free())
                ret.add(d);
        }
        return ret;
    }


    @GetMapping("/{id}")
    public Driver getDriverById(@PathVariable long id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}/location")
    public void updateDriverLocation(@PathVariable long id, @RequestBody String newLocation) {
        driverService.updateDriverLocation(id, newLocation);
    }

    @PutMapping("/{id}/finish-order")
    public void finishOrder(@PathVariable long id) {
        driverService.finishOrder(id);
    }

    @PutMapping("/{id}/new-order")
    public void finishOrder(@PathVariable long id, @RequestBody long new_order) {
        driverService.newOrder(id, new_order);
    }

    @GetMapping("/{id}/accept")
    public void acceptOrder(@PathVariable long id, @RequestBody long waiting_order) {
        driverService.acceptOrder(id, waiting_order);
    }

    @PutMapping("/new-driver")
    public void addDriver(@RequestBody String name, @RequestBody String vehicleType) {
        long id = driverService.nextId();
        driverService.saveDriver(new Driver(id, name, vehicleType, ""));
    }

    @GetMapping("/{id}/waiting-orders")
    public ArrayList<Long> getWaitingOrders(@PathVariable long id) {
        return driverService.getWaitingOrders(id);
    }

}