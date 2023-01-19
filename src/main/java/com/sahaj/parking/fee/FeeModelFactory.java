package com.sahaj.parking.fee;

public class FeeModelFactory {

	private FeeModelFactory() {
		super();
	}

	public static IFeeModel createFeeModel(FeeModelType type) {
		if (type.equals(FeeModelType.MALL)) {
			return MallFeeModel.getInstance();
		} else if (type.equals(FeeModelType.STADIUM)) {
			return StadiumFeeModel.getInstance();
		} else if (type.equals(FeeModelType.AIRPORT)) {
			return AirportFeeModel.getInstance();
		} else {
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

}
