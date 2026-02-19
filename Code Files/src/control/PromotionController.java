package control;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Promotion;

public class PromotionController {
	private static final int DAY = 1;
	private static int shownForSession = 0;
	
	public boolean isPromotionReleaseDay() {
        // Returns true if it's the 1st of the month
		if (shownForSession == 1) {return false;}
		
        return LocalDate.now().getDayOfMonth() == DAY;
    }
	
	public ArrayList<Promotion> getMonthlyPromotion(){
		ArrayList<Promotion> promotions = new ArrayList<>();
		int month = LocalDate.now().getMonthValue();
		
		switch(month) {
			case 1:
				promotions.add(
						new Promotion("New Years' Express", 
								"50% discount for all flights booked within the first week of January!",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
			case 2:
				promotions.add(
						new Promotion("Valentine's Special", 
								"Book 2 flights, get a trip to Ibiza for you and 1 guest!",
								LocalDate.now().withDayOfMonth(1))
						);
				break;

			case 4:
				promotions.add(
						new Promotion("Easter Bunny Express!", 
								"Each passenger will have an chocolate egg on their seat which has a chance to "
								+ "contain a free trip voucher",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
			case 5:
				promotions.add(
						new Promotion("Mother's Day Bonanza", 
								"Any flight booked on mothers day is 20% off. Show your mum some love!",
								LocalDate.now().withDayOfMonth(1))
						);
				
				promotions.add(
						new Promotion("Spring Fever", 
								"All flights are 30% off for the first week in May.",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
			case 7:
				promotions.add(
						new Promotion("Summer Bash", 
								"All flights to and from Miami are 30% off",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
				
			case 8:
				promotions.add(
						new Promotion("Summer Bash Part 2", 
								"All flights to and from Hawaii are 70% off",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
			case 9:
				promotions.add(
						new Promotion("Back To School!!", 
								"All students get 40% off all flight bookings",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
			case 11:
				promotions.add(
						new Promotion("Pre-Holiday Fever", 
								"Family Trips are 50% off!",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
			case 12:
				promotions.add(
						new Promotion("Santa's Little Helpers", 
								"Book 5 flights, get 2 free!",
								LocalDate.now().withDayOfMonth(1))
						);
				break;
		}
		
		return promotions;
	}
	
	public void setShownForSession() {
		shownForSession = 1;
	}
}
