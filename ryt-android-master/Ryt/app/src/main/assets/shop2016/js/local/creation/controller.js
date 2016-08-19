/**
 * Created by Administrator on 2016/5/31 0031.
 */
//页面的初始化和渲染页面(统一调配函数)
function initPage(artWorkId, currentUserId) {
    var paramStr = window.demo.fetchParamObject();
    var paramObject = JSON.parse(paramStr);
    var param = new Object();
    param.artWorkId = paramObject.artWorkId;
    param.currentUserId = paramObject.currentUserId;
    console.log("==============================");
    console.log("param.artWorkId"+param.artWorkId+"param.currentUserId"+param.currentUserId);
    console.log("==============================");
    PageVariable.param = param;
    PageVariable.artWorkId = param.artWorkId;
    getArtWorkBaseInfoData(getArtWorkBaseInfo);
    getArtWorkDetailData(getArtWorkDetail);
}
//获得项目基本信息的controller
function getArtWorkBaseInfo() {
    var artWorkInfo = PageVariable.artWorkInfo;
    var artWorkProject = PageVariable.artWorkProject;
    if (!artWorkProject.messageList.length > 0) {
        $("#dt").hide();
    }
    $("#mainPicture").html(generateDOTTemplateResult(Template.artWorkBaseInfoPicture, artWorkInfo));
    $("#mainInfo").html(generateDOTTemplateResult(Template.artWorkBaseInfoMain, artWorkInfo));
    $("#rz").html(generateDOTTemplateResult(Template.artWorkScheduleInvest, artWorkProject));
    $("#cz").html(generateDOTTemplateResult(Template.artWorkScheduleCreate, artWorkProject));
    $("#pm").html(generateDOTTemplateResult(Template.artWorkScheduleAuction, artWorkProject));
    $("#dt").html(generateDOTTemplateResult(Template.artWorkScheduleMessage, artWorkProject));
    setTimeout("tabsHeight()", 1000);
    for (var i = 0; i < artWorkProject.messageList.length; i++) {
        var message = artWorkProject.messageList[i];
        PageVariable.messageMap[message.id] = message;
        if (message.artworkCommentList != null) {
            for (var k = 0; k < message.artworkCommentList.length; k++) {
                var commentTemp = message.artworkCommentList[k];
                PageVariable.commentMap[commentTemp.id] = commentTemp;
            }
        }
    }
}
//获得项目详情的controller
function getArtWorkDetail() {
    var artWorkView = PageVariable.artWorkView;
    $("#artWorkView").html(generateDOTTemplateResult(Template.artWorkDetail, artWorkView));
    tabsHeight();
}
//获得用户评价的controller
function getArtWorkComment() {
    var artWorkComment = PageVariable.artWorkComment;
    if (pageEntity.pageIndex == 1) {
        $("#userComment").html(generateDOTTemplateResult(Template.artWorkComment, artWorkComment));
    } else {
        $("#userComment").append(generateDOTTemplateResult(Template.artWorkComment, artWorkComment));
    }
    pageEntity.pageIndex = pageEntity.pageIndex + 1;
    tabsHeight();
    for (var i = 0; i < artWorkComment.commentList.length; i++) {
        var comment = artWorkComment.commentList[i];
        PageVariable.messageMap[comment.id] = comment;
    }
}
//获得项目投资记录的controller
function getArtWorkInvestRecord() {
    var artWorkInvestRecord = PageVariable.artWorkInvestRecord;
    $("#topThree").html(generateDOTTemplateResult(Template.artWorkInvestRecordTop, artWorkInvestRecord));
    if (pageEntity.pageIndex == 1) {
        $("#investList").html(generateDOTTemplateResult(Template.artWorkInvestRecordList, artWorkInvestRecord));
    } else {
        $("#investList").append(generateDOTTemplateResult(Template.artWorkInvestRecordList, artWorkInvestRecord));
    }
    pageEntity.pageIndex = pageEntity.pageIndex + 1;
    tabsHeight();
}


//获得项目的基本信息
function getArtWorkBaseInfoData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            var msgList = obj["artworkMessageList"];
            var artWork = obj["artwork"];
            var masterLevel = "";
            var auctionStartDatetime = new Date();
            auctionStartDatetime.setTime(artWork.investEndDatetime);
            var auctionStartDatetimeStr = auctionStartDatetime.format("MM月dd日");
            switch (artWork.author.master.level) {
                case "1":
                    masterLevel = "国家级";
                    break;
                case "2":
                    masterLevel = "省级";
                    break;
                case "3":
                    masterLevel = "市级";
                    break;
                case "4":
                    masterLevel = "县级";
                    break;
            }
            PageVariable.artWorkInfo = new ArtWorkInfo(artWork.picture_url, artWork.author.name, artWork.brief, auctionStartDatetimeStr, artWork.step, artWork.title, artWork.author.master.title, artWork.author.pictureUrl, artWork.author.id);
            console.log(JSON.stringify(PageVariable.artWorkInfo));
            PageVariable.artWorkProject = new ArtWorkProject(artWork.investEndDatetime, artWork.step, artWork.investNum, artWork.investStartDatetime, msgList, artWork.auctionStartDatetime);

        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.initPage, dealRequestParam(getParamObject()), success, function () {
    }, "post");
}
//获得项目详情信息数据
function getArtWorkDetailData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"]
            PageVariable.artWorkView = new ArtWorkView(obj.artworkAttachmentList, obj.artWork.description, obj.artworkdirection.make_instru, obj.artworkdirection.financing_aq);
        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.artWorkViewTab, dealRequestParam(getParamObject()), success, function () {
    }, "post");
}
//获得项目评价信息数据
function getArtWorkCommentData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkComment = new ArtWorkComment(obj.artworkCommentList);
        }, data, callback)
    }
    var param = getParamObject();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;

    ajaxRequest(hostName + RequestUrl.commentTab, dealRequestParam(param), success, function () {
    }, "post");
}
//获得项目融资记录的数据
function getArtWorkInvestRecordData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkInvestRecord = new ArtWorkInvestRecord(obj.artworkInvestList, obj.artworkInvestTopList);
        }, data, callback)
    }
    var param = getParamObject();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;
    ajaxRequest(hostName + RequestUrl.investTab, dealRequestParam(param), success, function () {
    }, "post");
}


function artWorkProjectPanelAction() {
    loadDataAction = function () {
    }
}                   //页面滑到项目进度时候的action
function artWorkCommentPanelAction() {
    //初始化页面分页信息
    getArtWorkCommentData(getArtWorkComment); //页面从新加载评论的数据
    loadDataAction = function () {
        getArtWorkCommentData(getArtWorkComment);
    }
}                   //页面滑到用户评价时候的action
function artWorkInvestRecordPanelAction() {
    //初始化页面分页信息
    getArtWorkInvestRecordData(getArtWorkInvestRecord); //页面从新加载评论的数据
    loadDataAction = function () {
        getArtWorkInvestRecordData(getArtWorkInvestRecord);
    }

}              //页面滑到投资记录时候的action
function artWorkViewPanelAction() {
    loadDataAction = function () {
        console.log("bottom");
    }
}                      //页面滑到项目详情时候的action