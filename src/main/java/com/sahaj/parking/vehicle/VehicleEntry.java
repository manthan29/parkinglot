package com.sahaj.parking.vehicle;

import java.time.LocalDateTime;

public class VehicleEntry {

	private Vehicle vehicle;
	private LocalDateTime entryDateTime;
	
	public VehicleEntry(Vehicle vehicle, LocalDateTime entryDateTime) {
		this.entryDateTime = entryDateTime;
		this.vehicle = vehicle;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public LocalDateTime getEntryDateTime() {
		return entryDateTime;
	}

}
