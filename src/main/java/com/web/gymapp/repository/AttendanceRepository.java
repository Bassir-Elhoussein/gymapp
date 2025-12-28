package com.web.gymapp.repository;

import com.web.gymapp.model.Attendance;
import com.web.gymapp.model.enums.AccessResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find all attendance records for a specific client
    List<Attendance> findByClientId(Long clientId);

    // Find attendance by specific date
    List<Attendance> findByDate(LocalDate date);

    // Find attendance within a date range
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Find attendance by access result (GRANTED, DENIED, etc.)
    List<Attendance> findByAccessResult(AccessResult accessResult);

    // Find client's attendance within date range
    @Query("SELECT a FROM Attendance a WHERE a.client.id = :clientId AND a.date BETWEEN :startDate AND :endDate ORDER BY a.checkInTime DESC")
    List<Attendance> findByClientIdAndDateBetween(
            @Param("clientId") Long clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Count successful check-ins for a client
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.client.id = :clientId AND a.accessResult = 'GRANTED'")
    Long countSuccessfulAttendancesByClientId(@Param("clientId") Long clientId);

    // Get today's attendance records
    @Query("SELECT a FROM Attendance a WHERE a.date = :today ORDER BY a.checkInTime DESC")
    List<Attendance> findTodaysAttendance(@Param("today") LocalDate today);

    // Check if client already checked in today
    @Query("SELECT COUNT(a) > 0 FROM Attendance a WHERE a.client.id = :clientId AND a.date = :today AND a.accessResult = 'GRANTED'")
    boolean hasClientCheckedInToday(@Param("clientId") Long clientId, @Param("today") LocalDate today);

    // Find all denied access attempts
    @Query("SELECT a FROM Attendance a WHERE a.accessResult != 'GRANTED' ORDER BY a.checkInTime DESC")
    List<Attendance> findAllDeniedAccess();

    // Find denied access by date range
    @Query("SELECT a FROM Attendance a WHERE a.accessResult != 'GRANTED' AND a.date BETWEEN :startDate AND :endDate ORDER BY a.checkInTime DESC")
    List<Attendance> findDeniedAccessByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Count total check-ins for today
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.date = :today AND a.accessResult = 'GRANTED'")
    Long countTodaysCheckIns(@Param("today") LocalDate today);

    // Get attendance by subscription
    List<Attendance> findBySubscriptionId(Long subscriptionId);

    // Find by fingerprint ID (useful for debugging fingerprint system)
    List<Attendance> findByFingerprintId(String fingerprintId);
}