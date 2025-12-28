package com.web.gymapp.model.enums;

/**
 * Author: Bassir El Houssein
 * Date: 12/28/2025
 */


/**
 * User roles for access control
 * ADMIN: Full system access (manage users, plans, reports, etc.)
 * STAFF: Limited access (process payments, check-ins, but limited admin functions)
 */
public enum Role {
    ADMIN,    // Full system access
    STAFF     // Can process payments, check-ins, but limited admin functions
}
