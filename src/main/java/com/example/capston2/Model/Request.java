package com.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Client ID cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer clientId;

    @NotNull(message = "Designer ID cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer designerId;

    @NotNull(message = "Request time cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate requestTime;

    @NotEmpty(message = "Project details cannot be empty")
    @Size(min = 10, max = 500, message = "Project details must be between 10 and 500 characters")
    @Column(columnDefinition = "text not null")
    private String projectDetails;

    @NotEmpty(message = "Status cannot be empty")
    @Column(columnDefinition = "varchar(50) not null")
    @Pattern(regexp = "^(On Hold|Accepted|Rejected)$")
    private String status = "On Hold";
}
