package com.sahaj.parking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.sahaj.parking.fee.FeeModelType;
import com.sahaj.parking.fee.MallFeeModel;
import com.sahaj.parking.ticket.Receipt;
import com.sahaj.parking.ticket.Ticket;
import com.sahaj.parking.vehicle.Vehicle;
import com.sahaj.parking.vehicle.VehicleType;

class ParkingLotTest {
	
	private TestInfo testInfo;

	@BeforeEach
	void init(TestInfo testInfo) {
	    this.testInfo = testInfo;
	    System.out.println(testInfo.getDisplayName());
	}
	
	@AfterEach
	void conclude() {
	    System.out.println("===============");
	}

	@Test
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
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(28));
		System.out.println(receipt);
		assertEquals(10, receipt.getFee());
	}

	@Test
	void stadiumParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, 10, 10, 10);

		Vehicle vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(220));
		System.out.println(receipt);
		assertEquals(30, receipt.getFee());

		vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899));
		System.out.println(receipt);
		assertEquals(390, receipt.getFee());

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(690));
		System.out.println(receipt);
		assertEquals(180, receipt.getFee());

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(785));
		System.out.println(receipt);
		assertEquals(580, receipt.getFee());

	}

	@Test
	void airportParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, 10, 10, 10);

		Vehicle vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(55));
		System.out.println(receipt);
		assertEquals(0, receipt.getFee());

		vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899));
		System.out.println(receipt);
		assertEquals(60, receipt.getFee());

		vehicle = new Vehicle(VehicleType.MOTORCYCLE, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1452));
		System.out.println(receipt);
		assertEquals(160, receipt.getFee());

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(50));
		System.out.println(receipt);
		assertEquals(60, receipt.getFee());

		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1439));
		System.out.println(receipt);
		assertEquals(80, receipt.getFee());
		
		vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ticket = parkingLot.park(vehicle);
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(4380));
		System.out.println(receipt);
		assertEquals(400, receipt.getFee());

	}
	@Test
	void overbookingTest() {
		Vehicle vehicle = new Vehicle(VehicleType.CAR, LocalDateTime.now());
		ParkingLot parkingLot = new ParkingLot(FeeModelType.MALL, 20, 3, 0);
		Ticket ticket = parkingLot.park(vehicle);
		parkingLot.park(vehicle);
		Ticket ticket3 = parkingLot.park(vehicle);
		Ticket ticket4 = parkingLot.park(vehicle);
		assertEquals(null, ticket4);
		parkingLot.unpark(ticket, LocalDateTime.now().plusMinutes(200));
		parkingLot.park(vehicle);
		parkingLot.unpark(ticket3, LocalDateTime.now().plusMinutes(200));
		parkingLot.park(vehicle);
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
			Vehicle vehicle = new Vehicle(VehicleType.valueOf(vehicleType), entryTime);
			ParkingLot parkingLot = new ParkingLot(FeeModelType.valueOf(locationType), motorcycleSpots, carSpots, busSpots);
			Ticket ticket = parkingLot.park(vehicle);
			System.out.println(ticket);
			Receipt receipt = parkingLot.unpark(ticket, exitTime);
			System.out.println(receipt);
			assertEquals(expectedFee, receipt.getFee());
			

		}
	}

	private void reset() {
		Ticket.resetCounter();
		Receipt.resetCounter();
	}
}
