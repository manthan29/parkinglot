package com.sahaj.parking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.sahaj.parking.fee.FeeModelType;
import com.sahaj.parking.fee.MallFeeModel;
import com.sahaj.parking.ticket.Receipt;
import com.sahaj.parking.ticket.Ticket;
import com.sahaj.parking.vehicle.Vehicle;
import com.sahaj.parking.vehicle.VehicleType;

class ParkingLotTest {

	// @Test
	void mallCalculateFeeTest() {
		MallFeeModel mallFeeModel = new MallFeeModel();

		Vehicle vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		int fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28));
		assertEquals(10, fee);

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28));
		assertEquals(20, fee);

		vehicle = new Vehicle(VehicleType.BUS, LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28));
		assertEquals(50, fee);

		vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(128));
		assertEquals(30, fee);

		vehicle = new Vehicle(VehicleType.BUS, LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(128));
		assertEquals(150, fee);
	}

	@Test
	void mallParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.MALL, 10, 10, 10);
		Vehicle vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle);
		Ticket expectedTicket = new Ticket(1, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(28));
		Receipt expectedReceipt = new Receipt(1, ticket, vehicle.getEntryDateTime().plusMinutes(28), 10);
		assertEquals(expectedReceipt, receipt);
	}

	@Test
	void stadiumParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, 10, 10, 10);

		Vehicle vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle);
		Ticket expectedTicket = new Ticket(1, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(220));
		Receipt expectedReceipt = new Receipt(1, ticket, vehicle.getEntryDateTime().plusMinutes(220), 30);
		assertEquals(expectedReceipt, receipt);

		vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(2, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899));
		expectedReceipt = new Receipt(2, ticket, vehicle.getEntryDateTime().plusMinutes(899), 390);
		assertEquals(expectedReceipt, receipt);

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(3, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(690));
		expectedReceipt = new Receipt(3, ticket, vehicle.getEntryDateTime().plusMinutes(690), 180);
		assertEquals(expectedReceipt, receipt);

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(4, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(785));
		expectedReceipt = new Receipt(4, ticket, vehicle.getEntryDateTime().plusMinutes(785), 580);
		assertEquals(expectedReceipt, receipt);

	}

	@Test
	void airportParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, 10, 10, 10);

		Vehicle vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle);
		Ticket expectedTicket = new Ticket(1, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(55));
		Receipt expectedReceipt = new Receipt(1, ticket, vehicle.getEntryDateTime().plusMinutes(55), 0);
		assertEquals(expectedReceipt, receipt);

		vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(2, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899));
		expectedReceipt = new Receipt(2, ticket, vehicle.getEntryDateTime().plusMinutes(899), 60);
		assertEquals(expectedReceipt, receipt);

		vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(3, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1452));
		expectedReceipt = new Receipt(3, ticket, vehicle.getEntryDateTime().plusMinutes(1452), 160);
		assertEquals(expectedReceipt, receipt);

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(4, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(50));
		expectedReceipt = new Receipt(4, ticket, vehicle.getEntryDateTime().plusMinutes(50), 60);
		assertEquals(expectedReceipt, receipt);

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(5, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1439));
		expectedReceipt = new Receipt(5, ticket, vehicle.getEntryDateTime().plusMinutes(1439), 80);
		assertEquals(expectedReceipt, receipt);
		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		expectedTicket = new Ticket(6, 1, vehicle.getEntryDateTime());
		assertEquals(expectedTicket, ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(4380));
		expectedReceipt = new Receipt(6, ticket, vehicle.getEntryDateTime().plusMinutes(4380), 400);
		assertEquals(expectedReceipt, receipt);

	}
	@Test
	void overbookingTest() {
		Vehicle vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ParkingLot parkingLot = new ParkingLot(FeeModelType.MALL, 20, 3, 0);
		Ticket ticket = parkingLot.park(vehicle);
		Ticket ticket2 = parkingLot.park(vehicle);
		Ticket ticket3 = parkingLot.park(vehicle);
		Ticket ticket4 = parkingLot.park(vehicle);
		assertEquals(null, ticket4);
		Receipt receipt = parkingLot.unpark(ticket, LocalDateTime.now().plusMinutes(200));
		Ticket ticket5 = parkingLot.park(vehicle);
		Receipt receipt2 = parkingLot.unpark(ticket3, LocalDateTime.now().plusMinutes(200));
		Ticket ticket6 = parkingLot.park(vehicle);
		Ticket ticket7 = parkingLot.park(vehicle);
		assertEquals(null, ticket7);
	}

	@Test
	void testFromFile() throws FileNotFoundException {
		// open the file containing the test cases
		Scanner sc = new Scanner(new File("src/test/resources/test.txt"));
		sc.nextLine();
		// read each line from the file and parse the test case
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] parts = line.split(" ");
			String locationType = parts[0];
			int motorcycleSpots = Integer.parseInt(parts[1]);
			int carSpots = Integer.parseInt(parts[2]);
			int busSpots = Integer.parseInt(parts[3]);
			String vehicleType = parts[4];
			int entryHour = Integer.parseInt(parts[5]);
			int entryMinute = Integer.parseInt(parts[6]);
			int exitHour = Integer.parseInt(parts[7]);
			int exitMinute = Integer.parseInt(parts[8]);
			int expectedFee = Integer.parseInt(parts[9]);

			LocalDateTime entryTime = LocalDateTime.of(2022, 1, 1, entryHour, entryMinute);
			LocalDateTime exitTime = LocalDateTime.of(2022, 1, 1, exitHour, exitMinute);
			// create the appropriate vehicle object
			Vehicle vehicle;
			if (vehicleType.equals("motorcycle")) {
				vehicle = new Vehicle(VehicleType.MOTORCYCLE, entryTime);
			} else if (vehicleType.equals("car")) {
				vehicle = new Vehicle(VehicleType.CAR, entryTime);
			} else if (vehicleType.equals("bus")) {
				vehicle = new Vehicle(VehicleType.BUS, entryTime);
			} else {
				continue;
			}
			ParkingLot parkingLot = new ParkingLot(FeeModelType.valueOf(locationType), motorcycleSpots, carSpots, busSpots);
			Ticket ticket = parkingLot.park(vehicle);
			Receipt receipt = parkingLot.unpark(ticket, exitTime);
			assertEquals(expectedFee, receipt.getFee());

		}
	}

	private void reset() {
		Ticket.resetCounter();
		Receipt.resetCounter();
	}
}
