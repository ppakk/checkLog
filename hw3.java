

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
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
public class hw3 {

  public static void main(String[] args) {

	  Pattern linkPattern = Pattern.compile("<link(.*?)/>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	  //Matcher linkMatcher = linkPattern.matcher(contents);
		
    String body= "<a href='http://www.denverpost.com/Entertainment/ci_27662953/Review:-%22Big-Fish%22-lands-well'><span id='title_1' class='l'><span class='goog-trans-section l' transId='gadget_1'>Theater review <b>...</b></span></span></a>  href='http://www.mercnews.com/ci_123/Junky'";

    System.out.println(body);

	//body = body.replaceAll("(http([s])?://)?(www\\.)?([\\w-.@]*\\.(?i)"+ext+")([\\w/]+(\\.[A-Za-z]{3,4})?)?([\\w=?&-]+)?", ("<a href=\"http$2://$3$4$6\">$1$3$4$6</a>".toLowerCase()));
	//body = body.replaceAll("(http([s])?://)?(www\\.denverpost\\.com)?([\\w='>]+)?", "$1$2");    
    //Pattern urlPattern = Pattern.compile("href='([^'\">]+)'");
    Pattern urlPattern = Pattern.compile("href='http://www.([^'\">]+)'");
    
    //int last = 0;
    Matcher m2 = urlPattern.matcher(body);
    while (m2.find()) {
        if (m2.find()) {
            String dir = m2.group(1);      
            if (dir != null) {
                body = body.replaceAll(dir, dir.toLowerCase());
            }

        }
    }
    
    System.out.println(body);
/*
    Pattern p = Pattern.compile("(?:http://)?.+?(/.+?)?/\\d+/\\d{2}/\\d{2}(/.+?)?/\\w{22}");

    String[] strings ={
            "www.examplesite.com/dir1/dir2/4444/2012/06/19/title-of-some-story/FAQKZjC3veXSalP9zxFgZP/htmlpage.html",
            "www.examplesite.com/2012/06/19/title-of-some-story/FAQKZjC3veXSalP9zxFgZP/htmlpage.html",
            "www.examplesite.com/dir/2012/06/19/title-of-some-story/FAQKZjC3veXSalP9zxFgZP/htmlpage.html",
            "www.examplesite.com/dir/2012/06/19/FAQKZjC3veXSalP9zxFgZP/htmlpage.html",
            "www.examplesite.com/2012/06/19/FAQKZjC3veXSalP9zxFgZP/htmlpage.html"
    };
    for (int idx = 0; idx < strings.length; idx++) {
        Matcher m = p.matcher(strings[idx]);
        if (m.find()) {
            String dir = m.group(1);
            String title = m.group(2);
            if (title != null) {
                title = title.substring(1); // remove the leading /
            }
//            System.out.println(idx+": Dir: "+dir+", Title: "+title);
        }
    }
*/
    

    String text = "<a href='http://www.denverpost.com/Entertainment/ci_27662953/Review:-%22Big-Fish%22-lands-well'><span id='title_1' class='l'><span class='goog-trans-section l' transId='gadget_1'>Theater review <b>...</b></span></span></a>  href='http://www.mercnews.com/ci_123/Junky'";
    //Matcher m = Pattern.compile("\\b\\w{3,}\\b").matcher(text);
    //Matcher m = Pattern.compile("(http([s])?://)?(www\\.denverpost\\.com)?").matcher(text);
    Matcher m = Pattern.compile("href='http://www.([^'\">]+)'").matcher(text);

    StringBuffer sb = new StringBuffer();
    int last = 0;
    while (m.find()) {
        sb.append(text.substring(last, m.start()));
        sb.append(m.group(0).toLowerCase());
        last = m.end();
    }
    sb.append(text.substring(last));

    System.out.println(sb.toString());

  }

}