
package com.web.gymapp.model.enums;

/**
 * Result of gym access attempt (from fingerprint machine)
 * Used in Attendance entity to track why access was granted/denied
 */
public enum AccessResult {
    GRANTED,                  // ✅ Access allowed - subscription is valid
    DENIED_EXPIRED,          // ❌ Subscription has expired (past endDate)
    DENIED_UNPAID,           // ❌ No payment made or insufficient payment
    DENIED_NO_SUBSCRIPTION,  // ❌ Client has no active subscription at all
    DENIED_SUSPENDED,        // ❌ Subscription suspended by admin
    DENIED_FINGERPRINT_ERROR // ❌ Fingerprint not recognized or device error
}