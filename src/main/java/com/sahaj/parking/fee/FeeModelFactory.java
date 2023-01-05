package com.sahaj.parking.fee;

public class FeeModelFactory {
	
	public static FeeModel createFeeModel(FeeModelType type) {
        if (type.equals(FeeModelType.MALL)) {
            return new MallFeeModel();
        } else if (type.equals(FeeModelType.STADIUM)) {
            return new StadiumFeeModel();
        } else if (type.equals(FeeModelType.AIRPORT)) {
            return new AirportFeeModel();
        } else {
            return null;
        }
	}

}
