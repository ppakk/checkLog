

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.mngi.openframework.dao.registration.CryptUtil;

/*
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;

import com.mngi.atgframework.logging.LogActivityEvent;
import com.mngi.postingService.exception.NoArticleBodyException;
import com.mngi.postingService.exception.NoArticleTitleException;
*/

/**
* Scheduled Execution to Post Articles. 
*/
public class hw1 {

  public static void main(String[] args) {

	  Pattern linkPattern = Pattern.compile("<link(.*?)/>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	  //Matcher linkMatcher = linkPattern.matcher(contents);
	
	String c = "Last call for Cirque du Soleil’s “Kurios”  “Kurios – Cabinet of Curiosities.”";
	c = "Quoted: Being a Thiel fellow — it’s complicated";
    System.out.println(c);
    c = c.replaceAll("’", "'");
	c = c.replaceAll("‘", "'");
	c = c.replaceAll("“", "\"");
	c = c.replaceAll("”", "\"");
	c = c.replace( (char)8221, (char)'\"'); // right double
	c = c.replaceAll("–", "-");
    System.out.println(c);
    
    Calendar	cal = Calendar.getInstance();
    System.out.println(cal.toString());
    
	/*
    StringBuffer body = new StringBuffer();
    String theLink = "http://www.amazon.com";
    String originatingText = "amazon";
    
//  body.append("<br><br>Read the full story here: <a href=\"" + theLink + "\">" + originatingText + "</a><br><br>");
//    body.append("<br><br>Read the full story here: <a href=\"" + theLink + "\"" + "onclick=\"var s=s_gi(s_account); s.linkTrackVars=\'prop31\';s.prop31=s.pageName;s.t1(this,\'e\',\'OneSpot - " + originatingText + "\');\">" );

    body.append("<br><br>Read the full story here: <a href=\"" + theLink + "\"" + " onclick=\"var s=s_gi(s_account); s.linkTrackVars=\'prop31\';s.prop31=s.pageName;s.tl(this,\'e\',\'OneSpot - " + originatingText + "\');\">" + originatingText + "</a><br><br>");
    
//               <br><br>Read the full story here: <a href="http://newstalk1290.wordpress.com">Newstalk1290 KPAY</a><br><br><div id="onespot_widget"></div><br style="clear:both;"/><br style="clear:both;"/><br style="clear:both;"/><br style="clear:both;"/><br style="clear:both;"/><br style="clear:both;"/><br style="clear:both;"/></div><div class="articlePositionFooter"></div><span class="articleFooterLinks"><div class="articleOptions"><a href="javascript:void(0);" class="articleOptions"><img src="http://extras.mnginteractive.com/dev/std/icon-print.gif" border='0'></a><a href="javascript:void(0);" class="articleOptions">Print</a>&nbsp;&nbsp;

	StringBuffer z = body;
	//System.out.println(body);
	
	
	Calendar	c = Calendar.getInstance();
	
	long twoWeekBefore = 60 * 60 * 24 * 14 * 1000;
	Date d1 = new Date(c.getTimeInMillis() - twoWeekBefore);
	System.out.println(d1);
	
	String	l = "catId";
	String link = "http://www.a.com/localnews/ci_123";
	link = link.substring(0,link.indexOf("/ci_")) + ("/" + l + "/" + link.substring(link.indexOf("/ci_"))).replaceAll("//","/");
	
	String pw = null;
	try {
		pw = CryptUtil.instance().hash("password1");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println(link);
	
	int pos = 0;
	String s1 = " { \"priority\": \"" + pos+1 + "\", \"type\": \"poll\", \"id\": ";
	String s2 = s1;
	System.out.println(s2);
	
    Date d = new Date();
    String s = d.toString();
    Date d2 = new Date(s);
    System.out.println("d2 = " + d2);
    
    
    String s22 = "Broncos DUI arrests \"bigger than football\" as NFL penalty\" looms";
    s22 = s22.replaceAll("\"", "&quot;");
    */

    		


  }

}