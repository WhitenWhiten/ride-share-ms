package com.whiten.drivermanagement.service;

import java.util.*;

import com.whiten.drivermanagement.entity.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DriverService {
    private final HashMap<Long, Driver> driverRepository;
    private long current_id;

    public DriverService() {
        driverRepository = new HashMap<Long, Driver>();
        driverRepository.put(1L, new Driver(1, "a", "confortable", "北京"));
        driverRepository.put(2L, new Driver(2, "b", "fast", "武汉"));
        current_id = 3;
    }

    public Driver[] getAllDrivers() {
        Collection<Driver> drivers = driverRepository.values();
        return (Driver[]) drivers.toArray(new Driver[drivers.size()]);
    }

    public Driver[] selectDrivers(String location, String type) {
        Driver[] drivers = getAllDrivers();
        if (location == null && type == null) { // 无要求则返回全体drivers
            return drivers;
        } else {
            ArrayList<Driver> ret = new ArrayList<>();
            if (location != null) { //如果location不为空，则根据location筛选
                for (Driver d : drivers) {
                    if (d.getCurrentLocation().equals(location)) ret.add(d);
                }
            }
            if (type != null) {
                for (Driver d : drivers) {
                    if (d.getVehicleType().equals(type))
                        ret.add(d);
                }
            }
            return (Driver[]) ret.toArray(new Driver[ret.size()]);

        }

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

    public void addWaitingOrder(long driver_id, long order_id) {
        if (driverRepository.containsKey(driver_id)) {
            Driver d = driverRepository.get(driver_id);
            d.getWaiting_orders().add(order_id);
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
                String serviceUrl = "http://localhost:8080/os/accept-order?orderId={orderId}";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.put(serviceUrl, null, order_id);
            }
        }
    }


    public void finishOrder(Long id, Double profit) {
        if (driverRepository.containsKey(id)){
            Driver d = driverRepository.get(id);
            d.set_free(true);
            d.setCurrent_order_id(-1);
            d.setBalance(d.getBalance() + profit);
        }
    }

    public void newOrder(long driver_id, long order_id) {
        if (driverRepository.containsKey(driver_id)){
            Driver d = driverRepository.get(driver_id);
            d.set_free(false);
            d.setCurrent_order_id(order_id);
        }
    }


    public Double getDriverBalance(long id) {
        if (driverRepository.containsKey(id)){
            Driver d = driverRepository.get(id);
            return d.getBalance();
        } else return (double) 0;
    }

}