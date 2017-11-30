<%--
    latest.jsp: Display the latest state of the configured channels
    
    Created:    2017-04-11 17:45 by Christian Berndt
    Modified:   2017-11-30 15:46 by Christian Berndt
    Version:    1.0.8
 --%>


<%@ include file="/html/init.jsp"%>

<%
    Format format = FastDateFormatFactoryUtil.getDateTime(
            DateFormat.MEDIUM, DateFormat.MEDIUM, locale, timeZone);
%>

<c:choose>
    <c:when test="<%= channelIdTermCollectors.size() == 0 %>">
        <div class="alert alert-info">
            <liferay-ui:message key="no-channel-data-found"/>
        </div>
    </c:when>
    <c:otherwise>
        <aui:row>
            <%
                int i = 0;
            
                for (TermCollector termCollector : channelIdTermCollectors) {
                    
                    Sort sort = new Sort(DataManagerFields.TIMESTAMP, true); 
                    
                    Hits hits = MeasurementLocalServiceUtil.search(
                            themeDisplay.getCompanyId(),
                            themeDisplay.getScopeGroupId(),
                            termCollector.getTerm(), 0,
                            new Date().getTime(), true, 0, 1, sort);
    
                    if (hits.getLength() > 0) {
                        
                        Document document = hits.toList().get(0);
        
                        id = termCollector.getTerm();
                        long timestamp = GetterUtil.getLong(document.get(DataManagerFields.TIMESTAMP));
        
                        PortletURL graphURL = renderResponse
                                .createRenderURL();
                        graphURL.setParameter("tabs1", "chart");
                        graphURL.setParameter("id", id);
                        graphURL.setParameter("from", String.valueOf(timestamp + 1 - oneWeek));
                        graphURL.setParameter("until", String.valueOf(timestamp + 1));
            %>
                <aui:col span="3">
                    <div class="display">
                        <div class="name"><%= id %></div> 
                        <a href="<%= graphURL.toString() %>">          
                            <span class="face">
                                <span class="value-wrapper">
                                    <span class="value"><%= document.get(DataManagerFields.VALUE) %></span><br/>
                                    <span class="unit"><%= document.get(DataManagerFields.UNIT) %></span> 
                                </span>
                            </span>
                        </a> 
                        <div class="caption">
                            <%= format.format(new Date(timestamp)) %> 
                        </div>
                    </div>
                </aui:col>
                
                <c:if test="<%= i > 0 && (i+1)%4 == 0 %>">
                    <%= "</div><div class='row-fluid'>" %>
                </c:if>    
            <%
                        i++;
                    }
                }
            %>
        </aui:row>
    </c:otherwise>
</c:choose>
