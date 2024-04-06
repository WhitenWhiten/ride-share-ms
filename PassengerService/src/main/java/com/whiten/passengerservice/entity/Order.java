package com.whiten.passengerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Order {

    private Long id;  //订单id
    private Long passengerId; //乘客id
    private Long driverId;    //司机id
    private double price;   //订单价格
    private String currentLocation; //当前位置
    private OrderStatus status; // "待接单", "进行中", "已完成"
    private Date createTime;    //订单创建时间
    private Date updateTime;    //订单上一次更新时间
}