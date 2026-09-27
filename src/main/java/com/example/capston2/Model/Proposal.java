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
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer chatRoomId;
    @NotNull
    @Column(columnDefinition = "double not null")
    private Double price;
    @NotNull
    @Column(columnDefinition = "date not null")
    private LocalDate deadLine;
    @NotEmpty
    @Column(columnDefinition = "text not null")
    private String details;
    @NotEmpty
    @Column(columnDefinition = "varchar(50) not null")
    @Pattern(regexp = "^(On Hold|Accepted|Rejected)$")
    private String status;
}
