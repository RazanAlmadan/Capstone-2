package com.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Designer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    @Column(columnDefinition = "varchar(255) not null unique")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @NotEmpty(message = "Visual field cannot be empty")
    @Column(columnDefinition = "varchar(100) not null")
    @Pattern(regexp = "^(Logo Design|Interior Design|Illustration|Motion Graphics|UI/UX|Social Media Design|3D Modeling)$")
    private String visualField;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(columnDefinition = "double")
    private Double averageRating;

    private ArrayList<Integer> catalogs;

    private Integer ratingCount = 0;

    private String profileImageUrl;
}
