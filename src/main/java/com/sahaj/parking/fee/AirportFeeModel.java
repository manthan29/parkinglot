package com.sahaj.parking.fee;

import java.time.LocalDateTime;

import com.sahaj.parking.vehicle.Vehicle;

public class AirportFeeModel implements FeeModel {

	private static AirportFeeModel airportFeeModel = null;
	
	private static final int MOTORCYCLE_FEE_INTERVAL_1 = 0;
	private static final int MOTORCYCLE_FEE_INTERVAL_2 = 40;
	private static final int MOTORCYCLE_FEE_INTERVAL_3 = 60;
	private static final int MOTORCYCLE_FEE_DAILY = 80;

	private static final int CAR_FEE_INTERVAL_1 = 60;
	private static final int CAR_FEE_INTERVAL_2 = 80;
	private static final int CAR_FEE_DAILY = 100;
	
	
	private AirportFeeModel() {
		super();
	}
	
	public static AirportFeeModel getInstance() {
		if(airportFeeModel == null)
			airportFeeModel = new AirportFeeModel();
		return airportFeeModel;
	}

	@Override
	public int calculateFee(Vehicle vehicle, LocalDateTime exitDateTime) {

		int interval = intervalInHour(vehicle.getEntryDateTime(), exitDateTime);
		int fee;
		switch (vehicle.getType()) {
		case MOTORCYCLE:
			if (interval <= 1)
				fee = MOTORCYCLE_FEE_INTERVAL_1;
			else if (interval <= 8)
				fee = MOTORCYCLE_FEE_INTERVAL_2;
			else if (interval <= 24)
				fee = MOTORCYCLE_FEE_INTERVAL_3;
			else
				fee = calculateDays(interval) * MOTORCYCLE_FEE_DAILY;
			break;
		case CAR:
			if (interval <= 12)
				fee = CAR_FEE_INTERVAL_1;
			else if (interval <= 24)
				fee = CAR_FEE_INTERVAL_2;
			else
				fee = calculateDays(interval) * CAR_FEE_DAILY;
			break;
		default:
			return -1;
		}
		return fee;
	}

	private int calculateDays(int interval) {
		return interval / 24 + ((interval % 24 > 0) ? 1 : 0);
	}

}
