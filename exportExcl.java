

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
* Scheduled Execution to Post Articles. 
*/
public class exportExcl {
  final static long MILLISECS_PER_DAY = 86400000L;

  public static void main(String[] args) {

	boolean val = getExcl();
	boolean val2 = val;
  }
  
  private static boolean getExcl() {
	  String upper1 = "By Michael Liedtke, The Associated Press";
	  String upper = upper1.toUpperCase();
	  String [] exclusions = {"abc", "Associated Press ", "By Mihael Liedtke, The Associated Press", "s"};
	  for (int i=0; i < exclusions.length; i++) {
		  if (upper.indexOf(exclusions[i].toUpperCase()) != -1) {
			  System.out.println("-- " +i);
			  return true;
		  }
      }
	  return false;
  }

}