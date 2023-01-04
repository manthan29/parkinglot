package com.sahaj.parking.fee;

public class FeeModelFactory {
	
	public static FeeModel createFeeModel(LocationType type) {
        if (type.equals(LocationType.MALL)) {
            return new MallFeeModel();
        } else if (type.equals(LocationType.STADIUM)) {
            return new StadiumFeeModel();
        } else if (type.equals(LocationType.AIRPORT)) {
            return new AirportFeeModel();
        } else {
            return null;
        }
	}

}
