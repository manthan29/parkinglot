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

import com.sahaj.parking.fee.AirportFeeModel;
import com.sahaj.parking.fee.FeeModelType;
import com.sahaj.parking.fee.StadiumFeeModel;
import com.sahaj.parking.ticket.Receipt;
import com.sahaj.parking.ticket.Ticket;
import com.sahaj.parking.vehicle.Vehicle;
import com.sahaj.parking.vehicle.VehicleEntry;
import com.sahaj.parking.vehicle.VehicleType;

class ParkingLotTest {

	@BeforeEach
	void init(TestInfo testInfo) {
		reset();
		System.out.println(testInfo.getDisplayName());
	}

	@AfterEach
	void conclude() {
		System.out.println("===============");
	}

	@Test
	void stadiumBusCalculateFeeTest() {
		StadiumFeeModel stadiumFeeModel = StadiumFeeModel.getInstance();
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.BUS), LocalDateTime.now());
		assertTrue(stadiumFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28)).isEmpty());
	}
	
	@Test
	void airportBusCalculateFeeTest() {
		AirportFeeModel airportFeeModel = AirportFeeModel.getInstance();
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.BUS), LocalDateTime.now());
		assertTrue(airportFeeModel.calculateFee(vehicle, LocalDateTime.now().plusMinutes(28)).isEmpty());
	}

	@Test
	void mallMotorcycle28MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.MALL, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(28)).get();
		System.out.println(receipt);
		assertEquals(10, receipt.getFee());
	}

	@Test
	void stadiumMotorcycle220MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(220)).get();
		System.out.println(receipt);
		assertEquals(30, receipt.getFee());
	}

	@Test
	void stadiumCar690MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(690)).get();
		System.out.println(receipt);
		assertEquals(180, receipt.getFee());
	}

	@Test
	void stadiumMotorcycle390MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899)).get();
		System.out.println(receipt);
		assertEquals(390, receipt.getFee());
	}

	@Test
	void stadiumCar580MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(785)).get();
		System.out.println(receipt);
		assertEquals(580, receipt.getFee());
	}

	@Test
	void airportCar55MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(55)).get();
		System.out.println(receipt);
		assertEquals(0, receipt.getFee());
	}

	@Test
	void airportMotorcycle899MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(899)).get();
		System.out.println(receipt);
		assertEquals(60, receipt.getFee());
	}

	@Test
	void airportMotorcycle1452MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1452)).get();
		System.out.println(receipt);
		assertEquals(160, receipt.getFee());
	}

	@Test
	void airportMotorcycle50MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(50)).get();
		System.out.println(receipt);
		assertEquals(60, receipt.getFee());
	}

	@Test
	void airportCar1439MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(1439)).get();
		System.out.println(receipt);
		assertEquals(80, receipt.getFee());
	}

	@Test
	void airportCar4380MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(4380)).get();
		System.out.println(receipt);
		assertEquals(400, receipt.getFee());

	}

	@Test
	void overbookingTest() {
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		ParkingLot parkingLot = new ParkingLot(FeeModelType.MALL, getParkingSpots(20, 3, 0));
		parkingLot.park(vehicle).get();
		parkingLot.park(vehicle).get();
		parkingLot.park(vehicle).get();
		Optional<Ticket> ticket4 = parkingLot.park(vehicle);
		assertTrue(ticket4.isEmpty());
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

	@Test
	void stadiumBusTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.BUS), LocalDateTime.now());
		assertTrue(parkingLot.park(vehicle).isEmpty());
	}
	
	@Test
	void airportBusTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.BUS), LocalDateTime.now());
		assertTrue(parkingLot.park(vehicle).isEmpty());
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
