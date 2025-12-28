package com.web.gymapp.controller;

import com.web.gymapp.model.Payment;
import com.web.gymapp.model.enums.PaymentMethod;
import com.web.gymapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Process a new payment
     * POST /api/payments
     */
    @PostMapping
    public ResponseEntity<Payment> processPayment(
            @RequestParam Long subscriptionId,
            @RequestParam Double amount,
            @RequestParam(required = false, defaultValue = "CASH") PaymentMethod method,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) Long processedByUserId) {
        Payment payment = paymentService.processPayment(subscriptionId, amount, method, notes, processedByUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    /**
     * Get payment by ID
     * GET /api/payments/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all payments
     * GET /api/payments
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    /**
     * Get all payments for a subscription
     * GET /api/payments/subscription/1
     */
    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<Payment>> getPaymentsBySubscription(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySubscriptionId(subscriptionId));
    }

    /**
     * Get all payments for a client (across all subscriptions)
     * GET /api/payments/client/1
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Payment>> getPaymentsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(paymentService.getPaymentsByClientId(clientId));
    }

    /**
     * Get payments by payment method
     * GET /api/payments/method/CASH
     */
    @GetMapping("/method/{method}")
    public ResponseEntity<List<Payment>> getPaymentsByMethod(@PathVariable PaymentMethod method) {
        return ResponseEntity.ok(paymentService.getPaymentsByMethod(method));
    }

    /**
     * Get payments within date range
     * GET /api/payments/date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getPaymentsByDateRange(startDate, endDate));
    }

    /**
     * Get payments processed by a specific user
     * GET /api/payments/user/1
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }

    /**
     * Get today's payments
     * GET /api/payments/today
     */
    @GetMapping("/today")
    public ResponseEntity<List<Payment>> getTodaysPayments() {
        return ResponseEntity.ok(paymentService.findTodaysPayments());
    }

    /**
     * Get recent payments
     * GET /api/payments/recent?limit=10
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Payment>> getRecentPayments(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(paymentService.getRecentPayments(limit));
    }

    /**
     * Get payments by amount range
     * GET /api/payments/amount-range?minAmount=100&maxAmount=500
     */
    @GetMapping("/amount-range")
    public ResponseEntity<List<Payment>> getPaymentsByAmountRange(
            @RequestParam Double minAmount,
            @RequestParam Double maxAmount) {
        return ResponseEntity.ok(paymentService.getPaymentsByAmountRange(minAmount, maxAmount));
    }

    /**
     * Get latest payment for a subscription
     * GET /api/payments/subscription/1/latest
     */
    @GetMapping("/subscription/{subscriptionId}/latest")
    public ResponseEntity<Payment> getLatestPaymentForSubscription(@PathVariable Long subscriptionId) {
        return paymentService.getLatestPaymentForSubscription(subscriptionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Calculate total revenue within date range
     * GET /api/payments/revenue/total?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/revenue/total")
    public ResponseEntity<Map<String, Double>> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double revenue = paymentService.calculateTotalRevenue(startDate, endDate);
        return ResponseEntity.ok(Map.of("totalRevenue", revenue));
    }

    /**
     * Calculate revenue by payment method
     * GET /api/payments/revenue/by-method?method=CASH&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/revenue/by-method")
    public ResponseEntity<Map<String, Object>> getRevenueByMethod(
            @RequestParam PaymentMethod method,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        Double revenue = paymentService.calculateRevenueByMethod(method, startDate, endDate);

        return ResponseEntity.ok(
                Map.of(
                        "revenue", revenue,
                        "method", method.name()
                )
        );
    }

    /**
     * Calculate today's revenue
     * GET /api/payments/revenue/today
     */
    @GetMapping("/revenue/today")
    public ResponseEntity<Map<String, Double>> getTodaysRevenue() {
        Double revenue = paymentService.calculateTodaysRevenue();
        return ResponseEntity.ok(Map.of("todaysRevenue", revenue));
    }

    /**
     * Get payment statistics
     * GET /api/payments/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPaymentStats() {
        Long totalPayments = paymentService.countTotalPayments();
        Double todaysRevenue = paymentService.calculateTodaysRevenue();
        List<Payment> todaysPayments = paymentService.findTodaysPayments();

        // Calculate revenue by method
        Double cashRevenue = paymentService.calculateRevenueByMethod(
                PaymentMethod.CASH,
                LocalDateTime.now().withHour(0).withMinute(0),
                LocalDateTime.now()
        );

        Long cashCount = paymentService.countPaymentsByMethod(PaymentMethod.CASH);

        Map<String, Object> stats = Map.of(
                "totalPayments", totalPayments,
                "todaysRevenue", todaysRevenue,
                "todaysPaymentCount", todaysPayments.size(),
                "cashRevenue", cashRevenue,
                "cashPaymentCount", cashCount
        );

        return ResponseEntity.ok(stats);
    }

    /**
     * Get revenue breakdown by payment method for date range
     * GET /api/payments/revenue/breakdown?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/revenue/breakdown")
    public ResponseEntity<Map<String, Object>> getRevenueBreakdown(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        Double totalRevenue = paymentService.calculateTotalRevenue(startDate, endDate);
        Double cashRevenue = paymentService.calculateRevenueByMethod(PaymentMethod.CASH, startDate, endDate);
        Double chequeRevenue = paymentService.calculateRevenueByMethod(PaymentMethod.CHEQUE, startDate, endDate);
        // Double cardRevenue = paymentService.calculateRevenueByMethod(PaymentMethod.CARD, startDate, endDate);
        // Double bankRevenue = paymentService.calculateRevenueByMethod(PaymentMethod.BANK_TRANSFER, startDate, endDate);

        Map<String, Object> breakdown = Map.of(
                "totalRevenue", totalRevenue,
                "cash", cashRevenue,
                "cheque", chequeRevenue,
                // "card", cardRevenue,
                // "bankTransfer", bankRevenue,
                "startDate", startDate,
                "endDate", endDate
        );

        return ResponseEntity.ok(breakdown);
    }

}