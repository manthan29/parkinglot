package com.sahaj.parking.fee;

public class AirportFeeModel extends FeeModel {

	private static AirportFeeModel airportFeeModel;

	private AirportFeeModel() {
		super(FeeModelType.AIRPORT);
	}

	public static AirportFeeModel getInstance() {
		if (airportFeeModel == null)
			airportFeeModel = new AirportFeeModel();
		return airportFeeModel;
	}

}
