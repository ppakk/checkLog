/*
 *
 * This class is part of the NGPS project.
 *
 * Copyright 2004, MNGI, Inc. All Rights Reserved.
 *
 *
 */
package com.mngi.portlet.freeform.droplet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;

import atg.nucleus.ServiceException;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;

import com.mngi.atgframework.droplet.BaseDroplet;
import com.mngi.openframework.beans.ObjToRdbms.FreeformBean;
import com.mngi.openframework.beans.ObjToRdbms.SiteBean;
import com.mngi.openframework.config.Config;
import com.mngi.openframework.config.OpenFrameworkDynamicConfiguration;
import com.mngi.openframework.services.ServiceFactory;
import com.mngi.openframework.services.ServiceFactoryException;
import com.mngi.openframework.services.business.site.SiteManager;


/**
 *  Description of class functionality and general usage.
 *
 * @author
 */
public class GSAFreeformDroplet extends BaseDroplet {

  private String query;
  private String gsaYahooSponsorEnabled;   // read from openframework.properties to control enabling yahoo sponsor search.
  private String searchContentType;  // either gsa or yahoo
  private String siteId;
  private String baseImageUrl;
  
  private SiteManager siteManager;
  
  public String getQuery() {
      if (query == null) {
          return "";
      }
      return query;
  }

  public void setQuery(String query){
      this.query = query;    
  }

  public String getGsaYahooSponsorEnabled() {
      return gsaYahooSponsorEnabled;
  }

  public void setGsaYahooSponsorEnabled(String val){
      this.gsaYahooSponsorEnabled = val;    
  }
  
  public String getSearchContentType() {
      return searchContentType;
  }
  
  public void setSearchContentType(String val) {
     this.searchContentType = val;
  }
  
  public String getSiteId() {
      return siteId;
  }

  public void setSiteId(String val){
      this.siteId = val;    
  }

  public String getBaseImageUrl(){
      return baseImageUrl;
  }

  public void setBaseImageUrl(String val){
      this.baseImageUrl = val;
  }

  public void doStartService() throws ServiceException {
      setBaseImageUrl(Config.getProperty("base.image.url"));
      
      if (siteManager == null) {
          try {
    	      siteManager = (SiteManager) ServiceFactory.getService("SiteManager");
          } catch (ServiceFactoryException e) {
              e.printStackTrace();
          }
      }
  }
  
