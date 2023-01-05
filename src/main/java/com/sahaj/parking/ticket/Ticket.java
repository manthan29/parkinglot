package com.sahaj.parking.ticket;

import java.time.LocalDateTime;

public class Ticket {

	private static int ticketCounter = 0;
	
	private int ticketNo;
	private int spotNo;
	private LocalDateTime entryDateTime;
	
	public Ticket(int spotNo, LocalDateTime entryDateTime) {
		super();
		this.ticketNo = ++ticketCounter;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryDateTime == null) ? 0 : entryDateTime.hashCode());
		result = prime * result + spotNo;
		result = prime * result + ticketNo;
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
		Ticket other = (Ticket) obj;
		if (entryDateTime == null) {
			if (other.entryDateTime != null)
				return false;
		} else if (!entryDateTime.equals(other.entryDateTime))
			return false;
		if (spotNo != other.spotNo)
			return false;
		if (ticketNo != other.ticketNo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ticket [ticketNo=" + ticketNo + ", spotNo=" + spotNo + ", entryDateTime=" + entryDateTime + "]";
	}
	
	public static void resetCounter() {
		ticketCounter = 0;
	}
	
}
