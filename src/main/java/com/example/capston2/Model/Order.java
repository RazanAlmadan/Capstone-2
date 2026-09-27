package com.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`orders`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Client Id cannot be empty")
    @Column(columnDefinition = "int not null unique")
    private Integer clientId;
    @NotNull(message = "Designer ID cannot be empty")
    @Column(columnDefinition = "int not null unique")
    private Integer designerId;
    @NotNull(message = "Price cannot be empty")
    @Column(columnDefinition = "double not null")
    private Double price;

    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null")
    @Pattern(regexp = "^(Waiting for Down Payment|Work In Progress|Draft Sent|Done)$")
    private String status;

    @NotNull(message = "DeadLine cannot be empty")
    private LocalDate deadLine;

}
