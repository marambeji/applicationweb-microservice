package com.medsync.msrendezvous.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientNom;
    private String medecinNom;
    private String specialite;
    private String dateHeure;
    private String statut; // 'PREVU', 'ANNULE', 'TERMINE'

    // @ElementCollection : indique à JPA que la collection favoriteUsers
    // doit être persistée dans une table séparée, associée à l'entité RendezVous.
    // Set<Long> : collection d'IDs des utilisateurs favoris.
    @ElementCollection
    private Set<Long> favoriteUsers = new HashSet<>();
}
