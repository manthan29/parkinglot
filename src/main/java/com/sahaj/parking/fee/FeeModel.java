package com.sahaj.parking.fee;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sahaj.parking.vehicle.Vehicle;

public interface FeeModel {

	public abstract int calculateFee(Vehicle vehicle, LocalDateTime exitDateTime);
	
	public default int intervalInHour(LocalDateTime entryTime, LocalDateTime exitTime) {
		int interval = (int) ChronoUnit.MINUTES.between(entryTime, exitTime);
		return (int) Math.ceil(interval / 60.0);
	}
	
}
