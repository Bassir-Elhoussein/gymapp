
package com.web.gymapp.model.enums;

/**
 * Status of a subscription
 * Used to track the current state of a client's subscription
 */
public enum SubscriptionStatus {
    /**
     * Subscription is currently active
     * Client can access gym if dates are valid and payment made
     */
    ACTIVE,

    /**
     * Subscription has passed its end date
     * Automatically set when endDate is in the past
     */
    EXPIRED,

    /**
     * Subscription manually suspended by admin
     * Reasons: behavior issues, payment disputes, etc.
     */
    SUSPENDED,

    /**
     * Subscription cancelled by client or admin
     * Different from EXPIRED - this is a manual cancellation
     */
    CANCELLED
}