package com.sahaj.parking.fee;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.sahaj.parking.vehicle.VehicleEntry;
import com.sahaj.parking.vehicle.VehicleType;

public abstract class FeeModel implements IFeeModel {

	private List<FeeRule> feeRules;
	private FeeModelType feeModelType;

	protected FeeModel(FeeModelType feeModelType) {
		this.feeModelType = feeModelType;
		feeRules = constructFeeRules();
	}

	private int intervalInHour(LocalDateTime entryTime, LocalDateTime exitTime) {
		int interval = (int) ChronoUnit.MINUTES.between(entryTime, exitTime);
		return (int) Math.ceil(interval / 60.0);
	}

	public Optional<Integer> calculateFee(VehicleEntry vehicleEntry, LocalDateTime exitDateTime) {

		int interval = intervalInHour(vehicleEntry.getEntryDateTime(), exitDateTime);
		int fee = 0;
		Optional<FeeRule> feeRuleOpt = identifyFeeRuleType(interval, vehicleEntry.getVehicle().getType());
		if (!feeRuleOpt.isPresent())
			return Optional.empty();
		FeeRule feeRule = feeRuleOpt.get();
		if (feeRule.isCumulative()) {
			fee = cumulativeCalculation(feeRule, interval);
		} else {
			fee = nonCumulativeCalculation(feeRule, interval);
		}
		return Optional.ofNullable(fee);

	}

	private int nonCumulativeCalculation(FeeRule feeRule, int interval) {
		switch (feeRule.getRuleType()) {
		case FLAT:
			return feeRule.getBaseFee();
		case HOURLY:
			return calculateFeeForHourly(feeRule, interval);
		case DAILY:
			return calculateFeeForDaily(feeRule, interval);
		default:
			throw new IllegalArgumentException("Unexpected value: " + feeRule.getRuleType());
		}
	}

	private int cumulativeCalculation(FeeRule feeRule, int interval) {
		int fee = 0;
		List<FeeRule> cumulativeFeeRules = fetchSortedCumulativeFeeRules(feeRule);
		for (FeeRule fr : cumulativeFeeRules) {
			switch (fr.getRuleType()) {
			case FLAT:
				fee = fee + fr.getBaseFee();
				break;
			case HOURLY:
				fee = fee + calculateFeeForHourly(feeRule, remainingInterval(feeRule, interval));
				break;
			case DAILY:
				fee = fee + calculateFeeForDaily(feeRule, remainingInterval(feeRule, interval));
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + feeRule.getRuleType());
			}
		}
		return fee;
	}

	private int remainingInterval(FeeRule feeRule, int interval) {
		return interval - (int) feeRule.getHourRange().getMinimum();
	}

	private int calculateFeeForHourly(FeeRule feeRule, int interval) {
		return feeRule.getBaseFee() * interval;
	}

	private int calculateFeeForDaily(FeeRule feeRule, int interval) {
		return calculateDays(interval) * feeRule.getBaseFee();
	}

	protected Optional<FeeRule> identifyFeeRuleType(int intervalInHour, VehicleType vehicleType) {
		for (FeeRule feeRule : feeRules) {
			if (feeRule.getHourRange().isValidValue(intervalInHour) && feeRule.getVehicleType() == vehicleType)
				return Optional.of(feeRule);
		}
		return Optional.empty();
	}

	protected List<FeeRule> fetchSortedCumulativeFeeRules(FeeRule feeRule) {
		return feeRules.stream()
				.filter(fr -> fr.getHourRange().getMinimum() <= feeRule.getHourRange().getMinimum()
						&& feeRule.getVehicleType() == fr.getVehicleType())
				.sorted((f1, f2) -> Long.compare(f1.getHourRange().getMinimum(), f2.getHourRange().getMinimum()))
				.collect(Collectors.toList());
	}

	protected List<FeeRule> constructFeeRules() {

		try (Scanner sc = new Scanner(new File("src/main/res/bootstrap/fee_rules"));) {
			sc.nextLine();
			feeRules = new ArrayList<>();
			// read each line from the file and parse the test case
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] parts = line.split(" ");
				String feeModel = parts[0];
				String vehicleType = parts[1];
				String feeType = parts[2];
				int minRange = Integer.parseInt(parts[3]);
				int maxRange = Integer.parseInt(parts[4]);
				int fee = Integer.parseInt(parts[5]);
				boolean cumulative = Boolean.parseBoolean(parts[6]);
				if (FeeModelType.valueOf(feeModel).equals(feeModelType)) {
					FeeRule feeRule = new FeeRule(VehicleType.valueOf(vehicleType), minRange, maxRange, fee,
							FeeModelType.valueOf(feeModel), FeeRuleType.valueOf(feeType), cumulative);
					feeRules.add(feeRule);
				}
			}
			return feeRules;
		} catch (FileNotFoundException e) {
			System.err.println("Could not reach file \"fee_rules\" and could not create the rules");
			return new ArrayList<>();
		}
	}

	private int calculateDays(int interval) {
		return interval / 24 + ((interval % 24 > 0) ? 1 : 0);
	}

}
