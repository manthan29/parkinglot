package com.sahaj.parking.vehicle;

import java.time.LocalDateTime;

public class Vehicle {

	private VehicleType type;
	private LocalDateTime entryDateTime;
	
	public Vehicle(VehicleType type, LocalDateTime entryDateTime) {
		this.entryDateTime = entryDateTime;
		this.type = type;
	}

	public VehicleType getType() {
		return type;
	}

	public LocalDateTime getEntryDateTime() {
		return entryDateTime;
	}

	@Override
	public String toString() {
		return "Vehicle [type=" + type + ", entryDateTime=" + entryDateTime + "]";
	}
	
	
}
