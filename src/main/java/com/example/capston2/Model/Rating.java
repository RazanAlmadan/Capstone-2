package com.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer clientId;
    @NotNull(message = "designer Id cannot be empty")
    @Column(columnDefinition = "int not null unique")
    private Integer designerId;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer orderId;
    @Min(1)
    @Max(5)
    @Column(columnDefinition = "int")
    private Integer rate;
    @Size(min = 4, max = 500)
    private String comment;
    @NotNull
    @Column(columnDefinition = "date not null")
    private LocalDate timeStamp = LocalDate.now();
}
