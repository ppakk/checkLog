<%@ taglib uri="/dsp" prefix="dsp"%>
<%@ taglib uri="/mngi_util" prefix="util"%>
<dsp:page>
<!-- BEGIN FREEFORM RENDER, ID <dsp:valueof param="id"/> -->
<dsp:droplet name="/mngi/portlet/freeform/droplet/GSAFreeformDroplet">
	<dsp:param name="id" param="id"/>
	<dsp:param name="query" param="query"/>
	<dsp:param name="isPreviewing" param="isPreviewing"/>
	<dsp:param name="siteId" param="siteId"/>
	<dsp:param name="settingPropId" param="settingPropId"/>
	<dsp:oparam name="output">
	
		<dsp:droplet name="/atg/dynamo/droplet/IsEmpty">
			<dsp:param name="value" param="css"/>
			<dsp:oparam name="true">
				<dsp:getvalueof id="css" idtype="java.lang.String" param="css">
				<util:availablecss path="<%=css%>">
					<dsp:valueof param="element.html" converter="valueishtml"/>
				</util:availablecss>
				</dsp:getvalueof>
			</dsp:oparam>
			<dsp:oparam name="false">
			<dsp:valueof param="element.html" converter="valueishtml"/>
			</dsp:oparam>
		</dsp:droplet>
		
	</dsp:oparam>
	<dsp:oparam name="configInvalid">
		<!-- FREEFORM RENDER, siteId <dsp:valueof param="siteId"/>, settingPropId <dsp:valueof param="settingPropId"/> INVALID! -->
	</dsp:oparam>
	<dsp:oparam name="error">
		<!-- FREEFORM RENDER, ID <dsp:valueof param="id"/> ERRORED! -->
	</dsp:oparam>
	<dsp:oparam name="invalid">
		<!-- FREEFORM WAS NOT VALID.... -->
	</dsp:oparam>
	<dsp:oparam name="empty">
		<!-- FREEFORM RENDER, ID <dsp:valueof param="id"/> NOT FOUND -->
	</dsp:oparam>
</dsp:droplet>
<!-- END FREEFORM RENDER, ID <dsp:valueof param="id"/> -->	
</dsp:page>