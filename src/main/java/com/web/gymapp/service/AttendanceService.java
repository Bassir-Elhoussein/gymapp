package com.web.gymapp.service;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.Attendance;

import java.util.List;

public interface AttendanceService {
    Attendance checkIn(Long clientId);
    List<Attendance> getAll();
}