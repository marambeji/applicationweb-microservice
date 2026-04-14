package com.medsync.msdonsang.repository;

import com.medsync.msdonsang.model.DonSang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonSangRepository extends JpaRepository<DonSang, Long> {
}
