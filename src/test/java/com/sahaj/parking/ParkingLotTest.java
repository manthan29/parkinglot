package com.sahaj.parking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import com.sahaj.parking.exception.BootstrapLoadFailException;
import com.sahaj.parking.fee.AirportFeeModel;
import com.sahaj.parking.fee.FeeModelFactory;
import com.sahaj.parking.fee.FeeModelType;
import com.sahaj.parking.fee.FeeRuleReader;
import com.sahaj.parking.fee.MallFeeModel;
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

	@ParameterizedTest
	@CsvSource({ "220,30", "899,390" })
	void stadiumMotorcycle220MinutesTest(int minsParked, int expectedFee) {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(minsParked)).get();
		System.out.println(receipt);
		assertEquals(expectedFee, receipt.getFee());
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
	void stadiumCar580MinutesTest() {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.STADIUM, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(785)).get();
		System.out.println(receipt);
		assertEquals(580, receipt.getFee());
	}

	@ParameterizedTest
	@CsvSource({ "55,0", "899,60", "1452,160" })
	void airportMotorcycle55MinutesTest(int minsParked, int expectedFee) {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(minsParked)).get();
		System.out.println(receipt);
		assertEquals(expectedFee, receipt.getFee());
	}

	@ParameterizedTest
	@CsvSource({ "50,60", "1439,80", "4380,400" })
	void airportCarTest(int minsParked, int expectedFee) {
		ParkingLot parkingLot = new ParkingLot(FeeModelType.AIRPORT, getParkingSpots(10, 10, 10));
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(minsParked)).get();
		System.out.println(receipt);
		assertEquals(expectedFee, receipt.getFee());
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

	@ParameterizedTest
	@CsvFileSource(resources = "/test.txt", numLinesToSkip = 1, delimiter = ' ')
	void testFromFile(String locationType, int motorcycleSpots, int carSpots, int busSpots, String vehicleType,
			int entryDay, int entryHour, int entryMinute, int exitDay, int exitHour, int exitMinute, int expectedFee) {

		LocalDateTime entryTime = LocalDateTime.of(2022, 1, entryDay, entryHour, entryMinute);
		LocalDateTime exitTime = LocalDateTime.of(2022, 1, exitDay, exitHour, exitMinute);
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

	@Test
	void feeModelFactoryMallTest() {
		assertTrue(FeeModelFactory.createFeeModel(FeeModelType.MALL) instanceof MallFeeModel);
	}

	@Test
	void feeModelFactoryStadiumTest() {
		assertTrue(FeeModelFactory.createFeeModel(FeeModelType.STADIUM) instanceof StadiumFeeModel);
	}

	@Test
	void feeModelFactoryAirportTest() {
		assertTrue(FeeModelFactory.createFeeModel(FeeModelType.AIRPORT) instanceof AirportFeeModel);
	}

	@Test
	void customFeeRuleFilePath() {
		reset();
		FeeRuleReader feeRuleReader = FeeRuleReader.getInstance();
		assertThrows(BootstrapLoadFailException.class,
				() -> feeRuleReader.loadFeeRulesFromFileByFeeModelType("some_file"));
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
