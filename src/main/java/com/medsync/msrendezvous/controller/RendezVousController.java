package com.medsync.msrendezvous.controller;

import com.medsync.msrendezvous.dto.UserDTO;
import com.medsync.msrendezvous.dto.DonSangDTO;
import com.medsync.msrendezvous.model.RendezVous;
import com.medsync.msrendezvous.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendez-vous")
@RefreshScope
public class RendezVousController {

    @Value("${welcome.message:Welcome default}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

    @Autowired
    private RendezVousService rendezVousService;

    @GetMapping
    public List<RendezVous> getAll() {
        return rendezVousService.getAllRendezVous();
    }

    @PostMapping
    public RendezVous save(@RequestBody RendezVous rendezVous) {
        return rendezVousService.saveRendezVous(rendezVous);
    }

    @GetMapping("/{id}")
    public RendezVous getOne(@PathVariable Long id) {
        return rendezVousService.getRendezVousById(id);
    }

    @PutMapping("/{id}")
    public RendezVous update(@PathVariable Long id, @RequestBody RendezVous rendezVous) {
        rendezVous.setId(id);
        return rendezVousService.saveRendezVous(rendezVous);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
    }

    // =============================================
    // Endpoints utilisant OpenFeign pour appeler ms-user
    // (Scénario 1 : récupérer les users)
    // =============================================

    /**
     * Récupère la liste de tous les utilisateurs depuis le micro-service User
     * via OpenFeign (communication synchrone).
     */
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return rendezVousService.getAllUsers();
    }

    /**
     * Récupère un utilisateur par son ID depuis le micro-service User
     * via OpenFeign (communication synchrone).
     */
    @GetMapping("/users/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return rendezVousService.getUserById(id);
    }

    /**
     * Récupère la liste des dons de sang depuis le micro-service Don-de-sang
     * via OpenFeign.
     */
    @GetMapping("/dons-de-sang")
    public List<DonSangDTO> getAllDonsDeSang() {
        return rendezVousService.getAllDonsDeSang();
    }

    // =============================================
    // Scénario 2 : Gestion des utilisateurs favoris
    // =============================================

    /**
     * Récupère la liste des utilisateurs favoris d'un rendez-vous.
     * Chaque ID favori est résolu via OpenFeign pour obtenir les détails complets.
     */
    @GetMapping("/{id}/favorite-users")
    public List<UserDTO> getFavoriteUsers(@PathVariable Long id) {
        return rendezVousService.getFavoriteUsers(id);
    }

    /**
     * Ajoute un utilisateur aux favoris d'un rendez-vous.
     * Vérifie d'abord que l'utilisateur existe via OpenFeign avant de l'ajouter.
     */
    @PostMapping("/{id}/favorite-users/{userId}")
    public ResponseEntity<String> saveFavoriteUser(@PathVariable Long id, @PathVariable Long userId) {
        UserDTO user = rendezVousService.getUserById(userId);
        if (user != null) {
            rendezVousService.saveFavoriteUser(id, userId);
            return ResponseEntity.status(HttpStatus.OK).body("User saved as favorite successfully.");
        } else {
            // Gérer le cas où le user n'existe pas
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with ID: " + userId);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Micro-service Rendez-vous est opérationnel avec OpenFeign !";
    }
}
