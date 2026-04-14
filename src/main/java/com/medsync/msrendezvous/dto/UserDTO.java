package com.medsync.msrendezvous.dto;

/**
 * DTO (Data Transfer Object) pour recevoir les données du micro-service User.
 * Cette classe n'est pas persistée en base de données,
 * elle sert uniquement à transférer les données entre les micro-services.
 */
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;

    public UserDTO() {
    }

    public UserDTO(Long id, String nom, String prenom, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
