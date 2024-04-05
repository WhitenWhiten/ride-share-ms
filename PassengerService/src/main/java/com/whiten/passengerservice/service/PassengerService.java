package com.whiten.passengerservice.service;

import com.whiten.passengerservice.entity.Passenger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.sql.Driver;
import java.util.ArrayList;
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

    public String searchRide() {
        // 向Drivers微服务请求所有状态为空闲的司机
        RestTemplate restTemplate = new RestTemplate();

        // 定义目标服务的URL
        String serviceUrl = "http://localhost:8081/free-drivers";
        ArrayList<Long> free_id = new ArrayList<>();
        try {
            // 发起GET请求并获取响应
            return restTemplate.getForObject(serviceUrl, String.class);
        } catch (Exception ignored) {}
        return null;
    }

    public List<String> getCarTypes() {
        // 实现选择汽车类型的逻辑
        // 返回一个固定的汽车类型列表
        return List.of("经济型", "舒适型", "豪华型");
    }

    public String trackCarLocation(Long OrderId) {

        RestTemplate restTemplate = new RestTemplate();

        // 定义目标服务的URL
        String serviceUrl = "http://localhost:8083/" + OrderId.toString() + "/get-location";
        return restTemplate.getForObject(serviceUrl, String.class);
    }

    //用户下单尝试

}