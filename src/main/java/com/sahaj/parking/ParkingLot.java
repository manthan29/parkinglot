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

public class ParkingLot implements IParkingLot{

	private IFeeModel feeModel;
	private Map<Integer, VehicleEntry> vehicleEntries;
	private Map<VehicleType, Integer> parkingSpots;

	private ParkingLot(FeeModelType feeModelType, Map<VehicleType, Integer> parkingSpots) {
		this.parkingSpots = new HashMap<>();
		this.parkingSpots = parkingSpots;
		vehicleEntries = new HashMap<>();
		feeModel = FeeModelFactory.createFeeModel(feeModelType);
	}

	public Optional<Ticket> park(VehicleEntry vehicleEntry) {
		int spots = parkingSpots.getOrDefault(vehicleEntry.getVehicle().getVehicleType(), 0);
		if (spots == 0)
			return Optional.empty();
		else
			parkingSpots.put(vehicleEntry.getVehicle().getVehicleType(), spots - 1);

		int parkingSpot = getSpotNoForParking();
		vehicleEntries.put(parkingSpot, vehicleEntry);
		return Optional.of(new Ticket(parkingSpot, vehicleEntry.getEntryDateTime()));
	}

	public Optional<Receipt> unpark(Ticket ticket, LocalDateTime exitDateTime) {
		VehicleEntry vehicleEntry = vehicleEntries.get(ticket.getSpotNo());
		int spots = parkingSpots.get(vehicleEntry.getVehicle().getVehicleType());
		parkingSpots.put(vehicleEntry.getVehicle().getVehicleType(), spots + 1);
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

	public static class ParkingLotBuilder {

		public ParkingLot buildMallParkingLot(int motorcycleSpots, int carSpots, int busSpots) {
			HashMap<VehicleType, Integer> parkingSpots = new HashMap<>();
			parkingSpots.put(VehicleType.MOTORCYCLE, motorcycleSpots);
			parkingSpots.put(VehicleType.CAR, carSpots);
			parkingSpots.put(VehicleType.BUS, busSpots);
			// Stadium and Airport do not allow Bus parking
			return new ParkingLot(FeeModelType.MALL, parkingSpots);
		}

		public ParkingLot buildStadiumParkingLot(int motorcycleSpots, int carSpots) {
			HashMap<VehicleType, Integer> parkingSpots = new HashMap<>();
			parkingSpots.put(VehicleType.MOTORCYCLE, motorcycleSpots);
			parkingSpots.put(VehicleType.CAR, carSpots);
			// Stadium and Airport do not allow Bus parking
			return new ParkingLot(FeeModelType.STADIUM, parkingSpots);
		}

		public ParkingLot buildAirportParkingLot(int motorcycleSpots, int carSpots) {
			HashMap<VehicleType, Integer> parkingSpots = new HashMap<>();
			parkingSpots.put(VehicleType.MOTORCYCLE, motorcycleSpots);
			parkingSpots.put(VehicleType.CAR, carSpots);
			// Stadium and Airport do not allow Bus parking
			return new ParkingLot(FeeModelType.AIRPORT, parkingSpots);
		}

	}

}
