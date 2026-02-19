package boundary.utility;

public class IntegerValidator {
	public static boolean validateInteger(String number) {
		boolean result = true;
		
		if (!isInteger(number)) {result = false;}
		
		return result;
	}
	
	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
