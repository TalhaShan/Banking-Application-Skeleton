package com.transaction.bankingapplication.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String accountNumber;
    @NotNull
    private String name;
    @NotNull
    private Double balance;

    private LocalDateTime creationDate;

    private Boolean isActive=true;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL)
    private List<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "targetAccount", cascade = CascadeType.ALL)
    private List<Transaction> incomingTransactions;

    public Account(String accountNumber, String name, Double balance, LocalDateTime creationDate, Boolean isActive) {

        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.creationDate = creationDate;
        this.isActive = isActive;
    }
}
