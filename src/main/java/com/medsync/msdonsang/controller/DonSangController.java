package com.medsync.msdonsang.controller;

import com.medsync.msdonsang.model.DonSang;
import com.medsync.msdonsang.repository.DonSangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/don-de-sang")
@RefreshScope
public class DonSangController {

    @Value("${welcome.message:Welcome default}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @Autowired
    private DonSangRepository repository;

    @GetMapping
    public List<DonSang> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public DonSang save(@RequestBody DonSang donSang) {
        return repository.save(donSang);
    }

    @GetMapping("/{id}")
    public DonSang getOne(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public DonSang update(@PathVariable Long id, @RequestBody DonSang donSang) {
        if (repository.existsById(id)) {
            donSang.setId(id);
            return repository.save(donSang);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/test")
    public String test() {
        return "Micro-service Don de Sang (H2) est opérationnel !";
    }
}
