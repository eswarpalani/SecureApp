package com.virtusa.virtusabank.secureapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Transaction {

	@Id
	@GeneratedValue
	@Column(name = "transaction_id_seq", unique = true, nullable = false)
	private Integer id;

	@Column(name = "from_acc_no", nullable = false)
	private Integer fromAccSeqId;

	@Column(name = "to_acc_no", nullable = false)
	private Integer toAccSeqId;

	@Column(name = "amount", nullable = false)
	private Double amoutTransferred;

	@Column(name = "time", nullable = false)
	private Long timeMilliSec;
	
	@Column(name = "comment")
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@OneToOne
	@JoinColumn(name="fromAccSeqId", insertable=false, updatable=false)
	private Customer fromCustomer;

	@OneToOne
	@JoinColumn(name="toAccSeqId", insertable=false, updatable=false)
	private Customer toCustomer;

	public Long getTimeMilliSec() {
		return timeMilliSec;
	}

	public void setTimeMilliSec(Long timeMilliSec) {
		this.timeMilliSec = timeMilliSec;
	}

	public Customer getFromCustomer() {
		return fromCustomer;
	}

	public void setFromCustomer(Customer fromCustomer) {
		this.fromCustomer = fromCustomer;
	}

	public Customer getToCustomer() {
		return toCustomer;
	}

	public void setToCustomer(Customer toCustomer) {
		this.toCustomer = toCustomer;
	}

	public Integer getFromAccSeqId() {
		return fromAccSeqId;
	}

	public void setFromAccSeqId(Integer fromAccSeqId) {
		this.fromAccSeqId = fromAccSeqId;
	}

	public Integer getToAccSeqId() {
		return toAccSeqId;
	}

	public void setToAccSeqId(Integer toAccSeqId) {
		this.toAccSeqId = toAccSeqId;
	}

	public Double getAmoutTransferred() {
		return amoutTransferred;
	}

	public void setAmoutTransferred(Double amoutTransferred) {
		this.amoutTransferred = amoutTransferred;
	}

}
