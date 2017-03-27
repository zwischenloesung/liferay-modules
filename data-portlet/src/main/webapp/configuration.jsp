<%--
    configuration.jsp: Configure the data-manager's preferences.
    
    Created:    2017-03-13 16:32 by Christian Berndt
    Modified:   2017-03-27 23:34 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/html/init.jsp"%>

<%@page import="java.util.Arrays"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>

<%
    PortletURL portletURL = renderResponse.createRenderURL();

    List<KeyValuePair> selectedColumns = new ArrayList<KeyValuePair>();
    for (String column : columns) {
        selectedColumns.add(new KeyValuePair(column, column));
    }

    Arrays.sort(columns); 
    
    List<KeyValuePair> availableColumns = new ArrayList<KeyValuePair>();
    for (String headerName : headerNames) {
        if (Arrays.binarySearch(columns, headerName) < 0) {
            availableColumns.add(new KeyValuePair(headerName,
                    headerName));
        }
    }
%>

<liferay-portlet:actionURL portletConfiguration="true"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="true"
    var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm"
    onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>
    <aui:input name="<%=Constants.CMD%>" type="hidden"
        value="<%=Constants.UPDATE%>" />
    <aui:input name="redirect" type="hidden"
        value="<%=configurationRenderURL%>" />

    <liferay-ui:panel-container
        id="datamanagerSettingsPanelContainer"
        persistState="<%=true%>">
        
        <liferay-ui:panel id="datamanagerDataPanel"
            title="data" extended="true">

            <aui:fieldset>

                <aui:input name="preferences--dataURL--"
                    helpMessage="data-url-help"
                    value="<%= dataURL %>" />

            </aui:fieldset>

        </liferay-ui:panel>        

        <liferay-ui:panel id="datamanagerColumnsPanel"
            title="columns" extended="true">

            <aui:fieldset>

                <%-- 
                <aui:input name="preferences--headerNames--"
                    helpMessage="header-names-help"
                    value="<%=StringUtil.merge(headerNames, StringPool.COMMA) %>" />
                --%>
            
                <aui:input name="preferences--columns--" type="hidden" />
                
                <liferay-ui:input-move-boxes
                    rightList="<%=availableColumns%>"
                    rightTitle="available" 
                    leftBoxName="selectedColumns"
                    leftList="<%=selectedColumns%>"
                    rightBoxName="availableColumns" 
                    leftTitle="current"
                    leftReorder="true" />

            </aui:fieldset>

        </liferay-ui:panel>
        
        <liferay-ui:panel id="datamanagerMiscellaneousPanel"
            title="miscellaneous" extended="true">

            <aui:fieldset>

                <aui:select name="preferences--paginationType--">
                    <aui:option value="approximate" label="approximate" selected='<%= "approximate".equals(paginationType) %>'/>
                    <aui:option value="article" label="article" selected='<%= "article".equals(paginationType) %>'/>
                    <aui:option value="more" label="more" selected='<%= "more".equals(paginationType) %>'/>
                    <aui:option value="regular" label="regular" selected='<%= "regular".equals(paginationType) %>'/>
                </aui:select>

            </aui:fieldset>

        </liferay-ui:panel>

    </liferay-ui:panel-container>

    <aui:button-row>
        <aui:button type="submit" />
    </aui:button-row>
</aui:form>

<aui:script>
    Liferay.provide(window, '<portlet:namespace />saveConfiguration',
        function() {
            if (document.<portlet:namespace />fm.<portlet:namespace />columns) {        
                document.<portlet:namespace />fm.<portlet:namespace />columns.value = Liferay.Util.listSelect(document.<portlet:namespace />fm.<portlet:namespace />selectedColumns);
            }
            submitForm(document.<portlet:namespace />fm);
        }, ['liferay-util-list-fields']
    );
</aui:script>
