package com.whiten.ordermanagement.controller;

import com.whiten.ordermanagement.entity.Order;
import com.whiten.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<Long> createOrder(@RequestParam Long passengerId, @RequestParam Long driverId, @RequestParam double price) {
        return ResponseEntity.ok(orderService.createOrder(passengerId, driverId, price));
    }

    @PutMapping("/{orderId}/accept")
    public ResponseEntity<Boolean> acceptOrder(@PathVariable long orderId) {
        return ResponseEntity.ok(orderService.acceptOrder(orderId));
    }

    @PutMapping("/{orderId}/close")
    public ResponseEntity<Boolean> closeOrder(@PathVariable long orderId) {
        return ResponseEntity.ok(orderService.closeOrder(orderId));
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{orderId}/set-location")
    public ResponseEntity<String> setLocation(@PathVariable long orderId, @RequestParam String newLocation) {
        orderService.setRideLocation(orderId, newLocation);
        return ResponseEntity.ok("");
    }

    @GetMapping("/{orderId}/get-location")
    public ResponseEntity<String> getLocation(@PathVariable long orderId) {
        return ResponseEntity.ok(orderService.getRideLocation(orderId));
    }


}