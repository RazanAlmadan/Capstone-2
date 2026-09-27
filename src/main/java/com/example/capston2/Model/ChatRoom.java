package com.example.capston2.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Request ID cannot be empty")
    @Column(columnDefinition = "int not null unique")
    private Integer requestId;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer designerId;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer clientId;
    @NotNull(message = "Created at time cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate createdAt;

    private Integer proposalId;
}
