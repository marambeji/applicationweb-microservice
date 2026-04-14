package com.medsync.mspharmacie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsPharmacieApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsPharmacieApplication.class, args);
    }
}
