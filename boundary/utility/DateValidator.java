package boundary.utility;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator {
	
	public static boolean validateDate(String date) {
		if (date == null || date.trim().isEmpty()) {
            return false;
        }

        String regex = "^([0-2][0-9]|(3)[0-1])-(0[1-9]|1[0-2])-(\\d{4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date.trim());

        if (!matcher.matches()) {
            return false; 
        }

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6));

        if (month < 1 || month > 12) {
            return false;
        }

        // Validate actual day for the given month and year
        try {
            LocalDate parsedDate = LocalDate.of(year, month, day);
            LocalDate today = LocalDate.now();

            // ✅ Ensure the date is today or in the future
            if (parsedDate.isBefore(today)) {
                return false;
            }

            return true;
        } catch (DateTimeException e) {
            // Invalid day for the month (e.g. 31-02-2025)
            return false;
        }
	}
	
	public static boolean validateFormDate(String date) {
		if (date == null || date.trim().isEmpty()) {
            return false;
        }

        String regex = "^([0-2][0-9]|(3)[0-1])-(0[1-9]|1[0-2])-(\\d{4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date.trim());

        if (!matcher.matches()) {
            return false; 
        }

        return true;
	}
}