  /**
   *  Services this droplet. This method is the entry point for all activity
   *  that occurs within the droplet.
   *
   * @param  request               the request
   * @param  response              the response
   * @exception  ServletException  if an application specific error occurs
   * @exception  IOException       if an error occured reading from the
   *      request or to the response
   */
  public void service(DynamoHttpServletRequest request, DynamoHttpServletResponse response)
      throws IOException, ServletException {

      OpenFrameworkDynamicConfiguration configuration =
          OpenFrameworkDynamicConfiguration.getInstance();

      String gsaUrl = configuration.get("gsaUrl");
      // if gsaUrl is not set do nothing
      if (gsaUrl == null || gsaUrl.length() == 0 || gsaUrl.equals("0")) {
    	  return;
      }
      // get client param for gsa
      String gsaClient = configuration.get("gsaClient");
      if (gsaClient == null) {
          gsaClient = "default_frontend";
      }
      // get site (aka collection) value for gsa
      String gsaParams = configuration.get("gsaParams");
      if (gsaParams == null) {
    	  gsaParams = "";
      }
      String gsaYahoo = configuration.get("gsaYahooSponsorEnabled");
      setGsaYahooSponsorEnabled(gsaYahoo);
      
      String gsaUseMultiSites = configuration.get("gsaUseMultiSites");
      
      String q = (String) request.getParameter("q");
      String inmeta = "";
      if (q != null && q.length() > 0) {
          int inmetaPos = q.indexOf("inmeta:");
          if (inmetaPos > 0) {
              inmeta = URLEncoder.encode(q.substring(inmetaPos, q.length()), "UTF-8");
    	  }
          int daterangePos = q.indexOf(" daterange:");
          if (daterangePos > 0) {
              q = q.substring(0, daterangePos);
          }
      }
      if (q == null) {
          q = (String) request.getParameter("query");
      }
      setQuery(q);
      setSiteId((String) request.getLocalParameter("siteId"));
      if (getSiteId() == null) {
          return;
      }
      // start used for gsa pagination
      String start = (String) request.getParameter("start");
      String sort = (String) request.getParameter("sort");
      
      String view = request.getParameter("view");
      if ("yahoowebsearch".equals(view)) {
          setSearchContentType("Yahoo");
      } else {
          setSearchContentType(view);    	  
      }
      
      // pageOffset used for yahoo web search pagination
      String pageOffset = (String) request.getLocalParameter("pageOffset");
      int offset = 0;
      if (pageOffset != null) {
          offset = Integer.parseInt(pageOffset);
      }

      // check for results from other sites for search
      String searchSiteIds = "&requiredfields=SITEID:" + getSiteId();
      SiteBean sb = siteManager.getSiteBeanById(siteId);
      if ("1".equals(gsaUseMultiSites) && sb != null && sb.getSearchSiteId() != null) {
    	  Object[] searchSites = sb.getSearchSiteId().toArray();
    	  for (int j=0; j < searchSites.length; j++) {
    		  String siteVal = (String) searchSites[j];
    		  if (siteVal != null && siteVal.length() > 0 && !siteVal.equals(getSiteId())) {
    			  searchSiteIds += "|siteid:" + siteVal;
    		  }
    	  }
      }
      
      // gsa query params doc
      // www.google.com/support/enterprise/static/gsa/docs/admin/70/gsa_doc_set/xml_reference/request_format.html#1077692
      // store value from gsa request into result
      String result = "";
      if (getQuery() != null && getQuery().length() > 0) {
          String gsaQ = URLEncoder.encode(getQuery(),"UTF-8");
          String daterange = "";
          if (gsaQ.indexOf("+daterange") == -1) {
              daterange = "+daterange:2003-01-01.."; 
          }          
          String urlString = gsaUrl + "/search?q=" +gsaQ + daterange;
          if (inmeta != null && inmeta.length() > 0) {
              urlString += "+" + inmeta + "&dnavs=" +inmeta;
          }
          urlString += gsaParams;
          if (searchSiteIds != null && searchSiteIds.length() > 0) {
              urlString += searchSiteIds;
          }
          if (start != null && start.length() > 0) {
              urlString += "&start="+start;
          }
          if (sort != null && sort.length() > 0) {
              urlString += "&sort="+sort;
          }          
          if (true) {
              logDebug("gsaQ=" +gsaQ);
              logDebug("urlString=" +urlString);
          }
          // make gsa request
          try {
              URL url = new URL(urlString);
              HttpURLConnection connection = (HttpURLConnection)url.openConnection();
              connection.setDoOutput(true);
              connection.setAllowUserInteraction(false);
              connection.setRequestMethod("GET");
              if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                  BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream(), "utf-8") );
                  String inputLine;
                  while ((inputLine = in.readLine()) != null) {
                      result = result.concat(inputLine + "\n");
                  }
                  in.close();
              }
              connection.disconnect();
          } catch (Exception e) {
              logError("Error" +e);
          }
      } else {
          if (getQuery() != null && getQuery().length() > 0) {
              result = "No results found.";
          } else {
              result = "Please enter terms to search.";
          }
      }

      FreeformBean bean = new FreeformBean();
      if (isLoggingDebug()) {
          logDebug("\n\nresult=" +result +"\n\n");
      }
      result = result.replaceAll("/search", "/circare/html/gsa_template.jsp");
      String gsaExtra = "http://extras.mnginteractive.com/live/media/gsa";
      result = result.replaceAll("/less.gif", gsaExtra+"/less.gif");
      result = result.replaceAll("/more.gif", gsaExtra+"/more.gif");
      result = result.replaceAll("/nav_current.gif", gsaExtra+"/nav_current.gif");
      result = result.replaceAll("/nav_page.gif", gsaExtra+"/nav_page.gif");
      result = result.replaceAll("/remove.gif", gsaExtra+"/remove.gif");
      result = result.replaceAll("/images/ic_search.png", gsaExtra+"/ic_search.png");
      result = result.replaceAll("/nav_first.png", gsaExtra+"/nav_first.png");
      result = result.replaceAll("/nav_next.png", gsaExtra+"/nav_next.png");
      result = result.replaceAll("images/Title_Left.png", gsaExtra+"/Title_Left.png");
      result = result.replaceAll("/clicklog_compiled.js", gsaExtra+"/clicklog_compiled.js");
      result = result.replaceAll("/cluster.js", gsaExtra+"/cluster.js");
      result = result.replaceAll("/common.js", gsaExtra+"/common.js");
      result = result.replaceAll("/dyn_nav_compiled.js", gsaExtra+"/dyn_nav_compiled.js");
      result = result.replaceAll("/uri.js", gsaExtra+"/uri.js");
      result = result.replaceAll("/xmlhttp.js", gsaExtra+"/xmlhttp.js");
      bean.setHtml(result);
      
      request.setParameter("element", bean);
      request.setParameter("gsaYahooSponsorEnabled", getGsaYahooSponsorEnabled());
      // yahoo_search_on used for call to YahooSponsoredSearchDroplet
      if ("1".equals(getGsaYahooSponsorEnabled())) {
          request.setParameter("yahoo_search_on", "true");
      } else {
    	  request.setParameter("yahoo_search_on", "false");
      }
      request.setParameter("pageOffset", new Integer(offset));

      serviceOutput(request, response);
      return;

  }

}