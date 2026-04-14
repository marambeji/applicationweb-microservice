package com.medsync.msrendezvous.service;

import com.medsync.msrendezvous.client.UserClient;
import com.medsync.msrendezvous.client.DonSangClient;
import com.medsync.msrendezvous.dto.UserDTO;
import com.medsync.msrendezvous.dto.DonSangDTO;
import com.medsync.msrendezvous.model.RendezVous;
import com.medsync.msrendezvous.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service du micro-service Rendez-vous.
 * Utilise OpenFeign (via UserClient) pour appeler le micro-service User
 * et récupérer les informations des utilisateurs de manière synchrone.
 */
@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private DonSangClient donSangClient;

    // =============================================
    // Opérations CRUD sur les rendez-vous
    // =============================================

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public RendezVous getRendezVousById(Long id) {
        return rendezVousRepository.findById(id).orElse(null);
    }

    public RendezVous saveRendezVous(RendezVous rendezVous) {
        return rendezVousRepository.save(rendezVous);
    }

    public void deleteRendezVous(Long id) {
        rendezVousRepository.deleteById(id);
    }

    // =============================================
    // Communication avec le micro-service User via OpenFeign
    // =============================================

    /**
     * Récupère la liste de tous les utilisateurs depuis le micro-service User.
     * L'appel est effectué de manière synchrone via OpenFeign.
     */
    public List<UserDTO> getAllUsers() {
        return userClient.getAllUsers();
    }

    /**
     * Récupère un utilisateur par son ID depuis le micro-service User.
     * L'appel est effectué de manière synchrone via OpenFeign.
     */
    public UserDTO getUserById(Long id) {
        return userClient.getUserById(id);
    }

    // =============================================
    // Communication avec ms-don-de-sang via OpenFeign
    // =============================================

    public List<DonSangDTO> getAllDonsDeSang() {
        return donSangClient.getAllDons();
    }

    // =============================================
    // Scénario 2 : Gestion des utilisateurs favoris
    // =============================================

    /**
     * Récupère la liste des utilisateurs favoris d'un rendez-vous.
     * Pour chaque ID stocké dans favoriteUsers, on appelle le micro-service User
     * via OpenFeign pour obtenir les détails complets de l'utilisateur.
     */
    public List<UserDTO> getFavoriteUsers(Long rendezVousId) {
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).get();
        return rendezVous.getFavoriteUsers().stream()
                .map(userClient::getUserById)
                .collect(Collectors.toList());
    }

    /**
     * Ajoute un utilisateur aux favoris d'un rendez-vous.
     * L'ID de l'utilisateur est stocké dans la collection @ElementCollection.
     */
    public void saveFavoriteUser(Long rendezVousId, Long userId) {
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).get();
        rendezVous.getFavoriteUsers().add(userId);
        rendezVousRepository.save(rendezVous);
    }
}
