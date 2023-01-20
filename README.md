# ParkingLot Test
Use "/parkinglot/src/test/resources/test.txt" to provide inputs to the test case.
Please note the following when giving the inputs.
1. first line is headers and will be skipped. Follow the headers for the order of input
2. Following Vehicletypes are supported: MOTORCYCLE, CAR, BUS (Case sensitive)
3. Following FeemodelTypes are supported: MALL, STADIUM, AIRPORT (Case sensitive)

# FeeRules File
The fee rules can be changed from the "/parkinglot/src/main/res/bootstrap/fee_rules" file
Please note the following when giving the fee rules.
1. First line is hearders and will be skipped. Follow the headers for the order of input.
2. Delimiter is " "(space)
3. Following VehicleTypes are supported: MOTORCYCLE, CAR, BUS (Case sensitive)
4. Following FeeRuleTypes are supported: HOURLY, DAILY, FLAT (Case sensitive)
5. Following FeemodelTypes are supported: MALL, STADIUM, AIRPORT (Case sensitive)

Concepts
1. The Feemodel and Vehciletype are easy to understand from the problem statement itself.
2. The FeeRules must be exclusive based on the following attributes. This is an important assumption.
    - FeeModelType (MALL, STADIUM, AIRPORT)
    - VehicleType (MOTORCYCLE, CAR, BUS)
    - minRange (the smaller value of the interval)
    - maxRange (the larger value of the interval)
    There should be no overlap in the intervals within same FeeModelType and VehicleType
3. Based on the above attributes, a feeRule is resolved and the rules are applied in the following manner
    - The fee rule can either be cumulative or non cumulative. 
    - If we resolve to a cumulative feerule, it will need all the feerules that have <= minrange than the resolved feerule. All the fees add up to give the final fee. 
    - For non cumulative, the feerule itself is enough to get the fee
    - There are 3 variants of the FeeRuleType
      1. FLAT: the value is applied as it is
      2. HOURLY: the vallue is multiplied by the interval or the remaining interval (in the case of cumulative feerule)
      3. DAILY: the vallue is multiplied by the number of days or the remaining number of days (in the case of cumulative feerule)
  
