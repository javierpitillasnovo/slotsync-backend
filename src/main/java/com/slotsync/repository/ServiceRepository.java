package com.slotsync.repository;

import com.slotsync.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBusinessId(Long businessId);
    List<Service> findByBusinessIdAndIsActiveTrue(Long businessId);
    List<Service> findByCategory(String category);
    List<Service> findByBusinessIdAndCategory(Long businessId, String category);
    List<Service> findByBusinessIdOrderByDisplayOrderAsc(Long businessId);
}
