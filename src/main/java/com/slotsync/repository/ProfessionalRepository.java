package com.slotsync.repository;

import com.slotsync.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    List<Professional> findByBusinessId(Long businessId);
    List<Professional> findByBusinessIdAndIsActiveTrue(Long businessId);
    Optional<Professional> findByUserId(Long userId);
    List<Professional> findByBusinessIdAndAcceptsOnlineBookingsTrue(Long businessId);
}
