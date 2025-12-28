package com.web.gymapp.controller;

import com.web.gymapp.model.Attendance;
import com.web.gymapp.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * Record a check-in (for fingerprint machine or manual entry)
     * POST /api/attendance/check-in?clientId=1&fingerprintId=FP123
     */
    @PostMapping("/check-in")
    public ResponseEntity<Attendance> recordCheckIn(
            @RequestParam Long clientId,
            @RequestParam(required = false) String fingerprintId) {
        Attendance attendance = attendanceService.recordCheckIn(clientId, fingerprintId);
        return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
    }

    /**
     * Get attendance by ID
     * GET /api/attendance/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        return attendanceService.getAttendanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all attendance records
     * GET /api/attendance
     */
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    /**
     * Get all attendance for a specific client
     * GET /api/attendance/client/1
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Attendance>> getClientAttendance(@PathVariable Long clientId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByClientId(clientId));
    }

    /**
     * Get attendance for a specific date
     * GET /api/attendance/date/2024-12-28
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
    }

    /**
     * Get today's attendance
     * GET /api/attendance/today
     */
    @GetMapping("/today")
    public ResponseEntity<List<Attendance>> getTodaysAttendance() {
        return ResponseEntity.ok(attendanceService.getTodaysAttendance());
    }

    /**
     * Get attendance within date range
     * GET /api/attendance/date-range?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Attendance>> getAttendanceByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDateRange(startDate, endDate));
    }

    /**
     * Get client's attendance within date range
     * GET /api/attendance/client/1/date-range?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/client/{clientId}/date-range")
    public ResponseEntity<List<Attendance>> getClientAttendanceByDateRange(
            @PathVariable Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getClientAttendanceByDateRange(clientId, startDate, endDate));
    }

    /**
     * Count client's total successful check-ins
     * GET /api/attendance/client/1/count
     */
    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<Long> getClientAttendanceCount(@PathVariable Long clientId) {
        Long count = attendanceService.countClientAttendance(clientId);
        return ResponseEntity.ok(count);
    }

    /**
     * Check if client has checked in today
     * GET /api/attendance/client/1/checked-in-today
     */
    @GetMapping("/client/{clientId}/checked-in-today")
    public ResponseEntity<Boolean> hasCheckedInToday(@PathVariable Long clientId) {
        boolean checkedIn = attendanceService.hasClientCheckedInToday(clientId);
        return ResponseEntity.ok(checkedIn);
    }

    /**
     * Get all denied access attempts
     * GET /api/attendance/denied
     */
    @GetMapping("/denied")
    public ResponseEntity<List<Attendance>> getAllDeniedAccess() {
        return ResponseEntity.ok(attendanceService.getAllDeniedAccess());
    }

    /**
     * Get denied access attempts within date range
     * GET /api/attendance/denied/date-range?startDate=2024-01-01&endDate=2024-12-31
     */
    @GetMapping("/denied/date-range")
    public ResponseEntity<List<Attendance>> getDeniedAccessByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getDeniedAccessByDateRange(startDate, endDate));
    }

    /**
     * Count today's successful check-ins
     * GET /api/attendance/today/count
     */
    @GetMapping("/today/count")
    public ResponseEntity<Long> countTodaysCheckIns() {
        Long count = attendanceService.countTodaysCheckIns();
        return ResponseEntity.ok(count);
    }

    /**
     * Get attendance by subscription
     * GET /api/attendance/subscription/1
     */
    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<Attendance>> getAttendanceBySubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(attendanceService.getAttendanceBySubscriptionId(subscriptionId));
    }

    /**
     * Get attendance by fingerprint ID (for debugging)
     * GET /api/attendance/fingerprint/FP123
     */
    @GetMapping("/fingerprint/{fingerprintId}")
    public ResponseEntity<List<Attendance>> getAttendanceByFingerprint(@PathVariable String fingerprintId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByFingerprintId(fingerprintId));
    }

    /**
     * Get attendance statistics
     * GET /api/attendance/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAttendanceStats() {
        Long todaysCount = attendanceService.countTodaysCheckIns();
        List<Attendance> todaysAttendance = attendanceService.getTodaysAttendance();
        List<Attendance> deniedToday = attendanceService.getDeniedAccessByDateRange(LocalDate.now(), LocalDate.now());

        Map<String, Object> stats = Map.of(
                "todaysTotalCheckIns", todaysCount,
                "todaysSuccessful", todaysAttendance.stream().filter(Attendance::wasAccessGranted).count(),
                "todaysDenied", deniedToday.size(),
                "currentlyInGym", todaysCount
        );

        return ResponseEntity.ok(stats);
    }
}