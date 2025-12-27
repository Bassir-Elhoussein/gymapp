package com.web.gymapp.model;
/**
 * Author: Bassir El Houssein
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor@Data
@Entity
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<AppUser> users;

    public UserRole(Long id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }
}
