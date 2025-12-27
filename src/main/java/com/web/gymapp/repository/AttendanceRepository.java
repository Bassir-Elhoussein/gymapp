package com.web.gymapp.repository;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */
import com.web.gymapp.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}