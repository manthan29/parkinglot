package com.sahaj.parking.ticket;

import java.time.LocalDateTime;

public class Receipt {

	private static int receiptCounter = 0;

	private int receiptNo;
	private Ticket ticket;
	private LocalDateTime exitDateTime;
	private int fee;

	public Receipt(Ticket ticket, LocalDateTime exitDateTime, int fee) {
		super();
		this.ticket = ticket;
		this.fee = fee;
		this.exitDateTime = exitDateTime;
		this.receiptNo = ++receiptCounter;
	}
	
	
	//To be used only for testing
	public Receipt(int receiptNo, Ticket ticket, LocalDateTime exitDateTime, int fee) {
		super();
		this.receiptNo = receiptNo;
		this.ticket = ticket;
		this.exitDateTime = exitDateTime;
		this.fee = fee;
	}



	public LocalDateTime getExitDateTime() {
		return exitDateTime;
	}

	public int getReceiptNo() {
		return receiptNo;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public int getFee() {
		return fee;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exitDateTime == null) ? 0 : exitDateTime.hashCode());
		result = prime * result + fee;
		result = prime * result + receiptNo;
		result = prime * result + ((ticket == null) ? 0 : ticket.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Receipt other = (Receipt) obj;
		if (exitDateTime == null) {
			if (other.exitDateTime != null)
				return false;
		} else if (!exitDateTime.equals(other.exitDateTime))
			return false;
		if (fee != other.fee)
			return false;
		if (receiptNo != other.receiptNo)
			return false;
		if (ticket == null) {
			if (other.ticket != null)
				return false;
		} else if (!ticket.equals(other.ticket))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Receipt [receiptNo=" + receiptNo + ", ticket=" + ticket + ", exitDateTime=" + exitDateTime + ", fee="
				+ fee + "]";
	}

	public static void resetCounter() {
		receiptCounter = 0;
	}
}
