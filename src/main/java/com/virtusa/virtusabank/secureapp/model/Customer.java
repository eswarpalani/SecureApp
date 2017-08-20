package com.virtusa.virtusabank.secureapp.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
	@GeneratedValue
	@Column(name = "cust_id_seq", unique = true, nullable = false)
	private Integer id;

	@Column(name = "cust_first_name", nullable = false)
	private String firstName;
	
	@Column(name = "cust_last_name")
	private String lastName;
	
	@Column(name = "dob")
	private String dob;
	
	@Column(name = "cust_ssn")
	private String ssn;
	
	@Column(name = "uid", unique=true)
	private String uid = UUID.randomUUID().toString();

	@Column(name = "balance")
	private Double balance;
	
	@Column(name = "acct_no")
	private String acctNumber;

	public Integer getId() {
		return id;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getUid() {
		return uid;
	}

	public String getAcctNumber() {
		return acctNumber;
	}

	public void setAcctNumber(String acctNumber) {
		this.acctNumber = acctNumber;
	}
	
}
