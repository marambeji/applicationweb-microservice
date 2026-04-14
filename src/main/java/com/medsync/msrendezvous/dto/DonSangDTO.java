package com.medsync.msrendezvous.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonSangDTO {
    private Long id;
    private String donneurNom;
    private String groupeSanguin;
    private Long quantiteMl;
    private String dateDon;
}
