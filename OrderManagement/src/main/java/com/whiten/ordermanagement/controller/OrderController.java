package com.whiten.ordermanagement.controller;

import com.whiten.ordermanagement.entity.Order;
import com.whiten.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Long createOrder(@RequestParam Long passengerId, @RequestParam Long driverId) {
        return orderService.createOrder(passengerId, driverId);
    }

    @PutMapping("/{orderId}/accept")
    public boolean acceptOrder(@PathVariable long orderId) {
        return orderService.acceptOrder(orderId);
    }

    @PutMapping("/{orderId}/close")
    public boolean closeOrder(@PathVariable long orderId) {
        return orderService.closeOrder(orderId);
    }

    @GetMapping("/all-orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}