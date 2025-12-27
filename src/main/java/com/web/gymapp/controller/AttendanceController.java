package com.web.gymapp.controller;

/**
 * Author: Bassir El Houssein
 * Date: 11/24/2025
 */


import com.web.gymapp.model.Attendance;
import com.web.gymapp.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;




    @PostMapping("/checkin/{clientId}")
    public Attendance checkIn(@PathVariable Long clientId) {
        return service.checkIn(clientId);
    }

    @GetMapping
    public List<Attendance> getAll() {
        return service.getAll();
    }
}
