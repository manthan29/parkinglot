package com.sahaj.parking.fee;

public class FeeModelFactory {

	private FeeModelFactory() {
		super();
	}

	public static IFeeModel createFeeModel(FeeModelType type) {
		switch (type) {
		case MALL:
			return MallFeeModel.getInstance();
		case STADIUM:
			return StadiumFeeModel.getInstance();
		case AIRPORT:
			return AirportFeeModel.getInstance();
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

}
