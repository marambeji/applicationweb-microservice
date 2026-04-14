package com.medsync.mslocation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacieDTO {
    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private String telephone;
}
