package com.web.gymapp.service.impl;

import com.web.gymapp.model.Attendance;
import com.web.gymapp.model.Client;
import com.web.gymapp.model.Subscription;
import com.web.gymapp.model.enums.AccessResult;
import com.web.gymapp.model.enums.SubscriptionStatus;
import com.web.gymapp.repository.AttendanceRepository;
import com.web.gymapp.repository.ClientRepository;
import com.web.gymapp.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public Attendance recordCheckIn(Long clientId, String fingerprintId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        Attendance attendance = Attendance.builder()
                .client(client)
                .fingerprintId(fingerprintId)
                .build();

        // Check if client can access gym
        if (client.canAccessGym()) {
            Subscription activeSubscription = client.getCurrentSubscription();
            attendance.setSubscription(activeSubscription);
            attendance.setAccessResult(AccessResult.GRANTED);
            attendance.setDenialReason(null);
        } else {
            // Determine why access was denied
            Subscription currentSub = client.getCurrentSubscription();

            if (currentSub == null) {
                attendance.setAccessResult(AccessResult.DENIED_NO_SUBSCRIPTION);
                attendance.setDenialReason("No active subscription found");
            } else if (currentSub.isExpired()) {
                attendance.setSubscription(currentSub);
                attendance.setAccessResult(AccessResult.DENIED_EXPIRED);
                attendance.setDenialReason("Subscription expired on " + currentSub.getEndDate());
            } else if (currentSub.getAmountPaid() <= 0) {
                attendance.setSubscription(currentSub);
                attendance.setAccessResult(AccessResult.DENIED_UNPAID);
                attendance.setDenialReason("No payment made for subscription");
            } else if (currentSub.getStatus() == SubscriptionStatus.SUSPENDED) {
                attendance.setSubscription(currentSub);
                attendance.setAccessResult(AccessResult.DENIED_SUSPENDED);
                attendance.setDenialReason("Subscription is suspended by admin");
            } else {
                attendance.setSubscription(currentSub);
                attendance.setAccessResult(AccessResult.DENIED_FINGERPRINT_ERROR);
                attendance.setDenialReason("Unknown error occurred");
            }
        }

        return attendanceRepository.save(attendance);
    }

    @Override
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    public List<Attendance> getAttendanceByClientId(Long clientId) {
        return attendanceRepository.findByClientId(clientId);
    }

    @Override
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    @Override
    public List<Attendance> getTodaysAttendance() {
        return attendanceRepository.findTodaysAttendance(LocalDate.now());
    }

    @Override
    public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public List<Attendance> getClientAttendanceByDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByClientIdAndDateBetween(clientId, startDate, endDate);
    }

    @Override
    public Long countClientAttendance(Long clientId) {
        return attendanceRepository.countSuccessfulAttendancesByClientId(clientId);
    }

    @Override
    public boolean hasClientCheckedInToday(Long clientId) {
        return attendanceRepository.hasClientCheckedInToday(clientId, LocalDate.now());
    }

    @Override
    public List<Attendance> getAllDeniedAccess() {
        return attendanceRepository.findAllDeniedAccess();
    }

    @Override
    public List<Attendance> getDeniedAccessByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findDeniedAccessByDateRange(startDate, endDate);
    }

    @Override
    public Long countTodaysCheckIns() {
        return attendanceRepository.countTodaysCheckIns(LocalDate.now());
    }

    @Override
    public List<Attendance> getAttendanceBySubscriptionId(Long subscriptionId) {
        return attendanceRepository.findBySubscriptionId(subscriptionId);
    }

    @Override
    public List<Attendance> getAttendanceByFingerprintId(String fingerprintId) {
        return attendanceRepository.findByFingerprintId(fingerprintId);
    }
}