package com.whiten.drivermanagement.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Driver {
    private long id;
    private String name;
    private String vehicleType;
    private String currentLocation;
    private boolean is_free;    //仅当空闲时才能被下单
    private long current_order_id;    //忙时有效，记录当前订单id
    private ArrayList<Long> waiting_orders;

    public Driver(long id, String n, String vehicleType, String currentLocation) {
        this.id=id;
        this.name = n;
        this.vehicleType = vehicleType;
        this.currentLocation = currentLocation;
        this.is_free = true;
        this.current_order_id = -1;
        this.waiting_orders = new ArrayList<>();
    }
}