package com.FinFlow.repository;

import com.FinFlow.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, DAO {
}
