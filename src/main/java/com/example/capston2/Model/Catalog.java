package com.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer designerId;
    @NotEmpty(message = "Catalog name cannot be empty")
    @Size(min = 4, max = 50, message = "Catalog name must be between 4 and 50 characters")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    private ArrayList<String> images;

    @Column(columnDefinition = "text")
    private String aiStyle;

}
