
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;
import java.util.Calendar;


//=======================================================================================
/*
/  This application monitors articles processed
/  by querying the DB2 table
*/
//=======================================================================================
public class chkWP implements DataSource {
   
  private String driver;
  private String URL;
  private String login;
  private String password;
   
  private static int articleCnt = 0;

  public chkWP(String driver, String URL, String login, String password){
    this.driver = driver;
    this.URL = URL;
    this.login = login;
    this.password = password;
  }
   
  public Connection getConnection() throws SQLException {
    try {
      Class.forName(this.driver);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new SQLException(e.getMessage());
    }
    return DriverManager.getConnection(URL, login, password);
  }

  public Connection getConnection(String arg0, String arg1) throws SQLException {
    return null;
  }

  public PrintWriter getLogWriter() throws SQLException {
    return null;
  }

  public int getLoginTimeout() throws SQLException {
    return 0;
  }

  public void setLogWriter(PrintWriter arg0) throws SQLException {
  }

  public void setLoginTimeout(int arg0) throws SQLException {
  }

  
  
  
  //------------------------------------------------------------------------------------
  // This method sends an email as an alert when an article does not process
  //
  public void sendMail( String message, String subj ) throws MessagingException {

    boolean debug = false;

    //Set the host smtp address
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.XYZ.com");

    // create some properties and get the default Session
    Session session = Session.getDefaultInstance(props, null);
    session.setDebug(debug);

    // create a message
    Message msg = new MimeMessage(session);

    // set the from and to address
    InternetAddress addressFrom = new InternetAddress("dev@XYZ.com");
    msg.setFrom(addressFrom);

    InternetAddress[] addressTo = new InternetAddress[1]; 
    addressTo[0] = new InternetAddress("x@XYZ.com");
   

    msg.setRecipients(Message.RecipientType.TO, addressTo);

    // Setting the Subject and Content Type
    msg.setSubject(subj);
    msg.setContent(message, "text/plain");

    Transport.send(msg);

  }


  //------------------------------------------------------------------------------------
  // This method querys the table and see if an article is not being processed
  // after a configurable period of timeLength
  //
  public void checkData( Connection connect ) throws Exception {


    //-----------------------------------------------------------------------------
    // current time
    //
    Calendar calendar = Calendar.getInstance();

    if (connect == null) {
      return;
    }

    Statement connectStatement = connect.createStatement();

    //2005-04-04 00:00:00.00
    StringBuffer dateStr = new StringBuffer(50);
    dateStr.append( calendar.get(Calendar.YEAR) + "-" );
    dateStr.append( (calendar.get(Calendar.MONTH)+1) + "-" );
    dateStr.append( calendar.get(Calendar.DAY_OF_MONTH) + " " +  calendar.get(Calendar.HOUR_OF_DAY) + ":" );
    if (calendar.get(Calendar.MINUTE) < 10) {
       dateStr.append("0");
    }
    dateStr.append(calendar.get(Calendar.MINUTE));

    String sqlStr1 = "SELECT CONTENT_GROUP_UID, GROUP_NAME, SITE_UID " +
      "FROM CONTENT_GROUP " +
      "WHERE lead_cicg_uid is not null and lead_cicg_uid  not in " +
      "(select CONTENT_ITEM_CONTENT_GROUP_UID from CONTENT_ITEM_CONTENT_GROUP where " +
      "CONTENT_ITEM_CONTENT_GROUP_UID =lead_cicg_uid) order by site_uid with ur";

    try {
      ResultSet rset = connectStatement.executeQuery(sqlStr1);

      //-----------------------------------------------------------------------------
      // Iterate through the result and fill the table
      //
      String createDate = "";
      Calendar cal = Calendar.getInstance();
      final long offset = 25200000;  // Denver 7 hours from GMT
      String log = "";
      String CONTENT_GROUP_UID  = "";

      while (rset.next())
      {
        CONTENT_GROUP_UID         = rset.getString("CONTENT_GROUP_UID");
        String GROUP_NAME         = rset.getString("GROUP_NAME");
        String SITE_UID           = rset.getString("SITE_UID");

        log = log + CONTENT_GROUP_UID +"|" + GROUP_NAME + "|" + SITE_UID + '\r' + '\n'; 
        articleCnt++;
      }

      if (articleCnt < 1) {
        log = "No articles loaded.";
        System.out.println(dateStr);
      } else {
        // send log as email
        sendMail(log, "Dashbd DisplayGroup null " +dateStr);
        Statement updateStatement = connect.createStatement();
        String sqlDel = "update ngps.CONTENT_GROUP set LEAD_CICG_UID = null where CONTENT_GROUP_UID='" + CONTENT_GROUP_UID + "'";
        int updateStat = updateStatement.executeUpdate(sqlDel);
        System.out.println("update stat=" +updateStat);
        updateStatement.close();
      }

      rset.close();
    } catch (SQLException sqlex) {
      sqlex.printStackTrace();
      System.out.println("SQLException " + sqlex.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out.println("Exception " + ex.toString());
    }
    connectStatement.close();

  } // checkData()



  //------------------------------------------------------------------------------------
  // main
  //
  public static void main(String args []) throws Exception {

    /*
    if (args == null || args.length != 4) {
      System.out.println("Wrong args");
      System.exit(-1);
    }
    */

    Connection connection = null;


    chkWP ds = new chkWP("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://XYZ:55100/LIVEE_11", "XYZ", "passw");

    try {
      connection = ds.getConnection();
      ds.checkData( connection );
    } catch (Exception ex) {
      ds.sendMail("-- main Exception:  " +ex.toString(), "chkLog Alert");
      ex.printStackTrace();
      System.out.println("-- main Exception:" +ex.toString());
    }

    if (connection != null) {
      connection.close();
    }

  }  // public static void main

}
