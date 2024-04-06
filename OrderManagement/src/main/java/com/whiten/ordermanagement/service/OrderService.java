package com.whiten.ordermanagement.service;

import com.whiten.ordermanagement.entity.Order;
import com.whiten.ordermanagement.entity.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
        //通知Driver系统添加待接单的订单
        String serviceUrl = "http://127.0.0.1:8080/ds/add-waiting-order?driverId={driverId}&orderId={orderId}";
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("driverId", driverId);
        paramMap.put("orderId", oid);
        restTemplate.put(serviceUrl, null,paramMap);
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
                o.setUpdateTime(new Date());
                String serviceUrl = "http://127.0.0.1:8080/ds/close-order?driverId={driverId}&profit={profit}"; //通知Driver系统，让对应司机知道订单已顺利关闭
                RestTemplate restTemplate = new RestTemplate();
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("driverId", orderRepository.get(oid).getDriverId());
                double profit = 0;
                if (os.equals(OrderStatus.ongoing)) {
                    profit = orderRepository.get(oid).getPrice();
                }
                paramMap.put("profit", profit);
                restTemplate.put(serviceUrl, null, paramMap);
                o.setStatus(OrderStatus.closed);
                return true;
            } else return false;
        } else {
            return false;
        }
    }

    public Order[] getAllOrders() {
        Collection<Order> ret = orderRepository.values();
        return (Order[]) ret.toArray(new Order[ret.size()]);
    }

    public Order[] getRelatedOrders(long passengerId){
        Order[] all = getAllOrders();
        ArrayList<Order> ret = new ArrayList<>();
        for(Order o : all) {
            if (o.getPassengerId() == passengerId)
                ret.add(o);
        }
        return (Order[]) ret.toArray(new Order[ret.size()]);
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

    public OrderStatus getOrderState(Long oid) {
        if (orderRepository.containsKey(oid)) {
            return orderRepository.get(oid).getStatus();
        } else {
            return OrderStatus.non_exist;
        }
    }

    public double getOrderPrice(Long oid) {
        if (orderRepository.containsKey(oid)) {
            return orderRepository.get(oid).getPrice();
        } else {
            return Double.MAX_VALUE;
        }
    }

    public Long getOrderPassengerId(Long oid) {
        if (orderRepository.containsKey(oid)) {
            return orderRepository.get(oid).getPassengerId();
        } else {
            return -1L;
        }
    }




}