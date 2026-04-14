package com.medsync.mslocation.client;

import com.medsync.mslocation.dto.PharmacieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-pharmacie")
public interface PharmacieClient {

    @GetMapping("/api/pharmacie")
    List<PharmacieDTO> getAllPharmacies();

    @GetMapping("/api/pharmacie/{id}")
    PharmacieDTO getPharmacieById(@PathVariable("id") Long id);
}
