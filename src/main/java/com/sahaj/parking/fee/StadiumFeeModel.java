package com.sahaj.parking.fee;

public class StadiumFeeModel extends FeeModel {

	private static StadiumFeeModel stadiumFeeModel;

	private StadiumFeeModel() {
		super(FeeModelType.STADIUM);
	}

	public static StadiumFeeModel getInstance() {
		if (stadiumFeeModel == null)
			stadiumFeeModel = new StadiumFeeModel();
		return stadiumFeeModel;
	}

}
