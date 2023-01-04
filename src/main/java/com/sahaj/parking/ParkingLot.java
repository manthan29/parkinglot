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
	private int twoWheelerParkingSpots;
	private int carParkingSpots;
	private int busParkingSpots;
	private HashMap<Integer, Vehicle> parkedVehicles;

	public ParkingLot(LocationType locationType, int twoWheelerParkingSpots, int carParkingSpots, int busParkingSpots) {
		this.busParkingSpots = busParkingSpots;
		this.carParkingSpots = carParkingSpots;
		this.twoWheelerParkingSpots = twoWheelerParkingSpots;
		parkedVehicles = new HashMap<>();
		feeModel = FeeModelFactory.createFeeModel(locationType);

	}

	public Ticket park(Vehicle vehicle) {
		switch (vehicle.getType()) {

		case TWOWHEELER:
			if (twoWheelerParkingSpots == 0)
				return null;
			twoWheelerParkingSpots--;
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

	public Receipt unpark(Ticket ticket) {
		Vehicle vehicle = parkedVehicles.get(ticket.getSpotNo());

		switch (vehicle.getType()) {

		case TWOWHEELER:
			twoWheelerParkingSpots++;
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

		return new Receipt(ticket, LocalDateTime.now(), feeModel.calculateFee(ticket.getSpotNo(), LocalDateTime.now()));
	}

}
