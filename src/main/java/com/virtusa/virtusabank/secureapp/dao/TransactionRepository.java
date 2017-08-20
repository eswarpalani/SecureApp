package com.virtusa.virtusabank.secureapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.virtusa.virtusabank.secureapp.model.Transaction;

public interface TransactionRepository
		extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {

}
