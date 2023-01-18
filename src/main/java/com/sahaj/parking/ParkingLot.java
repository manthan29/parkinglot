package com.sahaj.parking;

import java.time.LocalDateTime;
import java.util.HashMap;

import com.sahaj.parking.fee.FeeModel;
import com.sahaj.parking.fee.FeeModelFactory;
import com.sahaj.parking.fee.FeeModelType;
import com.sahaj.parking.ticket.Receipt;
import com.sahaj.parking.ticket.Ticket;
import com.sahaj.parking.vehicle.Vehicle;
import com.sahaj.parking.vehicle.VehicleType;

public class ParkingLot {

	private FeeModel feeModel;

	private HashMap<Integer, Vehicle> parkedVehicles;

	private HashMap<VehicleType, Integer> parkingSpots;

	public ParkingLot(FeeModelType feeModelType, int motorcycleParkingSpots, int carParkingSpots, int busParkingSpots) {

		this.parkingSpots = new HashMap<>();
		parkingSpots.put(VehicleType.MOTORCYCLE, motorcycleParkingSpots);
		parkingSpots.put(VehicleType.CAR, carParkingSpots);
		parkingSpots.put(VehicleType.BUS, busParkingSpots);
		parkedVehicles = new HashMap<>();
		feeModel = FeeModelFactory.createFeeModel(feeModelType);
	}

	public Ticket park(Vehicle vehicle) {
		int spots = parkingSpots.get(vehicle.getType());
		if (spots == 0)
			return null;
		else
			parkingSpots.put(vehicle.getType(), spots-1);
		
		int parkingSpot = getSpotNoForParking();
		
		parkedVehicles.put(parkingSpot, vehicle);
		return new Ticket(parkingSpot, vehicle.getEntryDateTime());
	}

	public Receipt unpark(Ticket ticket, LocalDateTime exitDateTime) {
		Vehicle vehicle = parkedVehicles.get(ticket.getSpotNo());
		int spots = parkingSpots.get(vehicle.getType());
		parkingSpots.put(vehicle.getType(), spots+1);
		parkedVehicles.remove(ticket.getSpotNo());
		return new Receipt(ticket, exitDateTime, feeModel.calculateFee(vehicle, exitDateTime));
	}
	
	private int getSpotNoForParking() {
		int nextAvaiableParkingSpot = 1;
		while (parkedVehicles.containsKey(nextAvaiableParkingSpot)) {
			nextAvaiableParkingSpot++;
		}
		return nextAvaiableParkingSpot;
	}

}
