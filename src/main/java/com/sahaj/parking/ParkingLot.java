package com.sahaj.parking;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sahaj.parking.fee.FeeModelFactory;
import com.sahaj.parking.fee.FeeModelType;
import com.sahaj.parking.fee.IFeeModel;
import com.sahaj.parking.ticket.Receipt;
import com.sahaj.parking.ticket.Ticket;
import com.sahaj.parking.vehicle.VehicleEntry;
import com.sahaj.parking.vehicle.VehicleType;

public class ParkingLot {

	private IFeeModel feeModel;

	private Map<Integer, VehicleEntry> vehicleEntries;

	private Map<VehicleType, Integer> parkingSpots;

	public ParkingLot(FeeModelType feeModelType, Map<VehicleType, Integer> parkingSpots) {

		this.parkingSpots = new HashMap<>();
		this.parkingSpots = parkingSpots;
		// Stadium and Airport do not allow Bus
		if (feeModelType == FeeModelType.STADIUM || feeModelType == FeeModelType.AIRPORT)
			this.parkingSpots.remove(VehicleType.BUS);
		vehicleEntries = new HashMap<>();
		feeModel = FeeModelFactory.createFeeModel(feeModelType);
	}

	public Optional<Ticket> park(VehicleEntry vehicleEntry) {
		int spots = parkingSpots.getOrDefault(vehicleEntry.getVehicle().getType(), 0);
		if (spots == 0)
			return Optional.empty();
		else
			parkingSpots.put(vehicleEntry.getVehicle().getType(), spots - 1);

		int parkingSpot = getSpotNoForParking();
		
		vehicleEntries.put(parkingSpot, vehicleEntry);
		return Optional.of(new Ticket(parkingSpot, vehicleEntry.getEntryDateTime()));
	}

	public Optional<Receipt> unpark(Ticket ticket, LocalDateTime exitDateTime) {
		VehicleEntry vehicleEntry = vehicleEntries.get(ticket.getSpotNo());
		int spots = parkingSpots.get(vehicleEntry.getVehicle().getType());
		parkingSpots.put(vehicleEntry.getVehicle().getType(), spots + 1);
		vehicleEntries.remove(ticket.getSpotNo());
		Optional<Integer> feeOpt = feeModel.calculateFee(vehicleEntry, exitDateTime);
		if (!feeOpt.isPresent())
			return Optional.empty();
		return Optional.of(new Receipt(ticket, exitDateTime, feeOpt.get()));
	}

	private int getSpotNoForParking() {
		int nextAvaiableParkingSpot = 1;
		while (vehicleEntries.containsKey(nextAvaiableParkingSpot)) {
			nextAvaiableParkingSpot++;
		}
		return nextAvaiableParkingSpot;
	}

}
