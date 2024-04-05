package com.whiten.passengerservice.controller;

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

    @GetMapping("/search")
    public ResponseEntity<String> searchRide() {
        String passengers = passengerService.searchRide();
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/car-types")
    public ResponseEntity<List<String>> getCarTypes() {
        List<String> carTypes = passengerService.getCarTypes();
        return ResponseEntity.ok(carTypes);
    }

    @GetMapping("/track-location/{rideId}")
    public ResponseEntity<String> trackCarLocation(@PathVariable Long rideId) {
        String location = passengerService.trackCarLocation(rideId);
        return ResponseEntity.ok(location);
    }
    @PutMapping("/make-order")
    public ResponseEntity<String> tryMakeOrder(@RequestBody Long driver_id, @RequestBody Long passenger_id) {
        String serviceUrl = "http://localhost:8083/create-order?passengerId={passengerId}&driverId={driverId}";
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("passengerId", passenger_id);
        paramMap.put("driverId", driver_id);
        Long order_id = restTemplate.getForObject(serviceUrl, Long.class, paramMap);
        return ResponseEntity.ok(order_id.toString());
    }

    //用户结束订单
    @PutMapping("/close-order")
    public ResponseEntity<String> closeOrder(@RequestBody Long order_id) {
        RestTemplate restTemplate = new RestTemplate();
        String serviceUrl = "http://localhost:8083/";
        restTemplate.getForObject(serviceUrl+order_id.toString()+"/close", String.class);
        return ResponseEntity.ok("closed");
    }

}