<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/4/5
  Time: 18:40
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>

<html>
<head>
  <title>艺术家审核</title>
  <script src="<c:url value='/scripts/function.js'/>"></script>
</head>
<body  style="height: auto">
<div class="am-cf am-padding">
  <div class="am-fl am-cf">
    <strong class="am-text-primary am-text-lg">艺术家审核列表</strong>
  </div>
</div>
<jsp:include page="/layouts/myConfirm.jsp"/>
<jsp:include page="/layouts/myReject.jsp"/>

<table class="am-table am-table-bordered am-table-radius am-table-striped">
  <tr style="text-align:left">
    <td width="20%">操作</td>
    <td width="15%">艺术家</td>
    <td width="10%">联系方式</td>
    <td width="10%">头衔认证</td>
    <td width="15%">当前状态</td>
    <td width="10%">加入时间</td>
  </tr>

  <c:forEach items="${requestScope.pageInfo.list}" var="artist">
    <tr style="text-align: left">
      <td>
        <div class="am-btn-toolbar">
          <div class="am-btn-group am-btn-group-xs" style="width: 100%;" >
            <button onclick="myConfirm('<c:url value="/checkArtist/remove.do?id=${artist.id}"/>', 'D')"
                    <%--onclick="window.location.href='<c:url value="/checkProject/remove.do?id=${artwork.id}"/>'"--%>
                    class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">删除</button>
            <button onclick="window.location.href='<c:url value="/basic/xm.do?qm=viewCheckMaster&checkMaster=checkMaster&id=${artist.id}"/>'"
                    class="am-btn am-btn-default am-btn-xs am-hide-sm-only">查看</button>
            <c:if test="${artist.theStatus == '1'}">
              <button onclick="window.location.href='<c:url value="/checkArtist/checkPass.do?id=${artist.id}&type=${artist.theStatus}&resultPage=L"/>'"
                      class="am-btn am-btn-default am-btn-xs am-hide-sm-only">标记</button>
            </c:if>
            <c:if test="${artist.theStatus == '2'}">
              <button onclick="window.location.href='<c:url value="/checkArtist/checkPass.do?id=${artist.id}&type=${artist.theStatus}&resultPage=L"/>'"
                      class="am-btn am-btn-default am-btn-xs am-hide-sm-only">通过</button>
              <button onclick="myReject('<c:url value="/checkArtist/checkReject.do?id=${artist.id}&resultPage=L"/>')"
                      <%--onclick="window.location.href='<c:url value="/checkProject/checkReject.do?id=${artwork.id}&resultPage=L"/>'"--%>
                      class="am-btn am-btn-default am-btn-xs am-hide-sm-only">驳回</button>
            </c:if>
            <%--<c:if test="${artwork.step == '12'}">--%>
              <%--<button onclick="window.location.href='<c:url value=""/>'"--%>
                      <%--class="am-btn am-btn-default am-btn-xs am-hide-sm-only">撤销通过</button>--%>
            <%--</c:if>--%>
          </div>
        </div>
      </td>
      <td>${artist.user.name}</td>
      <td>${artist.user.username}</td>
      <td>${artist.titleCertificate}</td>
      <td>
        <ming800:status name="status" dataType="Artist.status" checkedValue="${artist.theStatus}" type="normal"/>
      </td>
      <td>
        <fmt:formatDate value="${artist.user.createDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
      </td>
    </tr>
  </c:forEach>
</table>
<div style="clear: both">
  <c:url value="/basic/xm.do" var="url" />
  <ming800:pcPageList bean="${requestScope.pageInfo.pageEntity}" url="${url}">
    <ming800:pcPageParam name="qm" value="${requestScope.qm}"/>
    <ming800:pcPageParam name="conditions" value="${requestScope.conditions}"/>
  </ming800:pcPageList>
</div>

</body>
</html>
