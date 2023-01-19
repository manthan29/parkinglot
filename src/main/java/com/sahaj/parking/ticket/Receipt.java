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
	public String toString() {
		return "Receipt [receiptNo=" + receiptNo + ", ticket=" + ticket + ", exitDateTime=" + exitDateTime + ", fee="
				+ fee + "]";
	}

	public static void resetCounter() {
		receiptCounter = 0;
	}
}
