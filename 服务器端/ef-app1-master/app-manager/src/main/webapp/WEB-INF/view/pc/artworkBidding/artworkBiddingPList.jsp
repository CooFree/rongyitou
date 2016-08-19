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
  <title>订单列表</title>
</head>
<body style="height: auto">
<div class="am-cf am-padding">
  <div class="am-fl am-cf">
    <strong class="am-text-primary am-text-lg">订单列表</strong>
  </div>
</div>

<table class="am-table am-table-bordered am-table-radius am-table-striped">
  <tr style="text-align:left">
    <td>操作</td>
    <td>项目名称</td>
    <td>拍品用户/中奖用户</td>
    <td>成交金额（元）</td>
    <td>当前状态</td>
    <td>付款时间</td>
    <td>发放时间</td>
  </tr>

  <c:forEach items="${requestScope.pageInfo.list}" var="artworkBidding">
    <tr style="text-align:left">
      <td>操作</td>
      <td>${artworkBidding.artwork.title}</td>
      <td>${artworkBidding.creator.name}</td>
      <td>
        <fmt:formatNumber value="${artworkBidding.price}" pattern="##.##" minFractionDigits="2"/>元
      </td>
      <td>
        <ming800:status name="status" dataType="ArtworkBidding.type" checkedValue="${artworkBidding.status}" type="normal"/>
      </td>
      <td>
        <fmt:formatDate value="${artworkBidding.createDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
      </td>
      <td>${artworkBidding.createDatetime}</td>
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
