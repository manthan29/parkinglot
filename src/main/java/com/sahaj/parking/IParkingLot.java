package com.sahaj.parking;

import java.time.LocalDateTime;
import java.util.Optional;

import com.sahaj.parking.ticket.Receipt;
import com.sahaj.parking.ticket.Ticket;
import com.sahaj.parking.vehicle.VehicleEntry;

public interface IParkingLot {

	public Optional<Ticket> park(VehicleEntry vehicleEntry);

	public Optional<Receipt> unpark(Ticket ticket, LocalDateTime exitDateTime);
}
