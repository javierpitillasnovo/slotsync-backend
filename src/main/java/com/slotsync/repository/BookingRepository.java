package com.slotsync.repository;

import com.slotsync.entity.Booking;
import com.slotsync.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Optional<Booking> findByBookingCode(String bookingCode);

    List<Booking> findByBusinessId(Long businessId);

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByProfessionalId(Long professionalId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.business.id = :businessId AND b.status = :status")
    List<Booking> findByBusinessIdAndStatus(@Param("businessId") Long businessId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId " +
           "AND b.startTime BETWEEN :startTime AND :endTime " +
           "AND b.status NOT IN ('CANCELLED', 'NO_SHOW')")
    List<Booking> findProfessionalBookingsInRange(
        @Param("professionalId") Long professionalId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT b FROM Booking b WHERE b.business.id = :businessId " +
           "AND CAST(b.startTime AS DATE) = CURRENT_DATE " +
           "AND b.status NOT IN ('CANCELLED', 'NO_SHOW') " +
           "ORDER BY b.startTime ASC")
    List<Booking> findTodayBookingsByBusiness(@Param("businessId") Long businessId);

    @Query("SELECT b FROM Booking b WHERE b.business.id = :businessId " +
           "AND b.startTime BETWEEN :startDate AND :endDate " +
           "AND b.status NOT IN ('CANCELLED', 'NO_SHOW') " +
           "ORDER BY b.startTime ASC")
    List<Booking> findBookingsByBusinessAndDateRange(
        @Param("businessId") Long businessId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.business.id = :businessId " +
           "AND b.status = 'CONFIRMED' AND b.startTime >= CURRENT_TIMESTAMP")
    long countUpcomingBookingsByBusiness(@Param("businessId") Long businessId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId AND b.status = 'NO_SHOW'")
    long countNoShowsByCustomer(@Param("customerId") Long customerId);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.business.id = :businessId " +
           "AND b.status = 'COMPLETED' AND YEAR(b.startTime) = :year AND MONTH(b.startTime) = :month")
    Double calculateMonthlyRevenue(@Param("businessId") Long businessId, @Param("year") int year, @Param("month") int month);
}
