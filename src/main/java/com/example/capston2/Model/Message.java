package com.example.capston2.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer chatRoomId;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer senderId;
    @NotEmpty
    @Column(columnDefinition = "varchar(100) not null")
    private String senderName;
    @NotNull
    @Column(columnDefinition = "int not null")
    private Integer receiverId;
    @NotEmpty
    @Column(columnDefinition = "text not null")
    private String content;
    @Column(columnDefinition = "varchar(100)")
    private String attachmentUrl;
    @Column(columnDefinition = "date not null")
    private LocalDate timeStamp;
}
