package com.whiten.passengerservice.controller;

import com.whiten.passengerservice.entity.Driver;
import com.whiten.passengerservice.entity.Order;
import com.whiten.passengerservice.entity.Passenger;
import com.whiten.passengerservice.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/")
public class PassengerController {

    private final PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping("/search-location")
    public ResponseEntity<Driver[]> searchRide(@RequestParam String location) {
        Driver[] passengers = passengerService.searchLocation(location);
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/search-type")
    public ResponseEntity<Driver[]> getCarTypes(@RequestParam String type) {
        Driver[] passengers = passengerService.searchCarType(type);
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/track-location")
    public ResponseEntity<String> trackCarLocation(@RequestParam Long rideId) {
        String location = passengerService.trackCarLocation(rideId);
        return ResponseEntity.ok(location);
    }


    @GetMapping("/order-state")
    public ResponseEntity<String> getOrderState(@RequestParam Long orderId) {
        return ResponseEntity.ok(passengerService.getOrderState(orderId));
    }

    @PostMapping("/make-order")
    public ResponseEntity<Long> tryMakeOrder(@RequestParam Long driver_id, @RequestParam Long passenger_id, @RequestParam double price) {
        return ResponseEntity.ok(passengerService.makeOrder(passenger_id, driver_id, price));
    }

    //用户结束订单
    @PutMapping("/close-order")
    public ResponseEntity<String> closeOrder(@RequestParam Long orderId) {
        boolean closed = passengerService.closeOrder(orderId);
        if(closed)  return ResponseEntity.ok("closed");
        else return ResponseEntity.ok("Not closed");
    }

    //返回最终余额
    @PutMapping("/add-balance")
    public ResponseEntity<Double> addBalance(@RequestParam Long passengerId, @RequestParam Double val) {
        return ResponseEntity.ok(passengerService.addBalance(passengerId, val));
    }

    @GetMapping("/view-passenger")
    public ResponseEntity<Passenger> viewPassenger(@RequestParam Long passengerId) {
        return ResponseEntity.ok(passengerService.viewPassenger(passengerId));
    }

    @GetMapping("/passenger-orders")
    public ResponseEntity<Order[]> passengerOrders(@RequestParam Long passengerId) {
        return ResponseEntity.ok(passengerService.passengerOrders(passengerId));
    }

    @GetMapping("/all-passengers")
    public ResponseEntity<Passenger[]> allPassengers() {
        return ResponseEntity.ok(passengerService.allPassengers());
    }

}