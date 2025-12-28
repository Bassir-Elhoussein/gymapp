package com.web.gymapp.service.impl;

import com.web.gymapp.model.AppUser;
import com.web.gymapp.model.Payment;
import com.web.gymapp.model.Subscription;
import com.web.gymapp.model.enums.PaymentMethod;
import com.web.gymapp.repository.PaymentRepository;
import com.web.gymapp.repository.SubscriptionRepository;
import com.web.gymapp.repository.UserRepository;
import com.web.gymapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Payment processPayment(Long subscriptionId, Double amount, PaymentMethod method,
                                  String notes, Long processedByUserId) {
        // Validate subscription exists
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));

        // Validate amount
        if (amount == null || amount <= 0) {
            throw new RuntimeException("Payment amount must be greater than 0");
        }

        // Get user who processed payment (if provided)
        AppUser processedBy = null;
        if (processedByUserId != null) {
            processedBy = userRepository.findById(processedByUserId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + processedByUserId));
        }

        // Create payment
        Payment payment = Payment.builder()
                .subscription(subscription)
                .amount(amount)
                .method(method != null ? method : PaymentMethod.CASH)
                .notes(notes)
                .processedBy(processedBy)
                .build();

        payment = paymentRepository.save(payment);

        // Update subscription payment totals
        subscription.addPayment(payment);
        subscriptionRepository.save(subscription);

        return payment;
    }

    @Override
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsBySubscriptionId(Long subscriptionId) {
        return paymentRepository.findBySubscriptionId(subscriptionId);
    }

    @Override
    public List<Payment> getPaymentsByClientId(Long clientId) {
        return paymentRepository.findByClientId(clientId);
    }

    @Override
    public List<Payment> getPaymentsByMethod(PaymentMethod method) {
        return paymentRepository.findByMethod(method);
    }

    @Override
    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    @Override
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByProcessedByUserId(userId);
    }

    @Override
    public List<Payment> findTodaysPayments() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return paymentRepository.findTodaysPayments(startOfDay, endOfDay);
    }


    @Override
    public List<Payment> getRecentPayments(int limit) {
        return paymentRepository.findRecentPayments()
                .stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<Payment> getPaymentsByAmountRange(Double minAmount, Double maxAmount) {
        return paymentRepository.findByAmountRange(minAmount, maxAmount);
    }

    @Override
    public Optional<Payment> getLatestPaymentForSubscription(Long subscriptionId) {
        List<Payment> payments = paymentRepository.findLatestPaymentBySubscriptionId(subscriptionId);
        return payments.isEmpty() ? Optional.empty() : Optional.of(payments.get(0));
    }

    @Override
    public Double calculateTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = paymentRepository.calculateTotalRevenue(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public Double calculateRevenueByMethod(PaymentMethod method, LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = paymentRepository.calculateRevenueByMethod(method, startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public Double calculateTodaysRevenue() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        Double revenue = paymentRepository.calculateTodaysRevenue(startOfDay, endOfDay);
        return revenue != null ? revenue : 0.0;
    }


    @Override
    public Long countTotalPayments() {
        return paymentRepository.countTotalPayments();
    }

    @Override
    public Long countPaymentsByMethod(PaymentMethod method) {
        return paymentRepository.countPaymentsByMethod(method);
    }
}