

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.mngi.openframework.beans.*;
import com.mngi.openframework.beans.ObjToRdbms.*;
import com.mngi.openframework.logging.*;
import com.mngi.openframework.services.ServiceException;
import com.mngi.openframework.util.XMLDocument;

import java.util.regex.*;
import java.io.*;
import org.w3c.dom.*;

import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


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
public class bshTest {

  public static void main(String[] args) {

	  Pattern linkPattern = Pattern.compile("<link(.*?)/>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	  //Matcher linkMatcher = linkPattern.matcher(contents);
		
	String b1 = "[\\x00-\\x08]|[\\x0b-\\x1f]|[\\x7f-\\xff]";
    String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "+
"<nitf version=\"-//IPTC//DTD NITF 3.1//EN\">" +
"<!-- NITF Implementation Copyright 1999-2006 Mediaspan Software, LLC -->"+
"<!-- Content Syndication Server (CSS) -->"+
"<nitf><body><body.head><hedline><printhead></printhead><printdeck></printdeck><printsummary></printsummary><printinfobox></printinfobox>"+
"<printinfobox1></printinfobox1><printinfobox2></printinfobox2><printtagline></printtagline><printtable></printtable><webhead></webhead>"+
"<websummary></websummary></hedline></body.head><body.content>"+
"</body.content>el, Atlanta 36; BrWilson, Giants 35; Axford, Milwaukee 35; LNunez, Florida 32; HBell, San Diego 32.</p>hiladelphia 155.</p></p>"+
"</body></nitf>";
	String res = body.replaceAll(b1, " ");
	System.out.println(res);
	
	
	String bodycontent = "";

	try{
		// Open the file that is the first 
		// command line parameter
		FileInputStream fstream = new FileInputStream("C:\\emac19.xml");
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  //System.out.println (strLine);
		  bodycontent += strLine;
		}
		//Close the input stream
		in.close();
	}catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());
	}

	//check to see if image is present
	if(bodycontent.indexOf("<media media-type=\"image\">") >= 0){

		//Create an XML Document out of the bodycontents. to be revisitied with renaming image and moving
		XMLDocument parsedData = new XMLDocument();
		parsedData.createDocument(bodycontent);

		if(bodycontent.indexOf(".jpg") >= 0){
			//System.out.println("text: "+text);

			String credit = null;
			try {
				Node manifest = parsedData.getNode("nitf");

				NodeList nodeList = ((Element) manifest).getElementsByTagName("media");
				System.out.println("nums=" +nodeList.getLength()); 
				for(int i=0;i<nodeList.getLength();i++){
					Node node = nodeList.item(i);
					System.out.println("node "+node);
					Node mediaNode 	 = ((Element) node).getElementsByTagName("media-reference").item(0);
					Node captionNode = ((Element) node).getElementsByTagName("media.caption").item(0);
					Node creditNode  = ((Element) node).getElementsByTagName("media-iptc").item(0);
					
					if (captionNode != null) {
						Node firstC = captionNode.getFirstChild();
						String capt = firstC.getNodeValue();
						System.out.println("capt=" +capt);
						String s2 = capt;
					}
					System.out.println("mediaNode: "+mediaNode);
					System.out.println("captionNode: "+captionNode);
					System.out.println("creditNode: "+creditNode);
				}
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

  }
}