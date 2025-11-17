package com.slotsync.repository;

import com.slotsync.entity.Business;
import com.slotsync.enums.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long>, JpaSpecificationExecutor<Business> {

    Optional<Business> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Business> findByOwnerId(Long ownerId);

    List<Business> findBySubscriptionPlan(SubscriptionPlan plan);

    Optional<Business> findByStripeCustomerId(String stripeCustomerId);

    List<Business> findByIsActiveTrue();

    long countBySubscriptionPlan(SubscriptionPlan plan);
}
