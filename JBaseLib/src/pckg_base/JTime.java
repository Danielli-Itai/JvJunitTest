package pckg_base;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;





public class JTime {
	
	public static long  DateTimeDuration(int year, Month month, int day) {
		LocalDateTime from_date = LocalDateTime.of(year,month, day, 0, 0);
		LocalDateTime now = LocalDateTime.now();
	
		long months = ChronoUnit.MONTHS.between(from_date, now);
		return months;
	}
}
