package com.sahaj.parking;

import java.time.LocalDateTime;
import java.util.HashMap;

import com.sahaj.parking.fee.FeeModel;
import com.sahaj.parking.fee.FeeModelFactory;
import com.sahaj.parking.fee.LocationType;
import com.sahaj.parking.ticket.Receipt;
import com.sahaj.parking.ticket.Ticket;
import com.sahaj.parking.vehicle.Vehicle;

public class ParkingLot {

	private FeeModel feeModel;
	private int motorcycleParkingSpots;
	private int carParkingSpots;
	private int busParkingSpots;
	private HashMap<Integer, Vehicle> parkedVehicles;

	public ParkingLot(LocationType locationType, int motorcycleParkingSpots, int carParkingSpots, int busParkingSpots) {
		this.busParkingSpots = busParkingSpots;
		this.carParkingSpots = carParkingSpots;
		this.motorcycleParkingSpots = motorcycleParkingSpots;
		parkedVehicles = new HashMap<>();
		feeModel = FeeModelFactory.createFeeModel(locationType);
	}

	public Ticket park(Vehicle vehicle) {
		switch (vehicle.getType()) {

		case MOTORCYCLE:
			if (motorcycleParkingSpots == 0)
				return null;
			motorcycleParkingSpots--;
			break;
		case CAR:
			if (carParkingSpots == 0)
				return null;
			carParkingSpots--;
			break;
		case BUS:
			if (busParkingSpots == 0)
				return null;
			busParkingSpots--;
			break;
		default:
			return null;
		}

		int nextAvaiableParkingSpot = 1;
		while (parkedVehicles.containsKey(nextAvaiableParkingSpot)) {
			nextAvaiableParkingSpot++;
		}
		parkedVehicles.put(nextAvaiableParkingSpot, vehicle);
		
		return new Ticket(nextAvaiableParkingSpot, vehicle.getEntryDateTime());
	}

	public Receipt unpark(Ticket ticket, LocalDateTime exitDateTime) {
		Vehicle vehicle = parkedVehicles.get(ticket.getSpotNo());

		switch (vehicle.getType()) {

		case MOTORCYCLE:
			motorcycleParkingSpots++;
			break;
		case CAR:
			carParkingSpots++;
			break;
		case BUS:
			busParkingSpots++;
			break;
		default:
			return null;
		}

		parkedVehicles.remove(ticket.getSpotNo());

		return new Receipt(ticket, exitDateTime, feeModel.calculateFee(vehicle, exitDateTime));
	}

}
