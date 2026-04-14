package com.medsync.mslocation.controller;

import com.medsync.mslocation.dto.PharmacieDTO;
import com.medsync.mslocation.client.PharmacieClient;
import com.medsync.mslocation.model.Materiel;
import com.medsync.mslocation.repository.MaterielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location-materiel")
@RefreshScope
public class LocationController {

    @Value("${welcome.message:Welcome default}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @Autowired
    private MaterielRepository repository;

    @Autowired
    private PharmacieClient pharmacieClient;

    @GetMapping
    public List<Materiel> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Materiel save(@RequestBody Materiel materiel) {
        return repository.save(materiel);
    }

    @GetMapping("/{id}")
    public Materiel getOne(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Materiel update(@PathVariable Long id, @RequestBody Materiel materiel) {
        if (repository.existsById(id)) {
            materiel.setId(id);
            return repository.save(materiel);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/pharmacies")
    public List<PharmacieDTO> getAllPharmacies() {
        return pharmacieClient.getAllPharmacies();
    }

    @GetMapping("/test")
    public String test() {
        return "Micro-service Location Materiel (MySQL) est opérationnel !";
    }
}
