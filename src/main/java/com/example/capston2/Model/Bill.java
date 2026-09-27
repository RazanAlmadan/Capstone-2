package com.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer orderId;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Double downPayment;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Double fullPayment;
    @NotEmpty
    @Pattern(regexp = "^(Pay The Down Payment|Soon|Pay The Full Payment|Paid)$")
    private String statues;
}
