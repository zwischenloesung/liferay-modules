<%--
    init.jsp: Common imports and setup code of the map-search-portlet
    
    Created:    2016-07-21 22:27 by Christian Berndt
    Modified:   2016-07-27 12:21 by Christian Berndt
    Version:    1.0.2
--%>

<%-- Import required classes --%>
<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="com.liferay.portlet.documentlibrary.model.DLFileEntry"%>
<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>

<%-- Import required taglibs --%>

<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/inofix-util" prefix="ifx-util" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>

<%-- Common setup code, required by any jsp --%>

<portlet:defineObjects />
<theme:defineObjects />

<%
    String currentURL = PortalUtil.getCurrentURL(request);

    // Remove any actionParameters from the currentURL
    currentURL = HttpUtil.removeParameter(currentURL,
            renderResponse.getNamespace() + "javax.portlet.action");

    String classNames = portletPreferences.getValue("classNames", JournalArticle.class.getName() + StringPool.NEW_LINE + DLFileEntry.class.getName());
    String mapCenter = portletPreferences.getValue("mapCenter", "[47.05207, 8.30585]");
    String mapHeight = portletPreferences.getValue("mapHeight", "400px");
    String mapZoom = portletPreferences.getValue("mapZoom", "13");
    String markerIconConfig = portletPreferences.getValue("markerIconConfig", "");   
    String tilesCopyright = portletPreferences.getValue("tilesCopyright", "&copy; <a href=\"http://osm.org/copyright\" target=\"_blank\">OpenStreetMap</a> contributors");
    String tilesURL = portletPreferences.getValue("tilesURL", "http://{s}.tile.osm.org/{z}/{x}/{y}.png");
    boolean useDivIcon = GetterUtil.getBoolean(portletPreferences.getValue("useDivIcon", "false"));
    boolean useGlobalJQuery = GetterUtil.getBoolean(portletPreferences.getValue("useGlobalJQuery", "false"));    
    boolean viewByDefault = GetterUtil.getBoolean(portletPreferences.getValue("viewByDefault", "false"));
    boolean viewInContext = GetterUtil.getBoolean(portletPreferences.getValue("viewInContext", "false"));
             
 %>