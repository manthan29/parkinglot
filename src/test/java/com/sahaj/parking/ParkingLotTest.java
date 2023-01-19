package com.sahaj.parking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
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
import com.sahaj.parking.vehicle.VehicleEntry;
import com.sahaj.parking.vehicle.VehicleType;

class ParkingLotTest {

	@BeforeEach
	void init(TestInfo testInfo) {
		System.out.println(testInfo.getDisplayName());
	}

	@AfterEach
	void conclude() {
		System.out.println("===============");
	}

	@Test
	void mallCalculateFeeTest() {
		MallFeeModel mallFeeModel = MallFeeModel.getInstance();

		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		int fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28)).get();
		assertEquals(10, fee);

		vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28)).get();
		assertEquals(20, fee);

		vehicle = new VehicleEntry(new Vehicle(VehicleType.BUS), LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28)).get();
		assertEquals(50, fee);

		vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(128)).get();
		assertEquals(30, fee);

		vehicle = new VehicleEntry(new Vehicle(VehicleType.BUS), LocalDateTime.now());
		fee = mallFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(128)).get();
		assertEquals(150, fee);
	}

	@Test
	void mallParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.MALL, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(28)).get();
		System.out.println(receipt);
		assertEquals(10, receipt.getFee());
	}

	@Test
	void stadiumParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));

		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(220)).get();
		System.out.println(receipt);
		assertEquals(30, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899)).get();
		System.out.println(receipt);
		assertEquals(390, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(690)).get();
		System.out.println(receipt);
		assertEquals(180, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(785)).get();
		System.out.println(receipt);
		assertEquals(580, receipt.getFee());

	}

	@Test
	void airportParkUnparkTest() {
		reset();
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));

		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(55)).get();
		System.out.println(receipt);
		assertEquals(0, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899)).get();
		System.out.println(receipt);
		assertEquals(60, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1452)).get();
		System.out.println(receipt);
		assertEquals(160, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(50)).get();
		System.out.println(receipt);
		assertEquals(60, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1439)).get();
		System.out.println(receipt);
		assertEquals(80, receipt.getFee());

		vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(4380)).get();
		System.out.println(receipt);
		assertEquals(400, receipt.getFee());

	}

	@Test
	void overbookingTest() {
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		ParkingLot parkingLot = new ParkingLot(FeeModelType.MALL, getParkingSpots(20, 3, 0));
		Ticket ticket = parkingLot.park(vehicle).get();
		parkingLot.park(vehicle).get();
		Ticket ticket3 = parkingLot.park(vehicle).get();
		Optional<Ticket> ticket4 = parkingLot.park(vehicle);
		assertTrue(ticket4.isEmpty());
		parkingLot.unpark(ticket, LocalDateTime.now().plusMinutes(200));
		parkingLot.park(vehicle).get();
		parkingLot.unpark(ticket3, LocalDateTime.now().plusMinutes(200));
		parkingLot.park(vehicle).get();
		Optional<Ticket> ticket7 = parkingLot.park(vehicle);
		assertTrue(ticket7.isEmpty());
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
			// create the appropriate vehicle entry object
			VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.valueOf(vehicleType)), entryTime);

			ParkingLot parkingLot = new ParkingLot(FeeModelType.valueOf(locationType),
					getParkingSpots(motorcycleSpots, carSpots, busSpots));
			parkingLot.park(vehicle).ifPresentOrElse(ticket -> {
				System.out.println(ticket);
				Receipt receipt = parkingLot.unpark(ticket, exitTime).get();
				System.out.println(receipt);
				assertEquals(expectedFee, receipt.getFee());
			}, () -> assertEquals(expectedFee, -1));

		}
	}

	private HashMap<VehicleType, Integer> getParkingSpots(int motorcycleSpots, int carSpots, int busSpots) {
		HashMap<VehicleType, Integer> parkingSpots = new HashMap<VehicleType, Integer>();
		parkingSpots.put(VehicleType.MOTORCYCLE, motorcycleSpots);
		parkingSpots.put(VehicleType.CAR, carSpots);
		parkingSpots.put(VehicleType.BUS, busSpots);
		return parkingSpots;
	}

	private void reset() {
		Ticket.resetCounter();
		Receipt.resetCounter();
	}

}
