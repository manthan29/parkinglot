package com.sahaj.parking.fee;

import java.time.LocalDateTime;

import com.sahaj.parking.vehicle.Vehicle;

public class StadiumFeeModel implements FeeModel {

	private static final int MOTORCYCLE_FEE_INTERVAL_1 = 30;
	private static final int MOTORCYCLE_FEE_INTERVAL_2 = 60;
	private static final int MOTORCYCLE_FEE_HOURLY = 100;

	private static final int CAR_FEE_INTERVAL_1 = 60;
	private static final int CAR_FEE_INTERVAL_2 = 120;
	private static final int CAR_FEE_HOURLY = 200;

	@Override
	public int calculateFee(Vehicle vehicle, LocalDateTime exitDateTime) {

		int interval = intervalInHour(vehicle.getEntryDateTime(), exitDateTime);
		switch (vehicle.getType()) {
		case MOTORCYCLE:
			return MOTORCYCLE_FEE_INTERVAL_1 + interval2Multiplier(interval) * MOTORCYCLE_FEE_INTERVAL_2
					+ hourlyMultiplier(interval) * MOTORCYCLE_FEE_HOURLY;
		case CAR:
			return CAR_FEE_INTERVAL_1 + interval2Multiplier(interval) * CAR_FEE_INTERVAL_2 + hourlyMultiplier(interval) * CAR_FEE_HOURLY;
		default:
			return -1;
		}
	}
	
	private int interval2Multiplier(int interval) {
		return (interval > 4 ? 1 : 0);
	}
	
	private int hourlyMultiplier(int interval) {
		return ((interval - 12) > 0 ? (interval - 12) : 0);
	}

}
