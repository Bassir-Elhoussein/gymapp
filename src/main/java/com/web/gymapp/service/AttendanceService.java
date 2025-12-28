package com.web.gymapp.service;

import com.web.gymapp.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    Attendance recordCheckIn(Long clientId, String fingerprintId);

    Optional<Attendance> getAttendanceById(Long id);

    List<Attendance> getAllAttendance();

    List<Attendance> getAttendanceByClientId(Long clientId);

    List<Attendance> getAttendanceByDate(LocalDate date);

    List<Attendance> getTodaysAttendance();

    List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate);

    List<Attendance> getClientAttendanceByDateRange(Long clientId, LocalDate startDate, LocalDate endDate);

    Long countClientAttendance(Long clientId);

    boolean hasClientCheckedInToday(Long clientId);

    List<Attendance> getAllDeniedAccess();

    List<Attendance> getDeniedAccessByDateRange(LocalDate startDate, LocalDate endDate);

    Long countTodaysCheckIns();

    List<Attendance> getAttendanceBySubscriptionId(Long subscriptionId);

    List<Attendance> getAttendanceByFingerprintId(String fingerprintId);
}