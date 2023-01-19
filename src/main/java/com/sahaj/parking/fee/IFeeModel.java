package com.sahaj.parking.fee;

import java.time.LocalDateTime;
import java.util.Optional;

import com.sahaj.parking.vehicle.VehicleEntry;

public interface IFeeModel {

	public abstract Optional<Integer> calculateFee(VehicleEntry parkingSpot, LocalDateTime exitDateTime);

}
