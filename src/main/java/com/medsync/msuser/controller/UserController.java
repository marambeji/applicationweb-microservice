package com.medsync.msuser.controller;

import com.medsync.msuser.model.User;
import com.medsync.msuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RefreshScope
public class UserController {

    @Value("${welcome.message:Welcome default}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @Autowired
    private UserRepository repository;

    @GetMapping
    public List<User> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public User save(@RequestBody User user) {
        return repository.save(user);
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        if (repository.existsById(id)) {
            user.setId(id);
            return repository.save(user);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @Autowired
    private com.medsync.msuser.service.UserService userService;

    @GetMapping("/favorites")
    public List<com.medsync.msuser.model.JobDTO> getFavoriteJobs() {
        return userService.getFavoriteJobs();
    }

    @GetMapping("/test")
    public String test() {
        return "Micro-service User (H2) est opérationnel !";
    }
}
