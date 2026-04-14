package com.medsync.mspharmacie.repository;

import com.medsync.mspharmacie.model.Pharmacie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacieRepository extends JpaRepository<Pharmacie, Long> {
}
