package com.sahaj.parking.fee;

import java.time.temporal.ValueRange;

import com.sahaj.parking.vehicle.VehicleType;

public class FeeRule {

	private VehicleType vehicleType;
	
	private ValueRange hourRange;
	
	private int baseFee;
	
	private FeeModelType feeModelType;
	 
	private FeeRuleType ruleType;
	
	private boolean cumulative;

	public FeeRule(VehicleType vehicleType, int startHour, int endHour, int baseFee, FeeModelType feeModelType,
			FeeRuleType ruleType, boolean cumulative) {
		super();
		this.vehicleType = vehicleType;
		this.hourRange = ValueRange.of(startHour, endHour);
		this.baseFee = baseFee;
		this.feeModelType = feeModelType;
		this.ruleType = ruleType;
		this.cumulative = cumulative;
	}
	
	public ValueRange getHourRange() {
		return hourRange;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public int getBaseFee() {
		return baseFee;
	}

	public FeeModelType getFeeModelType() {
		return feeModelType;
	}

	public FeeRuleType getRuleType() {
		return ruleType;
	}
	
	public boolean isCumulative() {
		return cumulative;
	}

}
