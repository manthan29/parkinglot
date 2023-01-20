package com.sahaj.parking.fee;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sahaj.parking.exception.BootstrapLoadFailException;
import com.sahaj.parking.exception.ErrorCode;
import com.sahaj.parking.vehicle.VehicleType;

public class FeeRuleDAO {

	private static final String FEE_RULES_FILE_PATH = "src/main/res/bootstrap/fee_rules";
	private Map<FeeModelType, List<FeeRule>> feeRuleMap = new HashMap<>();
	
	private static FeeRuleDAO feeRuleReader;
	
	private FeeRuleDAO(String filePath) {
		super();
		loadFeeRulesFromFileByFeeModelType(filePath);
	}
	
	public static FeeRuleDAO getInstance() {
		if(feeRuleReader==null) {
			feeRuleReader = new FeeRuleDAO(FEE_RULES_FILE_PATH);
		}
		return feeRuleReader;
	}
	
	/**
	 * @param filePath To read the Fee Rules from a custom file
	 * @return singleton instance of FeeRuleReader
	 */
	public static FeeRuleDAO getInstance(String filePath) {
		if(feeRuleReader==null) {
			feeRuleReader = new FeeRuleDAO(filePath);
		}
		return feeRuleReader;
	}
	
	public void loadFeeRulesFromFileByFeeModelType(String filePath) {
		
		try (Scanner sc = new Scanner(new File(filePath));) {
			sc.nextLine();
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
				FeeModelType feeModelType = FeeModelType.valueOf(feeModel);
				FeeRule feeRule = new FeeRule(VehicleType.valueOf(vehicleType), minRange, maxRange, fee,
						FeeModelType.valueOf(feeModel), FeeRuleType.valueOf(feeType), cumulative);
				List<FeeRule> feeRules = feeRuleMap.get(feeModelType);
				if (feeRules == null) {
					feeRules = new ArrayList<>();
					feeRuleMap.put(feeModelType, feeRules);
				}
				feeRules.add(feeRule);
			}
		} catch (FileNotFoundException e) {
			throw new BootstrapLoadFailException(
					"Failed to load the FeeRule file from the following path : " + filePath, e,
					ErrorCode.FILE_NOT_FOUND);
		}
	}
	
	public void loadFeeRulesFromFileByFeeModelType() {
		loadFeeRulesFromFileByFeeModelType(FEE_RULES_FILE_PATH);
	}

	public List<FeeRule> getFeeRulesForFeeModelType(FeeModelType feeModelType) {
		return feeRuleMap.get(feeModelType);
	}

}
