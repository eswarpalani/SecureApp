package com.virtusa.virtusabank.secureapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virtusa.virtusabank.secureapp.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{ }