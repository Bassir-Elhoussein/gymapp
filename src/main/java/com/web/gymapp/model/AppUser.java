package com.web.gymapp.model;
/**
 * Author: Bassir El Houssein
 */

import com.web.gymapp.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "app_user")
public class AppUser {


    /**
     * Staff/Admin users who manage the gym system
     * Used for authentication and tracking who processed payments
     */



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String username;

        @Column(nullable = false)
        private String password; // Store hashed password (use BCrypt)

        private String fullName;

        private String email;

        private String phone;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role; // ADMIN or STAFF

        private Boolean isActive; // Can be used to disable user accounts

        private LocalDateTime createdAt;
        private LocalDateTime lastLogin;

        @PrePersist
        public void prePersist() {
            this.createdAt = LocalDateTime.now();
            if (this.isActive == null) {
                this.isActive = true;
            }
            if (this.role == null) {
                this.role = Role.STAFF;
            }
        }

        // Helper methods
        public boolean isAdmin() {
            return this.role == Role.ADMIN;
        }

        public boolean isStaff() {
            return this.role == Role.STAFF;
        }

        public void updateLastLogin() {
            this.lastLogin = LocalDateTime.now();
        }
    }