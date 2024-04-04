package com.whiten.passengerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private double balance;
}