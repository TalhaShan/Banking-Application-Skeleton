package com.transaction.bankingapplication.repository;

import com.transaction.bankingapplication.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {


}
