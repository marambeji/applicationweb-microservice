package com.medsync.msrendezvous.client;

import com.medsync.msrendezvous.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Client Feign pour communiquer avec le micro-service User.
 * 
 * - name = "ms-user" : nom du service tel qu'il est enregistré dans Eureka.
 * Feign découvre automatiquement l'URL via Eureka grâce à ce nom.
 * - Les méthodes déclarent les endpoints REST du micro-service User
 * que le micro-service Rendez-vous souhaite appeler.
 */
@FeignClient(name = "ms-user")
public interface UserClient {

    @GetMapping("/api/users")
    List<UserDTO> getAllUsers();

    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
