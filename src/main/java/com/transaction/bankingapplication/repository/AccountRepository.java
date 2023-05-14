package com.transaction.bankingapplication.repository;

import com.transaction.bankingapplication.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

}
