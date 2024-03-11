package com.java.employment_system.repository;

import com.java.employment_system.entity.Compensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompensationRepo extends JpaRepository<Compensation,Long> {
}
