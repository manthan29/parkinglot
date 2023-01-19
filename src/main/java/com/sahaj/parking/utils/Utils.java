package com.sahaj.parking.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Utils {

	public static int intervalInHour(LocalDateTime entryTime, LocalDateTime exitTime) {
		int intervalInHours = (int) ChronoUnit.MINUTES.between(entryTime, exitTime);
		return (int) Math.ceil(intervalInHours / 60.0);
	}
	
	public static int calculateDays(int intervalInHours) {
		return intervalInHours / 24 + ((intervalInHours % 24 > 0) ? 1 : 0);
	}
}
