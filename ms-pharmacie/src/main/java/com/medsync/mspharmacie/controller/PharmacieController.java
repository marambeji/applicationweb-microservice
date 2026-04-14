package com.medsync.mspharmacie.controller;

import com.medsync.mspharmacie.model.Pharmacie;
import com.medsync.mspharmacie.repository.PharmacieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacie")
@RefreshScope
public class PharmacieController {

    @Value("${welcome.message:Welcome default}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @Autowired
    private PharmacieRepository repository;

    @GetMapping
    public List<Pharmacie> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Pharmacie save(@RequestBody Pharmacie pharmacie) {
        return repository.save(pharmacie);
    }

    @GetMapping("/{id}")
    public Pharmacie getOne(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Pharmacie update(@PathVariable Long id, @RequestBody Pharmacie pharmacie) {
        if (repository.existsById(id)) {
            pharmacie.setId(id);
            return repository.save(pharmacie);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/test")
    public String test() {
        return "Micro-service Pharmacie (MySQL) est opérationnel !";
    }
}
