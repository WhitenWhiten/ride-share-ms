package com.whiten.drivermanagement.service;

import java.util.*;

import com.whiten.drivermanagement.entity.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DriverService{
    private final HashMap<Long, Driver> driverRepository;
    private long current_id;

    public DriverService() {
        driverRepository = new HashMap<Long,Driver>();
        driverRepository.put(1L, new Driver(1,"a", "confortable", "北京"));
        driverRepository.put(2L, new Driver(2,"b", "fast", "武汉"));
        current_id = 3;
    }

    public ArrayList<Driver> getAllDrivers() {
        return new ArrayList<Driver>(driverRepository.values());
    }

    public Driver getDriverById(long id) {
        return driverRepository.getOrDefault(id, null);
    }

    public long nextId(){
        long tmp = current_id;
        current_id += 1;
        return tmp;
    }

    public void saveDriver(Driver driver) {
            driverRepository.put(driver.getId(), driver);
    }

    public void deleteDriver(long id) {
        driverRepository.remove(id);
    }

    public void updateDriverLocation(long id, String newLocation) {
        // 实现更新司机位置信息的逻辑
        if (driverRepository.containsKey(id)) {
            Driver d = driverRepository.get(id);
            d.setCurrentLocation(newLocation);
        }
    }

    public ArrayList<Long> getWaitingOrders(long id) {
        if (driverRepository.containsKey(id)) {
            Driver d = driverRepository.get(id);
            return d.getWaiting_orders();
        }
        return new ArrayList<Long>();
    }

    public void acceptOrder(Long driver_id, Long order_id) {
        if (driverRepository.containsKey(driver_id)) {
            Driver d = driverRepository.get(driver_id);
            if (d.getWaiting_orders().contains(order_id)){
                d.getWaiting_orders().remove(order_id);
                d.set_free(false);
                d.setCurrent_order_id(order_id);
                String serviceUrl = "http://localhost:8083/accept";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForObject("http://localhost:8083/"+order_id.toString()+"/accept",
                        String.class);
            } else {
                //do nothing
            }
        }
    }


    public void finishOrder(long id) {
        if (driverRepository.containsKey(id)){
            Driver d = driverRepository.get(id);
            d.set_free(true);
            d.setCurrent_order_id(-1);
        }
    }

    public void newOrder(long driver_id, long order_id) {
        if (driverRepository.containsKey(driver_id)){
            Driver d = driverRepository.get(driver_id);
            d.set_free(false);
            d.setCurrent_order_id(order_id);
        }
    }

}