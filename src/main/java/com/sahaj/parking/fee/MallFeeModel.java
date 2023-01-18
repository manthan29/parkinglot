package com.sahaj.parking.fee;

import java.time.LocalDateTime;

import com.sahaj.parking.vehicle.Vehicle;

public class MallFeeModel implements FeeModel {

	private static MallFeeModel mallFeeModel;
	
	private static final int MOTORCYCLE_FLAT_FEE_PER_HOUR = 10;
	private static final int CAR_FLAT_FEE_PER_HOUR = 20;
	private static final int BUS_FLAT_FEE_PER_HOUR = 50;

	private MallFeeModel() {
		super();
	}
	
	public static MallFeeModel getInstance() {
		if(mallFeeModel == null) 
			mallFeeModel = new MallFeeModel();
		return mallFeeModel;
	}
	
	@Override
	public int calculateFee(Vehicle vehicle, LocalDateTime exitDateTime) {

		int interval = intervalInHour(vehicle.getEntryDateTime(), exitDateTime);
		int fee;
		switch (vehicle.getType()) {
		case MOTORCYCLE:
			fee = MOTORCYCLE_FLAT_FEE_PER_HOUR;
			break;
		case CAR:
			fee = CAR_FLAT_FEE_PER_HOUR;
			break;
		case BUS:
			fee = BUS_FLAT_FEE_PER_HOUR;
			break;
		default:
			return -1;
		}
		return fee * interval;
	}

	

}
