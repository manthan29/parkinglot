package com.sahaj.parking.fee;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sahaj.parking.utils.Utils;
import com.sahaj.parking.vehicle.VehicleEntry;
import com.sahaj.parking.vehicle.VehicleType;

public abstract class FeeModel implements IFeeModel {

	private List<FeeRule> feeRules;
	private FeeModelType feeModelType;

	protected FeeModel(FeeModelType feeModelType) {
		this.feeModelType = feeModelType;
		feeRules = fetchFeeRulesForModelType();
	}

	public Optional<Integer> calculateFee(VehicleEntry vehicleEntry, LocalDateTime exitDateTime) {
		int intervalInHours = Utils.intervalInHour(vehicleEntry.getEntryDateTime(), exitDateTime);
		Optional<FeeRule> feeRuleOpt = identifyFeeRuleType(intervalInHours, vehicleEntry.getVehicle().getVehicleType());
		if (!feeRuleOpt.isPresent())
			return Optional.empty();
		FeeRule feeRule = feeRuleOpt.get();
		if (feeRule.isCumulative()) {
			return Optional.ofNullable(cumulativeCalculation(feeRule, intervalInHours));
		} else {
			return Optional.ofNullable(nonCumulativeCalculation(feeRule, intervalInHours));
		}
	}

	private int nonCumulativeCalculation(FeeRule feeRule, int intervalInHours) {
		switch (feeRule.getFeeRuleType()) {
		case FLAT:
			return feeRule.getBaseFee();
		case HOURLY:
			return intervalInHours * feeRule.getBaseFee();
		case DAILY:
			return Utils.calculateDays(intervalInHours) * feeRule.getBaseFee();
		default:
			throw new IllegalArgumentException("Unexpected value: " + feeRule.getFeeRuleType());
		}
	}

	private int cumulativeCalculation(FeeRule feeRule, int intervalInHours) {
		int fee = 0;
		List<FeeRule> cumulativeFeeRules = fetchCumulativeFeeRules(feeRule);
		for (FeeRule fr : cumulativeFeeRules) {
			switch (fr.getFeeRuleType()) {
			case FLAT:
				fee = fee + fr.getBaseFee();
				break;
			case HOURLY:
				fee = fee + feeRule.getBaseFee() * remainingInterval(feeRule, intervalInHours);
				break;
			case DAILY:
				fee = fee + Utils.calculateDays(intervalInHours) * remainingInterval(feeRule, intervalInHours);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + feeRule.getFeeRuleType());
			}
		}
		return fee;
	}

	private int remainingInterval(FeeRule feeRule, int intervalInHours) {
		return intervalInHours - (int) feeRule.getHourRange().getMinimum();
	}

	protected Optional<FeeRule> identifyFeeRuleType(int intervalInHour, VehicleType vehicleType) {
		for (FeeRule feeRule : feeRules) {
			if (feeRule.getHourRange().isValidValue(intervalInHour) && feeRule.getVehicleType() == vehicleType)
				return Optional.of(feeRule);
		}
		return Optional.empty();
	}

	protected List<FeeRule> fetchCumulativeFeeRules(FeeRule feeRule) {
		return feeRules.stream()
				.filter(fr -> fr.getHourRange().getMinimum() <= feeRule.getHourRange().getMinimum()
						&& feeRule.getVehicleType() == fr.getVehicleType())
				.collect(Collectors.toList());
	}
	
	protected List<FeeRule> fetchFeeRulesForModelType() {
		FeeRuleDAO feeRuleReader = FeeRuleDAO.getInstance();
		return feeRuleReader.getFeeRulesForFeeModelType(feeModelType);
	}

}
