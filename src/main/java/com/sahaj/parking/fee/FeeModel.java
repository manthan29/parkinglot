package com.sahaj.parking.fee;

import java.time.LocalDateTime;

public interface FeeModel {

	int calculateFee(int spotNumber, LocalDateTime exitDateTime);
	
}
