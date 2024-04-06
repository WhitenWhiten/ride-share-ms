package com.whiten.ordermanagement.controller;

import com.whiten.ordermanagement.entity.Order;
import com.whiten.ordermanagement.entity.OrderStatus;
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

    @PutMapping("/accept-order")
    public ResponseEntity<Boolean> acceptOrder(@RequestParam long orderId) {
        return ResponseEntity.ok(orderService.acceptOrder(orderId));
    }

    @PutMapping("/close-order")
    public ResponseEntity<Boolean> closeOrder(@RequestParam long orderId) {
        return ResponseEntity.ok(orderService.closeOrder(orderId));
    }

    //如果有passengerId参数，则返回相关乘客关联的所有订单，否则返回全部订单
    @GetMapping("/all-orders")
    public ResponseEntity<Order[]> getAllOrders(@RequestParam(required = false) Long passengerId) {
        if (passengerId == null) return ResponseEntity.ok(orderService.getAllOrders());
        else return ResponseEntity.ok(orderService.getRelatedOrders(passengerId));
    }

    @PutMapping("/set-location")
    public ResponseEntity<String> setLocation(@RequestParam long orderId, @RequestParam String newLocation) {
        orderService.setRideLocation(orderId, newLocation);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/get-location")
    public ResponseEntity<String> getLocation(@RequestParam long orderId) {
        return ResponseEntity.ok(orderService.getRideLocation(orderId));
    }

    @GetMapping("/get-order-state")
    public ResponseEntity<OrderStatus> getOrderStatus(@RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getOrderState(orderId));
    }

    @GetMapping("/order-price")
    public ResponseEntity<Double> getOrderPrice(@RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getOrderPrice(orderId));
    }

    @GetMapping("/order-passenger-id")
    public ResponseEntity<Long> getOrderPassengerId(@RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getOrderPassengerId(orderId));
    }
}