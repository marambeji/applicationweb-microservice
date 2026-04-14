package com.medsync.msrendezvous.client;

import com.medsync.msrendezvous.dto.DonSangDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Client Feign pour communiquer avec le micro-service Don de Sang.
 * 
 * - name = "ms-don-de-sang" : nom du service enregistré dans Eureka.
 */
@FeignClient(name = "ms-don-de-sang")
public interface DonSangClient {

    @GetMapping("/api/don-de-sang")
    List<DonSangDTO> getAllDons();

    @GetMapping("/api/don-de-sang/{id}")
    DonSangDTO getDonById(@PathVariable("id") Long id);
}
