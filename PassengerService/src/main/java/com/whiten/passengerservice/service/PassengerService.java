package com.whiten.passengerservice.service;

import com.whiten.passengerservice.entity.Order;
import com.whiten.passengerservice.entity.OrderStatus;
import com.whiten.passengerservice.entity.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.whiten.passengerservice.entity.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class PassengerService {
    private final HashMap<Long, Passenger> passengerRepository;
    private Long next_id;
    public PassengerService() {
        passengerRepository = new HashMap<>();
        passengerRepository.put(1L,new Passenger(1L,"汪","abcxyz","1322xxx6613","1368846568@qq.com", 0));
        passengerRepository.put(2L,new Passenger(2L,"飞","IOPQWQ","13xxx6613","1368846568@outlook.com", 0));
        next_id=3L;
    }

    public Long nextId(){
        long tmp = next_id;
        next_id+=1;
        return tmp;
    }

    public ArrayList<Passenger> getAllPassengers() {
        return new ArrayList<Passenger>(passengerRepository.values());
    }

    public Passenger getPassengerById(long id) {
        return passengerRepository.getOrDefault(id, null);
    }

    public Passenger createPassenger(Passenger passenger) {
        // 忽略外界传来的id，在内部重新赋予id
        long id = nextId();
        passenger.setId(id);
        return passengerRepository.put(id, passenger);
    }

    public void updatePassenger(Passenger passenger) {
        long id = passenger.getId();
        passengerRepository.put(id, passenger);
    }

    public void deletePassenger(Long id) {
        passengerRepository.remove(id);
    }

    public Driver[] searchLocation(String location) {
        RestTemplate restTemplate = new RestTemplate();
        String serviceUrl = "http://localhost:8080/ds/drivers?location={l}";
        try {
            return restTemplate.getForObject(serviceUrl, Driver[].class, location);
        } catch (Exception ignored) {
            return new Driver[0];
        }
    }

    public Driver[] searchCarType(String type) {
        RestTemplate restTemplate = new RestTemplate();
        String serviceUrl = "http://localhost:8080/ds/drivers?type={t}";
        try {
            return restTemplate.getForObject(serviceUrl, Driver[].class, type);
        } catch (Exception ignored) {
            return new Driver[0];
        }
    }

    public String trackCarLocation(Long OrderId) {

        RestTemplate restTemplate = new RestTemplate();

        // 定义目标服务的URL
        String serviceUrl = "http://localhost:8080/os/get-location?orderId={orderId}";
        return restTemplate.getForObject(serviceUrl, String.class, OrderId);
    }

    public String getOrderState(Long oid) {
        RestTemplate restTemplate = new RestTemplate();
        String serviceUrl = "http://localhost:8080/os/get-order-state?orderId={orderId}";
        OrderStatus os = restTemplate.getForObject(serviceUrl, OrderStatus.class, oid);
        if (os != null) {
            return switch (os) {
                case waiting -> "等待接单中";
                case ongoing -> "订单进行中";
                case closed -> "订单已关闭";
                case non_exist -> "不存在该订单";
                default -> "";
            };
        } else {
            return "";
        }
    }

    public Long makeOrder(Long passenger_id, Long driver_id, double price) {
        String serviceUrl = "http://localhost:8080/os/create-order?passengerId={passengerId}&driverId={driverId}&price={price}";
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("passengerId", passenger_id);
        paramMap.put("driverId", driver_id);
        paramMap.put("price", price);
        return restTemplate.postForObject(serviceUrl,null,  Long.class, paramMap);
    }


    public boolean closeOrder(Long orderId) {
        RestTemplate restTemplate = new RestTemplate();
        String serviceUrl1 = "http://localhost:8080/os/order-price?orderId={orderId}";  // 获取订单所需金额
        String serviceUrl2 = "http://localhost:8080/os/order-passenger-id?orderId={orderId}";//用户id
        String serviceUrl3 = "http://localhost:8080/os/close-order?orderId={orderId}";
        Double price = restTemplate.getForObject(serviceUrl1, Double.class, orderId);
        Long userId = restTemplate.getForObject(serviceUrl2, Long.class, orderId);
        if (passengerRepository.containsKey(userId)) {
            Passenger user = passengerRepository.get(userId);
            if (user.getBalance() >= price) {    //余额足够，允许关闭
                user.setBalance(user.getBalance() - price);
                restTemplate.put(serviceUrl3, null, orderId);
                return true;
            }

        }
        return false;
    }

    public Double addBalance(Long passenger_id, Double value) {
        if (passengerRepository.containsKey(passenger_id)) {
            Passenger p = passengerRepository.get(passenger_id);
            p.setBalance(p.getBalance() + value);
            return p.getBalance();
        } else {
            return -1.0;
        }
    }

    public Passenger viewPassenger(Long passenger_id) {
        if (passengerRepository.containsKey(passenger_id)) {
            Passenger p = passengerRepository.get(passenger_id);
            return p;
        } else {
            return new Passenger();
        }
    }

    public Order[] passengerOrders(long pid) {
        String serviceUrl = "http://127.0.0.1:8080/os/all-orders?passengerId={passengerId}";
        return (new RestTemplate()).getForObject(serviceUrl, Order[].class, pid);
    }

    public Passenger[] allPassengers() {
        Collection<Passenger> ps = passengerRepository.values();
        return ps.toArray(new Passenger[ps.size()]);
    }


}