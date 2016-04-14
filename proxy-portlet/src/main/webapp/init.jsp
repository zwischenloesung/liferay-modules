<%--
    init.jsp: Common imports and setup code of the proxy-portlet
    
    Created:    2016-04-14 11:48 by Christian Berndt
    Modified:   2016-04-14 11:48 by Christian Berndt
    Version:    1.0.0
--%>

<%-- Import required classes --%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>


<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>

<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    String[] hosts = portletPreferences.getValues("hosts", new String[] {""});
%>
