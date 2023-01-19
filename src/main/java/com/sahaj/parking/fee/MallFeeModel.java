package com.sahaj.parking.fee;

public class MallFeeModel extends FeeModel {

	private static MallFeeModel mallFeeModel;

	private MallFeeModel() {
		super(FeeModelType.MALL);
	}

	public static MallFeeModel getInstance() {
		if (mallFeeModel == null) {
			mallFeeModel = new MallFeeModel();
			
		}
		return mallFeeModel;
	}

}
