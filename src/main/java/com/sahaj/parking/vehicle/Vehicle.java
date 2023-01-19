package com.sahaj.parking.vehicle;

import java.time.LocalDateTime;

public class Vehicle {

	private VehicleType type;
	
	public Vehicle(VehicleType type) {
		this.type = type;
	}

	public VehicleType getType() {
		return type;
	}

}
