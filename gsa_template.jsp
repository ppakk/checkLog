<script language="JavaScript" type="text/javascript" src="http://extras.mnginteractive.com/live/js/mngiads/AdsInclude.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.3.0/build/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.3.0/build/dom/dom-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.3.0/build/event/event-min.js" ></script>
<%@ taglib uri="/dsp" prefix="dsp"%>
<%@ taglib uri="/dspel" prefix="dspel"%>
<%@ taglib uri="/mngi_util" prefix="util"%>

<dsp:page>

<script language="JavaScript" type="text/javascript">
	if (typeof(MNGiSearchQueryString) == "undefined"){
		MNGiSearchQueryString = "";
	}
	if (typeof(MNGiSearchContentType) == "undefined"){
		MNGiSearchContentType = "";
	}
	if (typeof(MNGiSearchModQuery) == "undefined"){
		MNGiSearchModQuery = "";
	}
</script>

<dsp:importbean bean="/mngi/framework/GlobalSettings"/>

<dsp:importbean bean="/mngi/common/data/MngiPortalObjects"/>


<dsp:getvalueof id="siteId" idtype="java.lang.String" value="">
<dsp:droplet name="/mngi/search/droplet/SiteIdDroplet">
	<dsp:param name="hostName" value="<%= request.getHeader("HOST") %>"/>
	<dsp:oparam name="found">
		<dsp:getvalueof id="sid" idtype="java.lang.String" param="element">
			<% siteId = sid; %>
		</dsp:getvalueof>
	</dsp:oparam>
	<dsp:oparam name="empty">
	</dsp:oparam>
</dsp:droplet>

<%
String siteCss = null;
String siteQuery = "Site.id = \"" + siteId + "\" AND SiteProp.name = \"SiteCss\"";
String searchCss = null;
String searchQuery = "TypeSpecificId = \"" + siteId + "\" AND SettingPropId = \"SearchCSS\"";
%>

<dsp:droplet name="/atg/dynamo/droplet/RQLQueryForEach">
	<dsp:param name="repository" value="/mngi/content/repository/ContentRepository"/>
	<dsp:param name="itemDescriptor" value="SiteSetting"/>
	<dsp:param name="queryRQL" value="<%=siteQuery%>"/>
	<dsp:oparam name="outputStart">
       <dsp:getvalueof id="listStyle" idtype="String" param="element.Value">
         <% siteCss = listStyle; %>
		 <!-- Search Site CSS = <dsp:valueof param="element.Value"/> -->
		 
       </dsp:getvalueof>
	</dsp:oparam>
</dsp:droplet>

<html>
<head>
<title>Search Results</title>
<%--
<dsp:getvalueof id="imageBasePath" idtype="String" bean="GlobalSettings.baseImageUrl">
	<link type="text/css" rel="stylesheet" href='<%=imageBasePath + "css/MNGiDefaultStyles.css"%>'>
</dsp:getvalueof>
--%>

<dsp:droplet name="/atg/dynamo/droplet/RQLQueryForEach">
	<dsp:param name="repository" value="/mngi/content/repository/ContentRepository"/>
	<dsp:param name="itemDescriptor" value="SettingValue"/>
	<dsp:param name="queryRQL" value="<%=searchQuery%>"/>
	<dsp:oparam name="outputStart">
       <dsp:getvalueof id="listStyle" idtype="String" param="element.Value">
         <% searchCss = listStyle; %>
		 <!-- Search CSS = <dsp:valueof param="element.Value"/> -->
       </dsp:getvalueof>
	</dsp:oparam>
</dsp:droplet>

</head>

<body>


<table cellspacing="2" cellpadding="2" border="0" align="center" width="100%">
<tr>
	<td align="center">

<%-- Search Open CSS Spans --%>

<util:availablecss path="<%=siteCss%>">

<dsp:getvalueof id="imageBasePath" idtype="String" bean="GlobalSettings.baseImageUrl">
	<link type="text/css" rel="stylesheet" href='<%=imageBasePath%>css/sca_template.css'>
</dsp:getvalueof>

<util:availablecss path="<%=searchCss%>">



<div class="srPreHeadFF">
	<dsp:include src="/portlet/freeform/html/display.jsp">
		<dsp:param name="siteId" value="<%=siteId%>"/>
		<dsp:param name="settingPropId" value="SearchPreHeadFF"/>
		<dsp:param name="isPreviewing" bean="MngiPortalObjects.isPreviewing"/>
	</dsp:include>
</div>
<div class="srHeadFF">
	<dsp:include src="/portlet/freeform/html/display.jsp">
		<dsp:param name="siteId" value="<%=siteId%>"/>
		<dsp:param name="settingPropId" value="SearchHeadFF"/>
		<dsp:param name="isPreviewing" bean="MngiPortalObjects.isPreviewing"/>
	</dsp:include>
</div>
<div class="srPostHeadFF">
	<dsp:include src="/portlet/freeform/html/display.jsp">
		<dsp:param name="siteId" value="<%=siteId%>"/>
		<dsp:param name="settingPropId" value="SearchPostHeadFF"/>
		<dsp:param name="isPreviewing" bean="MngiPortalObjects.isPreviewing"/>
	</dsp:include>
</div>
<div class="srPostHead1FF">
	<dsp:include src="/portlet/freeform/html/display.jsp">
		<dsp:param name="siteId" value="<%=siteId%>"/>
		<dsp:param name="settingPropId" value="SearchPostHead1FF"/>
		<dsp:param name="isPreviewing" bean="MngiPortalObjects.isPreviewing"/>
	</dsp:include>
</div>

<div style="clear:both;"></div>

<dsp:droplet name="/atg/dynamo/droplet/Switch">
	<dsp:param name="value" param="template"/>
	<dsp:oparam name="feedback">
		<dsp:include page="sca_template_feedback.jsp">
			<dsp:param name="siteId" value="<%=siteId%>"/>
		</dsp:include>
	</dsp:oparam>
	<dsp:oparam name="feedbackThankyou">
		<dsp:include page="sca_template_feedback_thankyou.jsp">
			<dsp:param name="siteId" value="<%=siteId%>"/>
		</dsp:include>
	</dsp:oparam>
	<dsp:oparam name="default">
	<table class="srContainerMain" cellpadding="0" cellspacing="0" border="0" width="1000">
	<tr>
		<td>
		<%-- --%>
		<dsp:include src="/portlet/freeform/html/gsadisplay.jsp">
			<dsp:param name="siteId" value="<%=siteId%>"/>
			<dsp:param name="settingPropId" value="SearchFooterFF"/>
			<dsp:param name="isPreviewing" bean="MngiPortalObjects.isPreviewing"/>
		</dsp:include>

		</td>
	</tr>
	</table>
	</dsp:oparam>
</dsp:droplet>

	
<div class="srFooterFF">
	<dsp:include src="/portlet/freeform/html/display.jsp">
		<dsp:param name="siteId" value="<%=siteId%>"/>
		<dsp:param name="settingPropId" value="SearchFooterFF"/>
		<dsp:param name="isPreviewing" bean="MngiPortalObjects.isPreviewing"/>
	</dsp:include>
</div>

<div class="srPrivacyPolicy">
	<center>
		<a href="#">Privacy Policy</a> | <a href="#">Terms of Use</a>
	</center>
</div>

<%-- Search Close CSS Spans --%>
</util:availablecss>
</util:availablecss>

</dsp:getvalueof>

</td>
</tr>
</table>


</body>
</html>

</dsp:page>