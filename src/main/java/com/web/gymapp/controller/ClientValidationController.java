package com.web.gymapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Bassir El Houssein
 * Date: 11/30/2025
 */
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientValidationController {

//    private final ClientValidationService validationService;
//
//    @GetMapping("/{clientId}/is-valid")
//    public ResponseEntity<Map<String, Object>> isClientValid(@PathVariable Long clientId) {
//        boolean valid = validationService.isClientValid(clientId);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("clientId", clientId);
//        response.put("isValid", valid);
//        response.put("message", valid ?
//                "Client subscription is still active." :
//                "Client subscription is expired or missing."
//        );
//
//        return ResponseEntity.ok(response);
//    }
}
