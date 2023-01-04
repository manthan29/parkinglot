package com.sahaj.parking.ticket;

import java.time.LocalDateTime;

public class Ticket {

	private static int ticketCounter = 0;
	
	private int ticketNo;
	private int spotNo;
	private LocalDateTime entryDateTime;
	
	public Ticket(int spotNo, LocalDateTime entryDateTime) {
		super();
		this.ticketNo = ticketCounter++;
		this.spotNo = spotNo;
		this.entryDateTime = entryDateTime;
	}

	public int getTicketNo() {
		return ticketNo;
	}

	public int getSpotNo() {
		return spotNo;
	}

	public LocalDateTime getEntryDateTime() {
		return entryDateTime;
	}
	
	
	
}
