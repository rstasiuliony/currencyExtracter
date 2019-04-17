package validators;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateValidator {
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private DateValidator() {}
	
	public static boolean isDateFormatValid(String date) {
		df.setLenient(false);
        try {
            df.parse(date);
            return true;
        } catch (ParseException e) {
            System.out.println("Neteisingai ávesta data.");
        }
		return false;
	}

	public static boolean isStartDateBeforeEndDate(String startDate, String endDate) {
		df.setLenient(false);
		try {
			Date date1 = df.parse(startDate);
	        Date date2 = df.parse(endDate);	
	        if (date1.compareTo(date2) < 0 || date1.compareTo(date2) == 0) {
		        return true;
	        }
		} catch (ParseException e){
			System.out.println("Ávyko datø apdorojimo klaida.");
			return false;
		}
		System.out.println("Pabaigos data negali bûti anksèiau uþ pradþios datà.");
		return false;
	}

	public static boolean isDateInLitas(String date) {
		df.setLenient(false);
		try {
			Date d = df.parse(date);
			Date litasDate = df.parse("2014-12-31");
			if(d.before(litasDate) || d.equals(litasDate)) {
				System.out.println("Data negali bûti ankstesnë uþ 2015-01-01.");
				return false;
			} 
		} catch (ParseException e) {
		}
		return true;
	}
}
