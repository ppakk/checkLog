

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
* Scheduled Execution to Post Articles. 
*/
public class coupondays {
  final static long MILLISECS_PER_DAY = 86400000L;

  public static void main(String[] args) {

	
	Calendar	c = Calendar.getInstance();
	
	System.out.println(getDaysLeft(c, "14 Dec 2010" ));
	System.out.println(getDaysLeft(c, "15 Dec 2010" ));
	System.out.println(getDaysLeft(c, "16 Dec 2010" ));
	System.out.println(getDaysLeft(c, "16 Dec 2011" ));
  }
  
  private static String getDaysLeft(Calendar cal, String endDateStr) {
  	// Find number of days from : endDateStr - cal and return value
  	String retVal = "";
  	if (endDateStr == null || endDateStr.length() < 1) {
  		return retVal;
  	}
  	try {
  		SimpleDateFormat dateFormat = new SimpleDateFormat ("dd MMM yyyy HH:mm:ss");
		Date endDate = dateFormat.parse(endDateStr + " 23:59:59");
		long diffTime = (endDate.getTime() - cal.getTimeInMillis());
		if (diffTime > 0) {
			long diffDays = (diffTime / MILLISECS_PER_DAY) + 1;
			retVal = String.valueOf(diffDays);
		}
	} catch (ParseException e) {
		e.printStackTrace();
	}
  	return retVal;
  }

}