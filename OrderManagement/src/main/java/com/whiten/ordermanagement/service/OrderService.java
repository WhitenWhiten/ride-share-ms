package com.whiten.ordermanagement.service;

import com.whiten.ordermanagement.entity.Order;
import com.whiten.ordermanagement.entity.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService{
    private final HashMap<Long, Order> orderRepository;
    private Long next_id;
    public OrderService() {
        this.orderRepository = new HashMap<>();
        next_id = 1L;
    }

    public Long nextId() {
        Long tmp = next_id;
        next_id += 1;
        return tmp;
    }


    public Long createOrder(Long passengerId, Long driverId, double price) {
        // 实现创建订单的逻辑
        Long oid = nextId();
        Date t = new Date();
        Order order = new Order(oid, passengerId, driverId, price, "",OrderStatus.waiting, t, t);
        this.orderRepository.put(oid, order);
        return oid;
    }

    public boolean acceptOrder(Long oid) {
        if (orderRepository.containsKey(oid)) {
            Order o = orderRepository.get(oid);
            OrderStatus os = o.getStatus();
            if (os.equals(OrderStatus.waiting)){
                o.setStatus(OrderStatus.ongoing);
                o.setUpdateTime(new Date());
                return true;
            } else return false;
        } else {
            return false;
        }
    }

    // 拒绝订单，或者从进行中跳转到关闭态
    public boolean closeOrder(Long oid) {
        if (orderRepository.containsKey(oid)) {
            Order o = orderRepository.get(oid);
            OrderStatus os = o.getStatus();
            if (os.equals(OrderStatus.waiting) || os.equals(OrderStatus.ongoing)){
                o.setStatus(OrderStatus.closed);
                o.setUpdateTime(new Date());
                return true;
            } else return false;
        } else {
            return false;
        }
    }

    public List<Order> getAllOrders() {
        return new ArrayList<Order>(orderRepository.values());
    }

    public void setRideLocation(Long oid, String loc){
        if (orderRepository.containsKey(oid)) {
            Order o = orderRepository.get(oid);
            o.setCurrentLocation(loc);
            o.setUpdateTime(new Date());
        }
    }

    public String getRideLocation(Long oid) {
        if (orderRepository.containsKey(oid)) {
            Order o = orderRepository.get(oid);
            return o.getCurrentLocation();
        } else return "No such Order";
    }




}