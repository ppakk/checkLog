

import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import org.xml.sax.InputSource;

import com.mngi.openframework.geo.beans.MapLocation;
import com.mngi.openframework.geo.proxy.MapLocationSet;



/**
* Scheduled Execution to Post Articles. 
*/
public class domParse {

  public static void main(String[] args) {

	  getSetFromXml("");

  }

  public static Document parse(URL url) throws DocumentException {
      SAXReader reader = new SAXReader();
      Document document = reader.read(url);
      return document;
  }
  
  private static MapLocationSet getSetFromXml(String urlInput) {

    MapLocationSet mapLocationSet = null;

    File file = new File("c:\\MyXMLFile.xml");

    URL url = null;
	try {
		url = new URL("http://directory.denverpost.com/marketplace/api/v1/ads/fullads/near/?format=xml&hascoupons=true&start_date__lte=2010-12-08&end_date__gte=2010-12-08&lat=39.7&long=-104.9&rad=23");
	} catch (MalformedURLException e) {
		e.printStackTrace();
	}
    
    try {
		Document doc = parse(url);
		
		Node node = doc.selectSingleNode( "//objects/object/headline" );
		//List n = doc.selectNodes( "//objects/object/headline" );
		List ids = doc.selectNodes( "//objects/object/id" );
		List uris = doc.selectNodes( "//objects/object/resource_uri" );
		List businessNames = doc.selectNodes( "//objects/object/business/name" );
		List headlines = doc.selectNodes( "//objects/object/headline" );
		List lats = doc.selectNodes( "//objects/object/latitude" );
		List longs = doc.selectNodes( "//objects/object/longitude" );
		
		
		
		String s1 = node.getStringValue();
		
		Element root = doc.getRootElement();

		for (int j=0; j< businessNames.size(); j++) {
			Element el = (Element) headlines.get(j);
			Element e2 = (Element) businessNames.get(j);
			System.out.println(el.getStringValue() +" - " + e2.getStringValue());
		}
        // iterate through child elements of root
		/*
        for ( Iterator i = n.iterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String s2 = element.getStringValue();
            String s3 = s2;
        }
        */

        // iterate through child elements of root with element name "foo"
        
        for ( Iterator i = root.elementIterator( "objects" ); i.hasNext(); ) {
            Element foo = (Element) i.next();
            String s2 = foo.getName();
            System.out.println(s2);
            String s3 = s2;
        }

	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


      return mapLocationSet;
        
  }
  
  /*
  private static String getTagValue(String sTag, Element eElement){
	 NodeList nlList= eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 Node nValue = (Node) nlList.item(0); 
	 
	 return nValue.getNodeValue();    
  }
  */

}