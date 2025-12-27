package com.web.gymapp.service.impl;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.*;
import com.web.gymapp.model.enums.SubscriptionStatus;
import com.web.gymapp.repository.AttendanceRepository;
import com.web.gymapp.repository.SubscriptionRepository;
import com.web.gymapp.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Attendance checkIn(Long clientId) {

        Subscription subscription = subscriptionRepository.findAll().stream()
                .filter(sub -> sub.getClient().getId().equals(clientId))
                .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE)
                .filter(sub -> !LocalDate.now().isBefore(sub.getStartDate())
                        && !LocalDate.now().isAfter(sub.getEndDate()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client has no active subscription"));

        Attendance attendance = Attendance.builder()
                .client(subscription.getClient())
                .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> getAll() {
        return attendanceRepository.findAll();
    }
}

