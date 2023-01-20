package com.sahaj.parking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
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
import com.sahaj.parking.fee.FeeRuleDAO;
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
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildMallParkingLot(10, 10, 10);
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
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildStadiumParkingLot(10, 10);
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.MOTORCYCLE), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(minsParked)).get();
		System.out.println(receipt);
		assertEquals(expectedFee, receipt.getFee());
	}

	@Test
	void stadiumCar690MinutesTest() {
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildStadiumParkingLot(10, 10);
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.CAR), LocalDateTime.now());
		Ticket ticket = parkingLot.park(vehicle).get();
		System.out.println(ticket);
		Receipt receipt = parkingLot.unpark(ticket, vehicle.getEntryDateTime().plusMinutes(690)).get();
		System.out.println(receipt);
		assertEquals(180, receipt.getFee());
	}

	@Test
	void stadiumCar580MinutesTest() {
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildStadiumParkingLot(10, 10);
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
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildAirportParkingLot(10, 10);
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
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildAirportParkingLot(10, 10);
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
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildAirportParkingLot(3, 3);
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
		FeeModelType feeModelType = FeeModelType.valueOf(locationType);
		IParkingLot parkingLot;
		switch (feeModelType) {
		case MALL:
			parkingLot = new ParkingLot.ParkingLotBuilder().buildMallParkingLot(motorcycleSpots, carSpots, busSpots);
			break;
		case STADIUM:
			parkingLot = new ParkingLot.ParkingLotBuilder().buildStadiumParkingLot(motorcycleSpots, carSpots);
			break;
		case AIRPORT:
			parkingLot = new ParkingLot.ParkingLotBuilder().buildAirportParkingLot(motorcycleSpots, carSpots);
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + locationType);
		}

		parkingLot.park(vehicle).ifPresentOrElse(ticket -> {
			System.out.println(ticket);
			Receipt receipt = parkingLot.unpark(ticket, exitTime).get();
			System.out.println(receipt);
			assertEquals(expectedFee, receipt.getFee());
		}, () -> assertEquals(expectedFee, -1));
	}

	@Test
	void stadiumBusTest() {
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildStadiumParkingLot(10, 10);
		VehicleEntry vehicle = new VehicleEntry(new Vehicle(VehicleType.BUS), LocalDateTime.now());
		assertTrue(parkingLot.park(vehicle).isEmpty());
	}

	@Test
	void airportBusTest() {
		IParkingLot parkingLot = new ParkingLot.ParkingLotBuilder().buildAirportParkingLot(10, 10);
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
		FeeRuleDAO feeRuleReader = FeeRuleDAO.getInstance();
		assertThrows(BootstrapLoadFailException.class,
				() -> feeRuleReader.loadFeeRulesFromFileByFeeModelType("some_file"));
	}

	private void reset() {
		Ticket.resetCounter();
		Receipt.resetCounter();
	}

}
