<%--
Copyright 2006, MediaNews Group, Inc. All Rights Reserved.
*** DO NOT REMOVE 'NoKeywords' TAG AS THIS MAY RESULT IN ERRORS WHEN THIS FILE IS SAVED IN STARTEAM ***
$NoKeywords$
--%>
<%@ page import="java.util.*,java.text.*" %>
<%@ taglib uri="/dsp" prefix="dsp" %>
<%@ taglib uri="/dspel" prefix="dspel" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<dspel:page>

<%-- BEGIN Security Check: --%>
<dsp:importbean bean="/mngi/authentication/IsAuthenticated"/>
<!-- IsAuthenticated.canProceed = <dsp:valueof bean="IsAuthenticated.canProceed"/> -->
<!-- IsAuthenticated.context = <dsp:valueof bean="IsAuthenticated.context"/> -->
<!-- IsAuthenticated.errorURL = <dsp:valueof bean="IsAuthenticated.errorURL"/> -->
<dsp:droplet name="/atg/dynamo/droplet/Switch">
    <dsp:param name="value" bean="IsAuthenticated.canProceed"/>
    <dsp:oparam name="true">
    
      <dspel:importbean bean="/mngi/dashboard/contentmanagement/formhandlers/SearchFormHandler" var="searchFormHandler"/>
      
      <dspel:importbean bean="/atg/userprofiling/Profile" var="profile"/>
      <dspel:setvalue bean="SearchFormHandler.profile" beanvalue="/atg/userprofiling/Profile"/>
      
      <dspel:importbean bean="/atg/dynamo/droplet/IsEmpty"/>
      <dspel:importbean bean="/atg/dynamo/droplet/Switch"/>
      <dspel:importbean bean="/atg/dynamo/droplet/ErrorMessageForEach"/>
      <dspel:importbean bean="/atg/dynamo/droplet/RQLQueryForEach"/>
      <dspel:importbean bean="/atg/dynamo/droplet/ForEach"/>
      
      <%-- NOTE: context MUST be set 1st --%>
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" value="${param.itemTypes}"/>
          <dspel:oparam name="false">
              <dspel:setvalue bean="SearchFormHandler.context" value="${param.itemTypes}"/>
              <dspel:setvalue bean="SearchFormHandler.itemTypes" value="${param.itemTypes}"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" value="${param.excludeIds}"/>
          <dspel:oparam name="false">
              <dspel:setvalue bean="SearchFormHandler.excludeIds" value="${param.excludeIds}"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" value="${param.maxAllowed}"/>
          <dspel:oparam name="false">
              <dspel:setvalue bean="SearchFormHandler.maxAllowed" value="${param.maxAllowed}"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" value="${param.titleCropped}"/>
          <dspel:oparam name="false">
              <dspel:setvalue bean="SearchFormHandler.titleCropped" value="${param.titleCropped}"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:setvalue bean="SearchFormHandler.value.siteId" beanvalue="Profile.currentSite.repositoryId"/>
      
      <dspel:setvalue param="page" value="/dashboard/html/content_management/article/popups/search/search.jsp"/>
      
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" bean="SearchFormHandler.value.filterBy"/>
          <dspel:oparam name="true">
              <dspel:setvalue bean="SearchFormHandler.value.filterBy" value="1"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" value="${param.returnValueElementId}"/>
          <dspel:oparam name="false">
              <dspel:setvalue bean="SearchFormHandler.value.returnValueElementId" value="${param.returnValueElementId}"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" value="${param.associateButtonId}"/>
          <dspel:oparam name="false">
              <dspel:setvalue bean="SearchFormHandler.value.associateButtonId" value="${param.associateButtonId}"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:droplet name="IsEmpty">
          <dspel:param name="value" value="${param.behaviors}"/>
          <dspel:oparam name="false">
              <dspel:setvalue bean="SearchFormHandler.value.behaviors" value="${param.behaviors}"/>
          </dspel:oparam>
      </dspel:droplet>
      
      <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
      
      <html>
      <head>
      <title>Search</title>
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
      <link rel="stylesheet" type="text/css" href="/dashboard/html/css/default.css">
      <SCRIPT language="JavaScript" src="/dashboard/html/js/common.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/pop-window.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/calendar.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/calendar2.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/validate.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/uploadMedia.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/chooseContentGroups.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/chooseSites.js" type="text/javascript"></SCRIPT>
      <SCRIPT language="JavaScript" src="/dashboard/html/js/new_controls.js" type="text/javascript"></SCRIPT>
      <script type="text/javascript" language="javascript" src="/dashboard/html/js/thumbnail_util.js"></script>
      
      </head>
      <body id="articleSearchBody" onkeypress="if (event.keyCode == 13) enterButtonHandler(event);">
      
      <dspel:droplet name="Switch">
          <dspel:param bean="SearchFormHandler.formError" name="value"/>
          <dspel:oparam name="true">
              <h2>The following Errors were found in your search:</h2>
              <dspel:droplet name="ErrorMessageForEach">
                  <dspel:param name="exceptions" bean="SearchFormHandler.formExceptions"/>
                  <dspel:oparam name="output">
                      <b><font color="red"><dspel:valueof param="message"/></font></b><p>
                  </dspel:oparam>
              </dspel:droplet>
              <p>
          </dspel:oparam>
      </dspel:droplet>
      
      <dspel:form id="searchForm" name="searchForm" action="search.jsp" method="post">
      
      <dspel:input name="successUrl" type="hidden" bean="SearchFormHandler.value.successUrl" value='${param["page"]}'/>
      <dspel:input name="errorUrl" type="hidden" bean="SearchFormHandler.value.errorUrl" value='${param["page"]}'/>
      
      
      <script language="JavaScript">
      
      var newData = '';
      
      // send behaviors as a | delimited list, like this: "SHOW_ASSOCIATE_BUTTON|ASSOCIATE_THEN_CLOSE_POPUP"
      var behaviors = '<dspel:valueof bean="SearchFormHandler.value.behaviors"/>';
      var itemTypes = '<dspel:valueof bean="SearchFormHandler.itemTypes"/>';
      var returnValueElementId = '<dspel:valueof bean="SearchFormHandler.value.returnValueElementId"/>';
      var associateButtonId = '<dspel:valueof bean="SearchFormHandler.value.associateButtonId"/>';
      var maxAllowed = '<dspel:valueof bean="SearchFormHandler.maxAllowed"/>';
      var timeZone =  '<dspel:valueof bean="SearchFormHandler.timeZoneShort"/>';
      var selectedSites = '<dspel:valueof bean="SearchFormHandler.value.selectedSites"/>';
      
      function $(elementId) {
          return document.getElementById(elementId);
      }
      
      function $$(elementId) {
          return document.getElementsByName(elementId);
      }
      
      /*
      Open-ended ( configurable / future extendable ) 
      way of returning data to calling window:
      */
      function compileData(input) {
        var compiledData = "";
        // add your own data compilation behaviors here:
        if (shouldDoBehavior("RETURN_TYPE_ID_NAME_URL")) {
          // for groups, I needed:
          // <itemType>:<repositoryId>|<itemName> <!-- itemUrl -->
          // <div id="itemType:repositoryIdimageURL" style="display:none">imageURL</div> (for Safari)
          
          var type_n_id = input.value;
          var element = $(type_n_id);
          var moreInfo = element.innerHTML.trim();
          if(moreInfo.indexOf("!--")==-1)
            moreInfo = "<!-- "+$(type_n_id+"imageURL").innerHTML+" -->";
          compiledData = type_n_id + "|" + moreInfo;    
        } else if (shouldDoBehavior("RETURN_SOME_FUTURE_WAY_OF_DOING_SOMETHING_BETTER")) {
          // get it?
          alert("I don't know how to do a RETURN_SOME_FUTURE_WAY_OF_DOING_SOMETHING_BETTER behavior yet, teach me pls...");
          compiledData = "?";
        } else {
          // the default just returns <itemType>:<repositoryId> just like ATG represents an item
          compiledData = input.value;
        }
        return compiledData;
      }
      
      /*
      this now iterates the available checkboxes, 
      calling compileData(<checkbox>) for each SELECTED one.
      returns true if data was compiled, false otherwise
      */
      function userMadeChoice() {
          var numInput = $("numOfChoices");
          if (numInput) {
              var num = numInput.value;
              switch (num) {
                  case 0:
                      newData = '';
                      break;
                  case 1:
                      var choiceInput = $("choice");
                      if (choiceInput.checked) {
                          newData = compileData(choiceInput);
                      }
                      break;
                  default:
                      var choiceInput = $$("choice");
                      if (choiceInput) {
                          for (var x=0; x<choiceInput.length; x++) {
                              if(choiceInput[x].checked) {
                                  var data = compileData(choiceInput[x]);
                                  if (newData == '') {
                                      newData = data;
                                  } else {
                                      newData += "," + data;
                                  }
                              }
                          }
                      } else {
                          alert("ERROR: No choices exist.");
                      }
                      break;
              }
          }
          return (newData != '');
      }
      
      /*
      this isn't currently used in this page, but demos
      how to dynamically write html to a page
      during the render process, based on a behavior
      */
      function decide(aBehavior,yes,no) {
          if (shouldDoBehavior(aBehavior)) {
              document.write(yes);
          } else {
              document.write(no);
          }
      }
      
      function handleAssociate() {
          if (userMadeChoice()) {
              if (shouldDoBehavior("ASSOCIATE_THEN_CLOSE_POPUP")) {
                  associate();
                  closePopUp();
              } else if (shouldDoBehavior("ASSOCIATE_ONLY")) {
                  associate();
              } else {
                alert("Unknown action in handleAssociate()");
              }
          } else {
              alert("ERROR: You haven't selected an item to associate.");
          }
      }
      
      function associate() {
          var parentWin = getParentWindow();
          if (parentWin && parentWin != null && isDefined(parentWin) && parentWin != false) {
              var returnValueElement = parentWin.getById(returnValueElementId);
              if (returnValueElement) {
                  returnValueElement.value = newData;
                  var associateButton = parentWin.getById(associateButtonId);
                  if (associateButton) {
					if(associateButton.click) {
                      associateButton.click();
                    } else if (associateButton.onclick) {
                    // hackety hack to deal with safari's non-existant click method on buttons
                      associateButton.onclick();
                    }
                  } else {
                      alert("ERROR: Invalid association button element id specified [" + associateButtonId + "]");
                  }
              } else {
                  alert("ERROR: Invalid return value element id specified [" + returnValueElementId + "]");
              }
          } else {
              alert("ERROR: Unable to find parent window, please close all windows and re-start your application.");
          }
      }
      
      function closePopUp() {
          window.close();
      }
      
      function shouldDoBehavior(aBehavior) {
          return (behaviors.indexOf(aBehavior) > -1) ? true : false;
      }
      
      function updateCheckboxes(theSelectElement) {
          var allCheckBoxes = $$(theSelectElement.name);
          var num = allCheckBoxes.length;
          var numChecked = 0;
          for (var x=0; x<num; x++) {
              if (allCheckBoxes[x].checked == true) {
                  numChecked += 1;
              }
          }
          if (numChecked < (maxAllowed-0)) {
              // make sure all checkboxes are enabled:
              for (var x=0; x<num; x++) {
                  allCheckBoxes[x].disabled = false;
              }
          } else {
              // update display:
              for (var x=0; x<num; x++) {
                  if (allCheckBoxes[x].checked != true) {
                      allCheckBoxes[x].disabled = true;
                  }
              }
          }
      }
      
      function choiceMade(theSelectElement) {
          if ((maxAllowed-0) != 0) {
              // apply rulz to checkboxes:
              updateCheckboxes(theSelectElement);
          }
      }
      
      // extra effort for child calendar windows to close when parent window closes.
      var calWin;
      
      function dateChooser(id) {
        var dateValue = $(id).value;
        var re_date = /^(\d+)\/(\d+)\/(\d+)/;
        if (!re_date.exec(dateValue)) {
          dateValue = "";
        }
        calWin = show_calendar2(id, dateValue, 'both', timeZone);
      }
      
      function handleCalWindow() {
        if (calWin != undefined) {
          if (calWin != null) {
            if (!calWin.closed) {
              try {
                calWin.close();
                calWin = undefined;
              } catch (error) {
                // ignore it...we tried
              }
            }
          }
        }
      }
      
      function saveCalDateTimeValueBack(id, value) {
        $(id).value = value;
      }
      // extra effort for child groups window to close when parent window closes.
      var groupsWin;
      
      function myChooseContentGroups(groupTypes, idOfResultElement, idOfNamesElement) {
        groupsWin = chooseContentGroups(groupTypes, idOfResultElement, idOfNamesElement);
      }
      
      function handleGroupsWindow() {
        if (groupsWin != undefined) {
          if (groupsWin != null) {
            if (!groupsWin.closed) {
              try {
                groupsWin.close();
                groupsWin = undefined;
              } catch (error) {
                // ignore it...we tried
              }
            }
          }
        }
      }
      
      $("articleSearchBody").onunload = function(){ handleCalWindow(); handleGroupsWindow(); }
      
      /*
       This will ignore most everything, except the case where my parent is saving
       at which time, I need to close which will kick off the onunload
       event above, closing all my children too.
       NOTE: When we make the rest of the dash 2.0'ish, use the hidden_controller
       openSingletonWin call for all the little pop-up grandchildren too.
      */
      function notify(thisEvent) {
      	try{
      		var result = true;
      		var windowHandle = thisEvent.eventInitiatorWindow;
      		if(windowHandle.name == window.name){
      			return true;
      		}			
      		var eventAction = thisEvent.eventAction;
      		switch (eventAction) {
      			case "SAVE_ACTION":
      				if(isChild(windowHandle)){
      					refresh();
      				} else if(isParent(windowHandle)) {
                self.close();
      				}
      				break;
      		}
      		return true;
      	} catch (error){
      		logError(window.name + " encountered error " + error.description);
      	}
      }

      ////////////////////////////////////////////////////////////////////////////////////////
      
      // ADDED BY MARK KODAK 
      // Set the horizontal and vertical position for the popup
      
      PositionX = 100;
      PositionY = 100;
      
      // Set these value approximately 20 pixels greater than the
      // size of the largest image to be used (needed for Netscape)
      
      defaultWidth  = 500;
      defaultHeight = 500;
      
      // Set autoclose true to have the window close automatically
      // Set autoclose false to allow multiple popup windows
      
      var AutoClose = true;
      
      if (parseInt(navigator.appVersion.charAt(0))>=4){
      var isNN=(navigator.appName=="Netscape")?1:0;
      var isIE=(navigator.appName.indexOf("Microsoft")!=-1)?1:0;}
      var optNN='scrollbars=no,width='+defaultWidth+',height='+defaultHeight+',left='+PositionX+',top='+PositionY;
      var optIE='scrollbars=no,width=100,height=100,left='+PositionX+',top='+PositionY;
      function popImage(imageURL,imageTitle){
      if (isNN){imgWin=window.open('about:blank','',optNN);}
      if (isIE){imgWin=window.open('about:blank','',optIE);}
      with (imgWin.document){
      writeln('<html><head><title>Loading...</title><style>body{margin:0px;}</style>');writeln('<sc'+'ript>');
      writeln('var isNN,isIE;');writeln('if (parseInt(navigator.appVersion.charAt(0))>=4){');
      writeln('isNN=(navigator.appName=="Netscape")?1:0;');writeln('isIE=(navigator.appName.indexOf("Microsoft")!=-1)?1:0;}');
      writeln('function reSizeToImage(){');writeln('if (isIE){');writeln('window.resizeTo(100,100);');
      writeln('width=100-(document.body.clientWidth-document.images[0].width);');
      writeln('height=130-(document.body.clientHeight-document.images[0].height);');
      writeln('window.resizeTo(width,height);}');writeln('if (isNN){');       
      writeln('window.innerWidth=document.images["George"].width;');writeln('window.innerHeight=document.images["George"].height;}}');
      writeln('function doTitle(){document.title="'+imageTitle+'";}');writeln('</sc'+'ript>');
      if (!AutoClose) writeln('</head><body bgcolor=000000 scroll="no" onload="reSizeToImage();doTitle();self.focus()">')
      else writeln('</head><body bgcolor=ffffff scroll="no" onload="reSizeToImage();doTitle();self.focus()" onblur="self.close()">');
      writeln('<img name="George" src='+imageURL+' style="display:block"><div align="center">');
      if (navigator.platform.toLowerCase().indexOf("win") > -1)
       {
      	writeln('<a style="font-size:11px;" href="javascript:window.close();">Close Window</a>');
        }
      	writeln('</div></body></html>');
      close();		
      }}
      
      </script>
      <table cellpadding="3" cellspacing="0" border="0" width="975">
          <tr>
              <td bgcolor="#333333" align="right">
                  <table>
                      <tr>
                          <td class="buttonlink" onclick="javascript:window.close();">Close</td>
                      </tr>
                  </table>
              </td>
          </tr>
          <tr>
              <dspel:droplet name="Switch">
                  <dspel:param bean="SearchFormHandler.itemTypes" name="value"/>
                  <dspel:oparam name="Image">
                      <td align="left" class="pagetitle1">Associate&nbsp;<SPAN class="pagetitle2">Images</SPAN></TD>
                  </dspel:oparam>
                  <dspel:oparam name="Freeform">
                      <td align="left" class="pagetitle1">Associate&nbsp;<SPAN class="pagetitle2">Freeforms</SPAN></TD>
                  </dspel:oparam>
                  <dspel:oparam name="Poll">
                      <td align="left" class="pagetitle1">Associate&nbsp;<SPAN class="pagetitle2">Polls</SPAN></TD>
                  </dspel:oparam>
                  <dspel:oparam name="Binary,Flash">
                      <td align="left" class="pagetitle1">Associate&nbsp;<SPAN class="pagetitle2">Pdf or Flash</SPAN></TD>
                  </dspel:oparam>
                  <dspel:oparam name="default">
                      <td align="left" class="pagetitle1">Associate&nbsp;<SPAN class="pagetitle2"><dspel:valueof bean="SearchFormHandler.itemTypes"/></SPAN></TD>
                  </dspel:oparam>
              </dspel:droplet>
          </tr>
          <tr>
              <td class="bodybold">< <dspel:valueof bean="SearchFormHandler.titleCropped"/> ></td>
          </tr>
          <tr> 
              <td>
                  <table cellpadding="5" cellspacing="0" border="1" bordercolor="#000000" width="100%">
                      <tr> 
                          <td>
                              <span class="tabletitle">&nbsp;<dspel:valueof bean="SearchFormHandler.itemTypes"/> Search</span>
                              <br>
                              <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                  <tr valign="top"> 
                                      <!-- BEGIN: Left column -->
                                      <td width="575">
                                          <table cellpadding="0" cellspacing="0" border="0">
                                              <!-- BEGIN: Filter By: -->
                                              <tr>
                                                  <td bgcolor="#dddddd" width="90" class="body">&nbsp;Filter&nbsp;By&nbsp;:</td>
                                                  <td>
                                                      <dspel:select id="filterBySelect" bean="SearchFormHandler.value.filterBy" onchange="updateSearchGUI();">
                                                          <dspel:option value="0"></dspel:option>
                                                          <dspel:option value="1">Created Today</dspel:option>
                                                          <dspel:option value="2">Created in last 2 Days</dspel:option>
                                                          <dspel:option value="3">Created in last 7 Days</dspel:option>
                                                          <dspel:option value="4">Created in last 30 Days</dspel:option>
                                                          <dspel:option value="5">Created in last 60 Days</dspel:option>
                                                          <dspel:option value="6">Starts Today</dspel:option>
                                                          <dspel:option value="7">Starts in next 7 Days</dspel:option>
                                                          <dspel:option value="8">Expired</dspel:option>
                                                          <dspel:option value="9">All items</dspel:option>
                                                      </dspel:select>
                                                  </td>
                                              </tr>
                                              <!-- END: Filter By: -->
                                              <!-- BEGIN: Name -->
                                              <tr>
                                                  <td bgcolor="#dddddd" width="90" class="body">&nbsp;Name&nbsp;:</td>
                                                  <td>
                                                      <dspel:input type="text" bean="SearchFormHandler.value.title" size="40" maxlength="255"/>
                                                  </td>
                                              </tr>
                                              <!-- END: Name -->
                                              <!-- BEGIN: Create Date: -->
                                              <tr>
                                                  <td bgcolor="#dddddd" width="90" class="body">&nbsp;Create&nbsp;Date&nbsp;:</td>
                                                  <td>
                                                      <dspel:input readonly="true" type="text" id="createDateMin" bean="SearchFormHandler.value.createDateMin" converter="MngiDate" nullable="true" size="24"/>
                                                      <img src="/dashboard/html/images/cal.gif" alt="Click to choose a beginning create date." height="16" width="16" border="0" onclick="dateChooser('createDateMin');" style="cursor:pointer;"/>
                                                      <img src="/dashboard/html/images/delete_icon.gif" alt="Click to remove the beginning create date." height="16" width="16" border="0" onclick="javascript:document.searchForm.createDateMin.value='';" style="cursor:pointer;"/>
                                                      <span class="body">to</span>
                                                      <dspel:input readonly="true" type="text" id="createDateMax" bean="SearchFormHandler.value.createDateMax" converter="MngiDate" nullable="true" size="24"/>
                                                      <img src="/dashboard/html/images/cal.gif" alt="Click to choose a ending created-on date." height="16" width="16" border="0" onclick="dateChooser('createDateMax');" style="cursor:pointer;"/>
                                                      <img src="/dashboard/html/images/delete_icon.gif" alt="Click to remove the ending created-on date." height="16" width="16" border="0" onclick="javascript:document.searchForm.createDateMax.value='';" style="cursor:pointer;"/>
                                                  </td>
                                              </tr>
                                              <!-- END: Create Date: -->
                                              <!-- BEGIN: Start Date: -->
                                              <tr>
                                                  <td bgcolor="#dddddd" width="90" class="body">&nbsp;Start&nbsp;Date&nbsp;:</td>
                                                  <td>
                                                      <dspel:input readonly="true" type="text" id="startDateMin" bean="SearchFormHandler.value.startDateMin" converter="MngiDate" nullable="true" size="24"/>
                                                      <img src="/dashboard/html/images/cal.gif" alt="Click to choose a beginning launch date." height="16" width="16" border="0" onclick="dateChooser('startDateMin');" style="cursor:pointer;"/>
                                                      <img src="/dashboard/html/images/delete_icon.gif" alt="Click to remove the beginning launch date." height="16" width="16" border="0" onclick="javascript:document.searchForm.startDateMin.value='';" style="cursor:pointer;"/>
                                                      <span class="body">to</span>
                                                      <dspel:input readonly="true" type="text" id="startDateMax" bean="SearchFormHandler.value.startDateMax" converter="MngiDate" nullable="true" size="24"/>
                                                      <img src="/dashboard/html/images/cal.gif" alt="Click to choose a ending launch date." height="16" width="16" border="0" onclick="dateChooser('startDateMax');" style="cursor:pointer;"/>
                                                      <img src="/dashboard/html/images/delete_icon.gif" alt="Click to remove the ending launch date." height="16" width="16" border="0" onclick="javascript:document.searchForm.startDateMax.value='';" style="cursor:pointer;"/>
                                                  </td>
                                              </tr>
                                              <!-- END: Start Date: -->
                                              <!-- BEGIN: Types ( only displayed when searching on more than one type is possible -->
                                              <dspel:droplet name="Switch">
                                                  <dspel:param name="value" bean="SearchFormHandler.multipleItemTypes"/>
                                                  <dspel:oparam name="true">
                                                      <tr>
                                                        <td class="body" colspan="2">
                                                          Types:
                                                          <dspel:droplet name="ForEach">
                                                              <dspel:param bean="SearchFormHandler.eachItemType" name="array"/>
                                                              <dspel:setvalue param="ItemType" paramvalue="element"/>
                                                              <dspel:oparam name="output">
                                                                  <dspel:input type="radio" bean="SearchFormHandler.value.selectedItemType" value='${param["ItemType"]}'/>
                                                                  <dspel:valueof param="ItemType"/>
                                                              </dspel:oparam>
                                                          </dspel:droplet>
                                                        </td>
                                                      </tr>
                                                  </dspel:oparam>
                                                  <dspel:oparam name="default">
                                                      <dspel:input type="hidden" bean="SearchFormHandler.value.selectedItemType" beanvalue="SearchFormHandler.itemTypes"/>
                                                  </dspel:oparam>
                                              </dspel:droplet>
<%-- TODO --%>
                                              <tr>
                                                  <td class="freeformSearchFormElementLabel">Sites:</td>
                                                  <td>
                                                  <table cellpadding="0" cellspacing="0">
                                                  <tr>
                                                      <td>
                                                      <table>
                                                      <tr>
                                                          <td class="buttonlink" onclick="chooseSitesWindow('selectedSites', 'siteNames');" style="padding:0px;" id="selectSitesButton">
														    &nbsp;+&nbsp;
                                                          </td>
                                                      </tr>
                                                      </table>
                                                      </td>										
                                                      <td>
                                                      <span id="siteNames"></span>
                                                          <dspel:input id="selectedSites" type="hidden" bean="SearchFormHandler.selectedSites"/>
                                                      </td>
                                                  </tr>
                                                  </table>
                                                  </td>								
                                              </tr>

                                              <!-- END: Types ( only displayed when searching on more than one type is possible -->
                                              <!-- BEGIN: Include Expired -->
                                              <tr>
                                                <td class="body" colspan="2">
                                                  Include items that are Expired?
                                                  <dspel:input type="radio" id="includeExpiredTrue" bean="SearchFormHandler.value.includeExpired" value="true"/>Yes
                                                  <dspel:input type="radio" id="includeExpiredFalse" bean="SearchFormHandler.value.includeExpired" value="false"/>No
                                                </td>
                                              </tr>
                                              <!-- END: Include Expired -->
                                              <!-- BEGIN: Include Deleted -->
                                              <tr>
                                                <td class="body" colspan="2">
                                                  Include items that are Deleted?
                                                  <dspel:input type="radio" id="includeDeletedTrue" bean="SearchFormHandler.value.includeDeleted" value="true"/>Yes
                                                  <dspel:input type="radio" id="includeDeletedFalse" bean="SearchFormHandler.value.includeDeleted" value="false"/>No
                                                </td>
                                              </tr>
                                              <!-- END: Include Deleted -->
                                          </table>
                                      </td>
                                      <!-- END: Left column -->
                                      <!-- BEGIN: Right column -->
                                      <td>
                                          <table cellpadding="0" cellspacing="0" border="0">
                                              <!-- BEGIN: Groups: -->
                                              <dspel:droplet name="Switch">
                                                  <dspel:param bean="SearchFormHandler.itemTypes" name="value"/>
                                                  <dspel:oparam name="Freeform">
                                                      <tr>
                                                          <td bgcolor="#dddddd" width="90" class="body">&nbsp;Groups&nbsp;:</td>
                                                          <td>
                                                              <table cellpadding="0" cellspacing="0" border="0">
                                                                  <tr>
                                                                      <td class="body">&nbsp;&nbsp;Unavailable when searching for Freeforms.</td>
                                                                  </tr>
                                                              </table>
                                                          </td>
                                                      </tr>
                                                  </dspel:oparam>
                                                  <dspel:oparam name="default">
                                                      <dspel:input id="selectedContentGroups" type="hidden" bean="SearchFormHandler.value.selectedContentGroups"/>
                                                      <tr>
                                                          <td bgcolor="#dddddd" width="90" class="body">&nbsp;Groups&nbsp;:</td>
                                                          <td>
                                                              <table cellpadding="0" cellspacing="0" border="0">
                                                                  <tr>
                                                                      <td>
                                                                          <input id="contentGroupNames" type="text" readonly="true" size="40">
                                                                      </td>
                                                                      <td>
                                                                          <table cellpadding="0" cellspacing="0" border="0">
                                                                              <tr>
                                                                                  <td class="buttonlink" onclick="myChooseContentGroups('CATEGORY,DISPLAYGROUP', 'selectedContentGroups', 'contentGroupNames');" style="padding:0px;">&nbsp;+&nbsp;</td>
                                                                              </tr>
                                                                          </table>
                                                                      </td>
                                                                  </tr>
                                                              </table>
                                                          </td>
                                                      </tr>
                                                      <script language="JavaScript" type="text/javascript">
                                                      initContentGroups("selectedContentGroups","contentGroupNames");
                                                      </script>
                                                  </dspel:oparam>
                                              </dspel:droplet>
                                              <!-- END: Groups: -->
                                              <!-- BEGIN: Keywords -->
                                              <tr>
                                                  <td bgcolor="#dddddd" width="90" class="body">&nbsp;Keywords&nbsp;:</td>
                                                  <td>
                                                      <dspel:input type="text" bean="SearchFormHandler.value.keywords" size="40" maxlength="255"/>
                                                  </td>
                                              </tr>
                                              <!-- END: Keywords -->
                                              <!-- BEGIN: Status -->
                                              <tr>
                                                  <td bgcolor="#dddddd" width="90" class="body">&nbsp;Status&nbsp;:</td>
                                                  <td>
                                                      <dspel:select id="filterByState" bean="SearchFormHandler.value.state" onchange="updateSearchGUI();">
                                                          <dspel:option value=""></dspel:option>
                                                          <dspel:droplet name="/atg/dynamo/droplet/RQLQueryForEach">
                                                              <dspel:param name="queryRQL" value="NOT (Name = \"Rejected\" OR Name = \"Hold\")"/>
                                                              <dspel:param name="repository" value="/mngi/content/repository/ContentRepository"/>
                                                              <dspel:param name="itemDescriptor" value="State"/>
                                                              <dspel:param name="sortProperties" value="+name"/>
                                                              <dspel:oparam name="output">
                                                                  <dspel:option paramvalue="element.id"><dspel:valueof param="element.name"/></dspel:option>
                                                              </dspel:oparam>
                                                          </dspel:droplet>
                                                      </dspel:select>
                                                  </td>
                                              </tr>
                                              <!-- END: Status -->
                                              <!-- BEGIN: Query shared content -->
                                              <dspel:droplet name="Switch">
                                                  <dspel:param bean="SearchFormHandler.itemTypes" name="value"/>
                                                  <dspel:oparam name="Binary,Flash">
                                                  
                                                  </dspel:oparam>
                                                  <dspel:oparam name="default">
                                                      <tr>
                                                          <td colspan="2" class="body">
                                                              <dspel:input type="radio" id="querySharedOnlyFalse" bean="SearchFormHandler.value.querySharedOnly" value="false"/>Search your content
                                                              <dspel:input type="radio" id="querySharedOnlyTrue" bean="SearchFormHandler.value.querySharedOnly" value="true"/>Search shared content
                                                          </td>
                                                      </tr>
                                                  </dspel:oparam>
                                              </dspel:droplet>
                                              <!-- END: Query shared content -->
                                          </table>
                                      </td>
                                  </tr>
                              </table>
                              <div align="right">
                                  <table>
                                      <tr>
                                          <td>
                                              <dspel:input name="search" type="submit" id="searchFormButton" bean="SearchFormHandler.search" value="Search" style="display:none;"/>
                                              <table>
                                                  <tr>
                                                      <td class="buttonlink" onclick="clickSearchButton();">Search</td>
                                                  </tr>
                                              </table>
                                          </td>
                                          <td>
                                              <dspel:input name="clearQuery" type="submit" id="searchResetButton" bean="SearchFormHandler.reset" value="New Search" style="display:none;"/>
                                              <table>
                                                  <tr>
                                                      <td class="buttonlink" onclick="clickIt('searchResetButton');">Reset</td>
                                                  </tr>
                                              </table>
                                          </td>
                                      </tr>
                                  </table>
                              </div>
                          </td>
                      </tr>
                  </table>
              </td>
          </tr>
          <dspel:droplet name="Switch">
              <dspel:param name="value" bean="SearchFormHandler.value.previouslySubmitted"/>
              <dspel:oparam name="true">
                  <tr>
                      <td>
                          <table width="100%" border="0" bordercolor="#000000" cellspacing="0" cellpadding="0">
                              <tr>
                                  <td>
                                      <dspel:droplet name="ForEach">
                                          <dspel:param bean="SearchFormHandler.results" name="array"/>
                                          <dspel:oparam name="outputStart">
                                               <dspel:droplet name="Switch">
                                                  <dspel:param name="value" bean="SearchFormHandler.value.lessThanMax"/>
                                                  <dspel:oparam name="true">
                                                      <div align="left">
                                                          <table>
                                                              <tr>
                                                                  <td>
                                                                      <table>
                                                                          <tr>
                                                                              <td class="buttonlink" onclick="handleAssociate();">Save</td>
                                                                          </tr>
                                                                      </table>
                                                                  </td>
                                                                  <td>
                                                                      <table>
                                                                          <tr>
                                                                              <td class="buttonlink" onclick="closePopUp();">Cancel</td>
                                                                          </tr>
                                                                      </table>
                                                                  </td>
                                                              </tr>
                                                          </table>
                                                          <table>
                                                              <tr>
                                                                  <td>
                                                                      <%-- NOTE: leave this on one line, or suffer spacing issues: --%>
                                                                      <span class="tabletitle">Search Results&nbsp;>&nbsp;Displaying&nbsp;<dspel:valueof param="size"/>&nbsp;<dspel:valueof bean="SearchFormHandler.value.selectedItemType"/><dspel:droplet name="Switch"><dspel:param name="value" param="size"/><dspel:oparam name="1">.</dspel:oparam><dspel:oparam name="default">s.</dspel:oparam></dspel:droplet></span>
                                                                  </td>
                                                              </tr>
                                                          </table>
                                                      </div>
                                                      <input type='hidden' id='numOfChoices' value='<dspel:valueof param="size"/>'>
                                                      <table width="100%" border="1" bordercolor="#000000" cellspacing="0" cellpadding="0">
                                                          <tr bgcolor="#dddddd">
                                                              <td align="center" class="bodybold">Select</td>
                                                              <td class="bodybold" align="center">
                                                                  &nbsp;<dspel:a href='${param["page"]}' bean="SearchFormHandler.value.sortBy" value="Title">Name&nbsp;(ID)</dspel:a>&nbsp;
                                                              </td>
                                                              <td class="bodybold" align="center">
                                                                  &nbsp;<dspel:a href='${param["page"]}' bean="SearchFormHandler.value.sortBy" value="StateId">Status</dspel:a>&nbsp;
                                                              </td>
                                                              <td class="bodybold" align="center">
                                                                  &nbsp;<dspel:a href='${param["page"]}' bean="SearchFormHandler.value.sortBy" value="CreateDate">Create&nbsp;Date</dspel:a>&nbsp;
                                                              </td>
                                                              <td class="bodybold" align="center">
                                                                  &nbsp;<dspel:a href='${param["page"]}' bean="SearchFormHandler.value.sortBy" value="StartDate">Start&nbsp;Date</dspel:a>&nbsp;
                                                              </td>
                                                              <dspel:droplet name="Switch">
                                                                  <dspel:param name="value" bean="SearchFormHandler.value.querySharedOnly"/>
                                                                  <dspel:oparam name="true">
                                                                      <td class="bodybold" align="center">
                                                                          &nbsp;<dspel:a href='${param["page"]}' bean="SearchFormHandler.value.sortBy" value="SiteId">Site</dspel:a>&nbsp;
                                                                      </td>
                                                                  </dspel:oparam>
                                                              </dspel:droplet>
                                                          </tr>
                                                  </dspel:oparam>
                                                  <dspel:oparam name="default">
                                                      <div align="left">
                                                          <table>
                                                              <tr>
                                                                  <td colspan="2" class="tabletitle">
                                                                      <hr style="color:black;height:1px;width:100%">
                                                                      Search Results &gt; <span class="tabletitle"> <font color=red><i><dspel:valueof bean="SearchFormHandler.maxResultsMsg"/></i></font></span>
                                                                  </td>
                                                              </tr>
                                                          </table>
                                                      </div>
                                                  </dspel:oparam>
                                              </dspel:droplet>
                                          </dspel:oparam>
                                          <dspel:oparam name="output">
                                              <dspel:droplet name="Switch">
                                                  <dspel:param name="value" bean="SearchFormHandler.value.lessThanMax"/>
                                                  <dspel:oparam name="true">
                                                      <tr>
                                                          <td class="body" align="center" valign="top">
                                                              <input type='checkbox' onclick='return choiceMade(this);' name='choice' id='choice' value='<dspel:valueof param="element.atgId"/>'>
                                                          </td>
                                                          <td class="body">
                                                               <dspel:droplet name="Switch">
                                                             		<dspel:param name="value" bean="SearchFormHandler.itemTypes"/>
                                                             		<dspel:oparam name="Image">
									                                <% boolean noSuitableThumbnail = true; %>
										                          	<dspel:droplet name="/mngi/droplet/AvailableImageSizes">
										                          		<dspel:param name="imageId" param="element.repositoryId"/>
										                          		<dspel:oparam name="output">
										                          			<dspel:droplet name="ForEach">
										                          				<dspel:param name="array" param="sizes"/>
										                          				<dspel:oparam name="output">
										                          					<dspel:droplet name="Switch">
										                          						<dspel:param name="value" param="element"/>
										                          						<dspel:oparam name="100"><% noSuitableThumbnail = false; %></dspel:oparam>                      						
										                          					</dspel:droplet>
										                          				</dspel:oparam>
										                          			</dspel:droplet>
										                          		</dspel:oparam>
										                          	</dspel:droplet>
										                          	<table cellspacing="0" cellpadding="0">	
											                          	<tr><td valign="top">									                          	
												                          	<dsp:getvalueof id="imageUrl" idtype="String" param="element.url">
												                         		<%
												                         			if (noSuitableThumbnail) { %>
												                         				<img src="<%=imageUrl%>" onLoad="javascript:handleLargeOriginal(this);" style="border: 2px solid #ffffff;vertical-align:middle;" onError="javascript:handleThumbnailError(this);"> 
												                         			<% } else { %>
																						<img src="<%=imageUrl.replaceAll("(http.*?)(\\.(jpg|JPG|gif|GIF|png|PNG))", "$1_100$2")%>" style="border: 2px solid #ffffff;vertical-align:middle;" onError="javascript:handleThumbnailError(this);">
												                         			<% }
												                         		%>                                   	
												                         	</dsp:getvalueof>
												                         	</td>
												                         	<td valign="top" class="body">
													                         	&nbsp;<span id="<dspel:valueof param="element.atgId"/>"><dspel:valueof param="element.title"/> <!-- <dspel:valueof param="element.url"/> --></span> (<dspel:valueof param="element.repositoryId"/>)
		                                                          				<div id="<dspel:valueof param="element.atgId"/>imageURL" style="display:none"><dspel:valueof param="element.url"/></div>
												                         	</td>
												                         </tr>
											                         </table>												                        
				                   									</dspel:oparam>  
				                   									<dspel:oparam name="default">
				                   										&nbsp;<span id="<dspel:valueof param="element.atgId"/>"><dspel:valueof param="element.title"/> <!-- <dspel:valueof param="element.url"/> --></span> (<dspel:valueof param="element.repositoryId"/>)
	                                                         			<div id="<dspel:valueof param="element.atgId"/>imageURL" style="display:none"><dspel:valueof param="element.url"/></div>
				                   									</dspel:oparam>														                                                           		                                                              	
                                                             	</dspel:droplet>                                                             	                                                          
                                                          </td>
                                                          <td class="body" align="center" valign="top">
                                                              <dspel:valueof param="element.state.name"/>
                                                          </td>
                                                          <td class="body" valign="top">
                                                              &nbsp;<dspel:valueof param="element.createDate" converter="MngiDate"/>&nbsp;
                                                          </td>
                                                          <td class="body" valign="top">
                                                              &nbsp;<dspel:valueof param="element.startDate" converter="MngiDate"/>&nbsp;
                                                          </td>
                                                          <dspel:droplet name="Switch">
                                                              <dspel:param name="value" bean="SearchFormHandler.value.querySharedOnly"/>
                                                              <dspel:oparam name="true">
                                                                  <td class="body" valign="top">
                                                                      &nbsp;<dspel:valueof param="element.origSite.name"/>
                                                                  </td>
                                                              </dspel:oparam>
                                                          </dspel:droplet>
                                                      </tr>
                                                  </dspel:oparam>
                                                  <dspel:oparam name="default">
                                                      <!-- over the max results -->
                                                  </dspel:oparam>
                                              </dspel:droplet>
                                          </dspel:oparam>
                                          <dspel:oparam name="outputEnd">
                                              <dspel:droplet name="Switch">
                                                  <dspel:param name="value" bean="SearchFormHandler.value.lessThanMax"/>
                                                  <dspel:oparam name="true">
                                                      </table>
                                                      <div align="left">
                                                          <table>
                                                              <tr>
                                                                  <td>
                                                                      <table>
                                                                          <tr>
                                                                              <td class="buttonlink" onclick="handleAssociate();">Save</td>
                                                                          </tr>
                                                                      </table>
                                                                  </td>
                                                                  <td>
                                                                      <table>
                                                                          <tr>
                                                                              <td class="buttonlink" onclick="closePopUp();">Cancel</td>
                                                                          </tr>
                                                                      </table>
                                                                  </td>
                                                              </tr>
                                                          </table>
                                                      </div>
                                                  </dspel:oparam>
                                                  <dspel:oparam name="default">
                                                      <!-- over the max results -->
                                                  </dspel:oparam>
                                              </dspel:droplet>
                                          </dspel:oparam>
                                          <dspel:oparam name="empty">
                                              <hr style="color:black;height:1px;width:100%">
                                              <span class="tabletitle">Search Results &gt; <font color=red><i><dspel:valueof bean="SearchFormHandler.value.searchErrorMessage"/></i></font></span>
                                          </dspel:oparam>
                                      </dspel:droplet>
                                  </td>
                              </tr>
                          </table>
                      </td>
                  </tr>
              </dspel:oparam>
          </dspel:droplet>
      </table>
      
      <dspel:valueof bean="SearchFormHandler.resetFormExceptions"/>
      
      <dspel:input type="hidden" id="forceDataReset" bean="SearchFormHandler.forceDataReset" priority="99999" />
      
      </dspel:form>
      
      <script type="text/javascript" language="JavaScript">
      
      // this registers this form for "enter key" activation:
      registerEnterKeyHandler("searchForm", "searchFormButton");
      
      var inputCreateDateMin = getById("createDateMin");
      var inputCreateDateMax = getById("createDateMax");
      var inputStartDateMin = getById("startDateMin");
      var inputStartDateMax = getById("startDateMax");
      
      var inputFilterBySelect = getById("filterBySelect");
      var inputFilterByState = getById("filterByState");
      
      var inputQuerySharedOnlyTrue = getById("querySharedOnlyTrue");
      var inputQuerySharedOnlyFalse = getById("querySharedOnlyFalse");
      
      var inputIncludeExpiredTrue = getById("includeExpiredTrue");
      var inputIncludeExpiredFalse = getById("includeExpiredFalse");
      
      var inputIncludeDeletedTrue = getById("includeDeletedTrue");
      var inputIncludeDeletedFalse = getById("includeDeletedFalse");
      
      var inputForceDataReset = getById("forceDataReset");
      
      //////////////////////////////////////////////////
      
      function clickSearchButton() {
          // only when the "Search" button is clicked,
          // not when the auto-search runs on page load...
          inputForceDataReset.value = "FORCE_RESET";
          clickIt('searchFormButton');
      }
      
      function updateSearchGUI() {
          
          <%
          Calendar todayMin = new GregorianCalendar();
          todayMin.set(Calendar.AM_PM,Calendar.AM);
          todayMin.set(Calendar.HOUR,0);
          todayMin.set(Calendar.MINUTE,0);
          todayMin.set(Calendar.SECOND,0);
          Calendar todayMax = new GregorianCalendar();
          todayMax.set(Calendar.AM_PM,Calendar.PM);
          todayMax.set(Calendar.HOUR,11);
          todayMax.set(Calendar.MINUTE,59);
          todayMax.set(Calendar.SECOND,59);
          SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
          %>
          // SEARCH:
          
          var FILTER_BLANK = "0";
          var FILTER_CREATED_TODAY = "1";
          var FILTER_CREATED_IN_LAST_TWO_DAYS = "2";
          var FILTER_CREATED_IN_LAST_SEVEN_DAYS = "3";
          var FILTER_CREATED_IN_LAST_THIRTY_DAYS = "4";
          var FILTER_CREATED_IN_LAST_SIXTY_DAYS = "5";
          var FILTER_STARTS_TODAY = "6";
          var FILTER_STARTS_IN_NEXT_SEVEN_DAYS = "7";
          var FILTER_EXPIRED = "8";
          var FILTER_ALL_ITEMS = "9";
          
          var FILTER_STATE_NONE_SELECTED = "";
          var FILTER_STATE_DELETED = "DELETED";
          var FILTER_STATE_EXPIRED = "EXPIRED";
          
          var createDateMinValue = inputCreateDateMin.value;
          var createDateMaxValue = inputCreateDateMax.value;
          var startDateMinValue = inputStartDateMin.value;
          var startDateMaxValue = inputStartDateMax.value;
          
          inputIncludeExpiredTrue.disabled=false;
          inputIncludeExpiredFalse.disabled=false;
          inputIncludeDeletedTrue.disabled=false;
          inputIncludeDeletedFalse.disabled=false;
          
          switch(inputFilterBySelect.value) {
              case FILTER_BLANK:
                  createDateMinValue = "";
                  createDateMaxValue = "";
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  break;
              case FILTER_CREATED_TODAY:
                  createDateMinValue = "<%=sdf.format(todayMin.getTime())%> " + timeZone;
                  createDateMaxValue = "<%=sdf.format(todayMax.getTime())%> " + timeZone;
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  break;
              case FILTER_CREATED_IN_LAST_TWO_DAYS:
                  <%
                  todayMin.add(Calendar.DATE, -2);
                  %>
                  createDateMinValue = "<%=sdf.format(todayMin.getTime())%> " + timeZone;
                  createDateMaxValue = "<%=sdf.format(todayMax.getTime())%> " + timeZone;
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  break;
              case FILTER_CREATED_IN_LAST_SEVEN_DAYS:
                  <%
                  todayMin.add(Calendar.DATE, -5);
                  %>
                  createDateMinValue = "<%=sdf.format(todayMin.getTime())%> " + timeZone;
                  createDateMaxValue = "<%=sdf.format(todayMax.getTime())%> " + timeZone;
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  break;
              case FILTER_CREATED_IN_LAST_THIRTY_DAYS:
                  <%
                  todayMin.add(Calendar.DATE, -23);
                  %>
                  createDateMinValue = "<%=sdf.format(todayMin.getTime())%> " + timeZone;
                  createDateMaxValue = "<%=sdf.format(todayMax.getTime())%> " + timeZone;
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  break;
              case FILTER_CREATED_IN_LAST_SIXTY_DAYS:
                  <%
                  todayMin.add(Calendar.DATE, -30);
                  %>
                  createDateMinValue = "<%=sdf.format(todayMin.getTime())%> " + timeZone;
                  createDateMaxValue = "<%=sdf.format(todayMax.getTime())%> " + timeZone;
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  break;
              case FILTER_STARTS_TODAY:
                  <%
                  todayMin.add(Calendar.DATE, 60);
                  %>
                  createDateMinValue = "";
                  createDateMinValue = "";
                  startDateMinValue = "<%=sdf.format(todayMin.getTime())%> " + timeZone;
                  startDateMaxValue = "<%=sdf.format(todayMax.getTime())%> " + timeZone;
                  break;
              case FILTER_STARTS_IN_NEXT_SEVEN_DAYS:
                  <%
                  todayMax.add(Calendar.DATE, 7);
                  %>
                  createDateMinValue = "";
                  createDateMinValue = "";
                  startDateMinValue = "<%=sdf.format(todayMin.getTime())%> " + timeZone;
                  startDateMaxValue = "<%=sdf.format(todayMax.getTime())%> " + timeZone;
                  break;
              case FILTER_EXPIRED:
                  createDateMinValue = "";
                  createDateMinValue = "";
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  inputIncludeExpiredTrue.checked=true;
                  inputIncludeExpiredFalse.disabled=true;
                  break;
              case FILTER_ALL_ITEMS:
                  createDateMinValue = "";
                  createDateMaxValue = "";
                  startDateMinValue = "";
                  startDateMaxValue = "";
                  break;
          }
          
          inputCreateDateMin.value = createDateMinValue;
          inputCreateDateMax.value = createDateMaxValue;
          inputStartDateMin.value = startDateMinValue;
          inputStartDateMax.value = startDateMaxValue;
      }
      
      // make sure GUI reflects state:
      updateSearchGUI();
      
      // adjust window size:
      // access denied error if window not-in-focus:
      //window.focus();
      //window.resizeTo(1024,768);
      
      ping();
      
      </script>
      
      </body>
      </html>
    </dsp:oparam>
    <dsp:oparam name="default">
        <html>
        <head>
        <title>Session Invalid or has Expired</title>
        <NOSCRIPT><META http-equiv="refresh" content="0; URL=<dsp:valueof bean="IsAuthenticated.context"/><dsp:valueof bean="IsAuthenticated.errorURL"/>"></NOSCRIPT>
        <script language="JavaScript" src="/dashboard/html/js/new_controls.js" type="text/javascript"></script>
        <script language="JavaScript" type="text/javascript">
        function handleNoSession() {
            var controller = connect();
            if (controller && controller != undefined && controller != null) {
              alert("ERROR: Session is invalid.\n\nThis program will attempt to restart the dashboard.");
              controller.restartApplication();
            } else {
                // controller win does not exist, is out of scope, or someone trying a direct url hack:
                var context = "<dsp:valueof bean="IsAuthenticated.context"/>";
                var errorUrl = "<dsp:valueof bean="IsAuthenticated.errorURL"/>";
                var newLocation = context + errorUrl;
                alert("ERROR: Session is invalid and this window has failed to find the controller application.\n\nThis program will attempt to restart the dashboard.");
                try {
                  window.opener.restartApplication();
                } catch (error) {
                  alert("This program was unable to recover from an error\n\n" + error.message + "\n\nPlease close all browser windows and start the dashboard.");
                }
                self.close();
            }
        }
        </script>
        
        </head>
        <body onload="handleNoSession();">
        ERROR: Session does not exist.
        </body>
        </html>
    </dsp:oparam>
</dsp:droplet>

</dspel:page>
