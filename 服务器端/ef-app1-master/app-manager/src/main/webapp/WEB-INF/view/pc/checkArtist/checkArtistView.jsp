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
    <title>艺术家详情</title>
    <script src="<c:url value='/scripts/function.js'/>"></script>
</head>
<body>
<div class="am-cf am-padding">
    <div class="am-fl am-cf">
        <strong class="am-text-primary am-text-lg">
            当前状态:<ming800:status name="step" dataType="Artist.status" checkedValue="${object.theStatus}" type="normal"/>
        </strong>
    </div>
</div>
<jsp:include page="/layouts/myConfirm.jsp"/>
<jsp:include page="/layouts/myReject.jsp"/>

<div class="am-g">
    <form class="am-form am-form-horizontal">
        <input type="hidden" name="id" value="${object.id}">
        <input type="hidden" name="status" value="${object.theStatus}" />

        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">申请艺术家 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.user.name}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">联系方式 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.user.username}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">微信号 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">微信号</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">手艺类别 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.artCategory}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">头衔认证 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.titleCertificate}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">详细地址 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">${object.provinceName}</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">身份证正反面照片 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">身份证正反面照片</div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">艺术家工作室照片 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">
                    <c:forEach items="${object.workShopPhotos}" var="workShopPhoto">
                        <img src="${workShopPhoto.url}"/>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">艺术家作品 <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">
                    <c:forEach items="${object.worksPhotos}" var="worksPhoto">
                        <img src="${worksPhoto.url}"/>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="am-form-group">
            <label class="am-u-sm-3 am-form-label">获奖/资格证书（非必填） <small>:</small></label>
            <div class="am-u-sm-9">
                <div style="margin-top: 10px">获奖/资格证书（非必填）</div>
            </div>
        </div>

        <div class="am-form-group">
            <div class="am-u-sm-9 am-u-sm-push-3">
                <c:if test="${object.theStatus == '1'}">
                    <%-- 如果艺术家状态是-待审核 --%>
                    <input onclick="window.location.href='<c:url value="/checkArtist/checkPass.do?id=${object.id}&type=${object.theStatus}&resultPage=V"/>'"
                           type="button" class="am-btn am-btn-primary" value="标记"/>
                </c:if>
                <c:if test="${object.theStatus == '2'}">
                    <%-- 如果艺术家状态是-审核中 --%>
                    <input onclick="window.location.href='<c:url value="/checkArtist/checkPass.do?id=${object.id}&type=${object.theStatus}&resultPage=V"/>'"
                           type="button" class="am-btn am-btn-primary" value="通过"/>
                    <input onclick="myReject('<c:url value="/checkArtist/checkReject.do?id=${object.id}&resultPage=V"/>')"
                           <%--onclick="window.location.href='<c:url value="/checkProject/checkReject.do?id=${artwork.id}&resultPage=V"/>'"--%>
                           type="button" class="am-btn am-btn-primary am-text-danger" value="驳回"/>
                </c:if>
                <input onclick="myConfirm('<c:url value="/checkArtist/remove.do?id=${object.id}"/>', 'D')"
                       <%--onclick="window.location.href='<c:url value="/checkProject/remove.do?id=${object.id}"/>'"--%>
                       type="button" class="am-btn am-btn-primary am-text-danger" value="删除"/>
            </div>
        </div>
    </form>
</div>

</body>
</html>
