

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
public class hw2 {

  public static void main(String[] args) {

	  Pattern linkPattern = Pattern.compile("<link(.*?)/>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	  //Matcher linkMatcher = linkPattern.matcher(contents);
		
    String body = "To purchase tickets, visit bit.ly/1yVPdoP. end";  //1
           //body = "<p>To view a trailer for the film, visit bit.ly/sanjosedoc </p>"; //2
           //body = "<p>To view a trailer for the film, visit (bit.ly/sanjosedoc) </p>"; //3
           //body = "<p>To view a trailer for the film, visit bit.ly/sanjosedoc.  </p>"; //4
           //body = "<p>To view a trailer for the film, visit \"bit.ly/sanjosedoc.\"  </p>"; //5

    //body = body.replaceAll("(http([s])?://)?(www\\.)?([\\w-.@]*\\.(?i)"+ext+")([\\w/]+(\\.[A-Za-z]{3,4})?)?([\\w=?&-]+)?","<a href=\"http$2://$3$4$6$8\">$1$3$4$6$8</a>");

    //body = body.replaceAll("(bit.ly/?)","<a href=\"http$2://$3$4$6$8\">$1$3$4$6$8</a>");
    System.out.println(body);

    //body = body.replaceAll("(bit\\.ly[/]\\.*)", "<a href=\"http://$1\">$1</a>");
//    body = body.replaceAll("(bit\\.ly[/]\\[. \\)\n\"])", "<a href=\"http://$1\">$1</a>");
    /*
    String [] s1 = body.split("(bit\\.ly/[^. \\)\n\"]+[.])");
    char [] chtmp =  {'(', 'b', 'i', 't', '\\', '\\', '.', 'l', 'y', '/', '[', '^', '.', ' ', '\\', '\\', ')', '\\', 'n', '\\', '"', ']', '+', '[', '.', ']', ')' };
    int j2 = body.indexOf("(bit\\.ly/[^. \\)\n\"]+[.])");
    System.out.println("+++" +j2);
    if (s1 != null && s1.length > 0) {
       for (int j=0; j < s1.length; j++) {
           System.out.println("-- " +s1[j]);
       }
    }
    */
    //body = body.replaceAll("(bit\\.ly/[^. \\)\n\"]+[. \\)\n\"])", "<a href=\"http://$1\">$1</a>");

    body = body.replaceAll("(bit\\.ly/[^. \\)\n\"]+)", "<a href=\"http://$1\">$1</a>");
    //body = body.replaceAll("(<a href=\"http://bit\\.ly/[$.])">$1</a>");
    
    
    System.out.println(body);

  }

}