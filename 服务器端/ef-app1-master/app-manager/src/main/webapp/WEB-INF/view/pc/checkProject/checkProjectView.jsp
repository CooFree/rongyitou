<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/7/22
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ming800" uri="http://java.ming800.com/taglib" %>
<html>
<head>
    <title>项目详情</title>
    <script src="<c:url value='/scripts/function.js'/>"></script>
</head>
<body>
<div class="am-cf am-padding">
    <div class="am-fl am-cf">
        <strong class="am-text-primary am-text-lg">
            当前状态:<ming800:status name="step" dataType="Artwork.step" checkedValue="${object.step}" type="normal"/>
        </strong>
    </div>
</div>
<jsp:include page="/layouts/myConfirm.jsp"/>
<jsp:include page="/layouts/myReject.jsp"/>

<div class="am-g">
    <form class="am-form am-form-horizontal">
        <input type="hidden" name="id" value="${object.id}">
        <input type="hidden" name="status" value="${object.status}" />

        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">项目标题 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.title}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">项目目标 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.investGoalMoney}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">项目发起人 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.author.name}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">项目发起人联系方式 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.author.username}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">项目介绍 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">
                    <span style="margin-top: 10px">${object.brief}</span>
                </div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">项目详情 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">
                    <span style="margin-top: 10px">${object.description}</span>
                </div>
            </div>
        </div>
        <div class="am-form-group">
            <div class="am-u-sm-9 am-u-sm-push-3">
                <c:if test="${object.step == '10'}">
                    <%-- 如果项目状态是-待审核 --%>
                    <input onclick="window.location.href='<c:url value="/checkProject/checkPass.do?id=${object.id}&type=${object.step}&resultPage=V"/>'"
                           type="button" class="am-btn am-btn-primary" value="标记"/>
                </c:if>
                <c:if test="${object.step == '11'}">
                    <%-- 如果项目状态是-审核中 --%>
                    <input onclick="window.location.href='<c:url value="/checkProject/checkPass.do?id=${object.id}&type=${object.step}&resultPage=V"/>'"
                           type="button" class="am-btn am-btn-primary" value="通过"/>
                    <input onclick="myReject('<c:url value="/checkProject/checkReject.do?id=${object.id}&resultPage=V"/>')"
                           <%--onclick="window.location.href='<c:url value="/checkProject/checkReject.do?id=${artwork.id}&resultPage=V"/>'"--%>
                           type="button" class="am-btn am-btn-primary am-text-danger" value="驳回"/>
                </c:if>
                <input onclick="myConfirm('<c:url value="/checkProject/remove.do?id=${object.id}"/>', 'D')"
                       <%--onclick="window.location.href='<c:url value="/checkProject/remove.do?id=${object.id}"/>'"--%>
                       type="button" class="am-btn am-btn-primary am-text-danger" value="删除"/>
            </div>
        </div>
    </form>
</div>

</body>
</html>
