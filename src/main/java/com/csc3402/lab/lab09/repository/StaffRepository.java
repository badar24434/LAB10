package com.csc3402.lab.lab09.repository;

import com.csc3402.lab.lab09.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Additional custom query methods can be added here if needed
    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);
}

