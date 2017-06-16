
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Capture {

   //private static final Logger logger = LoggerFactory.getLogger(Capture.class);
   
   private static final String resource = "https://dfm.dev.janraincapture.com/";
   private static final String clientId = "mcw6tscpsmchwadwf9qvx6nmfk9js5nr";
   private static final String secret = "npxxf8mknyusw3tndajuyuvjn55u49eg";
   
   public static void main(String[] args) {
      String s1 = null;
      s1 = apiCall("entity",
            "access_token=mu5d4zewq3gpgej6&type_name=user",
            false);
      String s2 = s1;
   }

   /**
    * Capture API call
    * @param api what API you are calling, e.g. "oauth/token"
    * See http://developers.janrain.com/documentation/api/
    * @param apiArgs arguments if any taken by the API e.g. 
    * "code=ev6mc4qydjbskf"
    * @return a String containing the JSON output of the call
    */
   public static String  apiCall (String api, String apiArgs,
         boolean clientIdSecretAuthentication) {
      String result = "";

      String urlString = resource + api;
      String query = apiArgs;
      if (clientIdSecretAuthentication) {
         query += "&client_id=" + clientId + "&client_secret=" + secret;
      }
      
      urlString = "http://gsadev.medianewsgroup.com/search?q=fish&btnG=Google+Search&access=p&client=default_frontend&output=xml_no_dtd&proxystylesheet=default_frontend&sort=date%3AD%3AL%3Ad1&oe=UTF-8&ie=UTF-8&ud=1&exclude_apps=1&site=default_collection";

      try {
         URL url = new URL(urlString);
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setDoOutput(true);
         connection.setAllowUserInteraction(false);
         connection.setRequestMethod("GET");

         if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
               //System.out.println(inputLine);
               result = result + inputLine + "\n";
            }
            in.close();
         }
         connection.disconnect();

      } catch (Exception e) {
         System.out.println("Error calling the Janrain Capture API" +e);
      }
      System.out.println("result=" +result);
      return result;
   }
 
   
}
