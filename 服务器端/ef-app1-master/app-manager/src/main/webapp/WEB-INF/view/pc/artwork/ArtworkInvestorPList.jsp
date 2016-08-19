<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/4/5
  Time: 17:00
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
    <title></title>
    <script type="text/javascript" src="<c:url value='/scripts/recommended.js'/>"></script>
</head>
<body>
<div>项目名称:${object.title}</div>
<div>发起人:${object.author.name}</div>
<div>目标金额:${object.investGoalMoney}</div>
<div>当前节点:<ming800:status name="orderType" dataType="Artwork.type"
                          checkedValue="${object.type}"
                          type="normal"/></div>
<div>项目创建时间:${object.investEndDatetime}</div>
<div>投资者信息</div>
<table>
    <tr>
        <td>排序</td>
        <td>投资者</td>
        <td>投资金额</td>
        <td>投资时间</td>
    </tr>
    <c:forEach items="${object.artworkInvests}" var="investor" varStatus="step">
        <tr>
        <td>${step.index + 1}</td>
            <td>${investor.creator.name}</td>
            <td>${investor.price}</td>
            <td>${investor.createDatetime}</td>
        </tr>
    </c:forEach>
</table>
<script>
    window.onload = function () {

        <% if (request.getParameter("message") != null && !"".equalsIgnoreCase(request.getParameter("message")))
         {
        %>
        alert("<%=request.getParameter("message")%>");
        <% } %>
    }
    function myRemove(id, clazz) {
        $.ajax({
            type: "get",
            url: '<c:url value="/remove.do?id="/>' + id + "&clazz=" + clazz + '',
            cache: false,
//            dataType: "json",
            data: {id: id},
            success: function (data) {
                console.log(data);
                $("#" + id).remove();
            },
            error: function (a) {
                console.log(eval(a).responseText);
            }
        });
    }
    function changeStatus(obj, id) {
        var status = $(obj).attr("status");
        $.ajax({
            type: "get",
            url: '<c:url value="/product/project/updateStatus.do"/>',
            cache: false,
            dataType: "json",
            data: {id: id, status: status},
            success: function (data) {
                $(obj).attr("status", data);
                if (status == "1") {
                    $(obj).find("span").text("隐藏");
                    $(obj).attr("status", "2");
                }
                if (status == "2") {
                    $(obj).find("span").text("显示");
                    $(obj).attr("status", "1");
                }
            }
        });
    }

</script>
</body>
</html>

