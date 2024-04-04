package com.whiten.drivermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DriverManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverManagementApplication.class, args);
    }

}
