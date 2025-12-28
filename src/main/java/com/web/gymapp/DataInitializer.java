package com.web.gymapp;

/**
 * Author: Bassir El Houssein
 * Date: 12/28/2025
 */
import com.web.gymapp.model.*;
import com.web.gymapp.model.enums.*;
import com.web.gymapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            ClientRepository clientRepository,
            SubscriptionPlanRepository planRepository,
            SubscriptionRepository subscriptionRepository,
            PaymentRepository paymentRepository,
            AttendanceRepository attendanceRepository
    ) {
        return args -> {

            // 1. Create subscription plans
            SubscriptionPlan plan1 = SubscriptionPlan.builder()
                    .name("Basic Plan5")
                    .price(500.0)
                    .durationMonths(1)
                    .description("Access to gym equipment only")
                    .build();

            SubscriptionPlan plan2 = SubscriptionPlan.builder()
                    .name("Premium Plan2")
                    .price(1200.0)
                    .durationMonths(3)
                    .description("Access to gym + classes")
                    .build();

            planRepository.saveAll(Arrays.asList(plan1, plan2));

            // 2. Create clients
            Client client1 = Client.builder()
                    .fullName("Aya Ziyad")
                    .phone("0601010101")
                    .email("aya@example.com")
                    .gender(Gender.FEMALE)
                    .birthDate(LocalDate.of(2005, 5, 15))
                    .build();

            Client client2 = Client.builder()
                    .fullName("Bassir El Houssein")
                    .phone("0602020202")
                    .email("bassir@example.com")
                    .gender(Gender.MALE)
                    .birthDate(LocalDate.of(2003, 10, 10))
                    .build();

            clientRepository.saveAll(Arrays.asList(client1, client2));

            // 3. Create subscriptions
            Subscription sub1 = Subscription.builder()
                    .client(client1)
                    .plan(plan1)
                    .startDate(LocalDate.now())
                    .endDate(plan1.calculateEndDate(LocalDate.now()))
                    .totalPrice(plan1.getPrice())
                    .build();

            Subscription sub2 = Subscription.builder()
                    .client(client2)
                    .plan(plan2)
                    .startDate(LocalDate.now())
                    .endDate(plan2.calculateEndDate(LocalDate.now()))
                    .totalPrice(plan2.getPrice())
                    .build();

            subscriptionRepository.saveAll(Arrays.asList(sub1, sub2));

            // 4. Create payments
            Payment pay1 = Payment.builder()
                    .subscription(sub1)
                    .amount(sub1.getTotalPrice())
                    .method(PaymentMethod.CASH)
                    .build();

            Payment pay2 = Payment.builder()
                    .subscription(sub2)
                    .amount(sub2.getTotalPrice() / 2) // partial payment
                    .method(PaymentMethod.CHEQUE)
                    .build();

            paymentRepository.saveAll(Arrays.asList(pay1, pay2));

            // Link payments to subscriptions
            sub1.addPayment(pay1);
            sub2.addPayment(pay2);

            subscriptionRepository.saveAll(Arrays.asList(sub1, sub2));

            // 5. Create attendances
            Attendance att1 = Attendance.builder()
                    .client(client1)
                    .subscription(sub1)
                    .accessResult(AccessResult.GRANTED)
                    .build();

            Attendance att2 = Attendance.builder()
                    .client(client2)
                    .subscription(sub2)
                    .accessResult(AccessResult.DENIED_UNPAID)
                    .denialReason("Payment not completed")
                    .build();

            attendanceRepository.saveAll(Arrays.asList(att1, att2));

            System.out.println("Sample data inserted successfully!");
        };
    }
}
