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
<jsp:include page="/do/generateTabs.do?qm=${requestScope.qm}&conditions=${requestScope.conditions}"/>
<jsp:include page="/layouts/myConfirm.jsp"/>
<jsp:include page="/layouts/myReject.jsp"/>
<table>
    <tr>
        <td width="25%">操作</td>
        <td width="10%">项目名称</td>
        <td width="10%">发起人</td>
        <td width="15%">当前状态</td>
        <td width="10%">融资金额(元)</td>
        <td width="10%">投资人数</td>
        <td width="5%">排序</td>
        <td width="15%">项目创建时间</td>
    </tr>

    <c:forEach items="${requestScope.pageInfo.list}" var="artwork">
        <tr>
            <td>
                <div class="am-btn-toolbar">
                    <div class="am-btn-group am-btn-group-xs" style="width: 100%;">
                        <button onclick="showConfirm('提示','删除项目将会关联删除一切相关记录，确定删除吗',function(){myRemove('${artwork.id}','com.efeiyi.ec.art.model.Artwork')})"
                                class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"> 删除
                        </button>
                        <button onclick="window.location.href = '/basic/xm.do?qm=viewArtwork&id=${artwork.id}';"
                                class="am-btn am-btn-default am-btn-xs am-hide-sm-only"> 查看
                        </button>
                        <button onclick="showConfirm('提示','排序是什么鬼？',function(){alert('unknown operation');})"
                                class="am-btn am-btn-default am-btn-xs am-hide-sm-only"> 排序
                        </button>
                        <c:if test="${artwork.step == 23}">
                            <span id="${artwork.id}">
                                <button onclick="showConfirm('提示','确定项目完成吗',function(){changeStatus('${artwork.id}',{'id': '${artwork.id}', 'clazz': 'com.efeiyi.ec.art.model.Artwork','step':'30'})})"
                                        class="am-btn am-btn-default am-btn-xs am-hide-sm-only"> 确认完成
                                </button>
                                <button onclick="myReject('${artwork.id}','com.efeiyi.ec.art.model.Artwork','25')"
                                        class="am-btn am-btn-default am-btn-xs am-hide-sm-only"> 驳回
                                </button>
                            </span>
                        </c:if>
                    </div>
                </div>
            </td>
            <td>${artwork.title}</td>
            <td>${artwork.author.name}</td>
            <td><ming800:status name="type" dataType="Artwork.type"
                                checkedValue="${artwork.type}"
                                type="normal"/>
                <font color="red"><ming800:status name="step" dataType="Artwork.step"
                                checkedValue="${artwork.step}"
                                type="normal"/></font></td>
            <td>${artwork.investsMoney}/${artwork.investGoalMoney}</td>
            <td>${artwork.auctionNum}</td>
            <td>${artwork.sorts}</td>
            <td>${artwork.createDatetime}</td>
        </tr>
    </c:forEach>
</table>
<div style="clear: both">
    <ming800:pcPageList bean="${requestScope.pageInfo.pageEntity}" url="/basic/xm.do">
        <ming800:pcPageParam name="qm" value="${requestScope.qm}"/>
        <ming800:pcPageParam name="conditions" value="${requestScope.conditions}"/>
    </ming800:pcPageList>
</div>
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
            url: '<c:url value="/remove.do"/>',
            cache: false,
//            dataType: "json",
            data: {"id": id, "clazz": clazz},
            success: function (data) {
                console.log(data);
                $("#" + id).remove();
            },
            error: function (a) {
                console.log(eval(a).responseText);
            }
        });
        e
    }
    function changeStatus(id, jsonData) {
        $.ajax({
            type: "get",
            url: '<c:url value="/updateObject.do"/>',
            cache: false,
//            dataType: "json",
            data: jsonData,
            success: function (data) {
                if ($("#" + id) != "undefine") {
                    $("#" + id).remove();
                }
            },
            error: function (a, b, c) {
                console.log(eval(a).responseText);
            }
        });
    }
    function myReject(id, clazz, step) {
        $('#my-reject').modal({
            relatedTarget: this,
            onConfirm: function (e) {
                var message = e.data || "";
                if (null == message || message.trim() == "") {
                    alert("驳回意见为必填项!");
                } else {
                    var jsonDate = {"id": id, "clazz": clazz, "step": step, "feedback": message};
                    changeStatus(id, jsonDate);
                }
            },
            onCancel: function (e) {
            }
        });
    }
</script>
</body>
</html>

