/**
 * Created by Administrator on 2016/5/31 0031.
 */
function initPage(artWorkId, currentUserId) {
    var paramStr = window.demo1.fetchParamObject1();
        var paramObject = JSON.parse(paramStr);
        var param = new Object();
        param.artWorkId = paramObject.artWorkId;
        param.currentUserId = paramObject.currentUserId;
    /*var param = new Object();
    param.artWorkId = "qydeyugqqiugd2";
     param.currentUserId = "imhfp1yr4636pj49";*/
     PageVariable.param = param;
     PageVariable.artWorkId = param.artWorkId;
     refreshPageEntity();
    getArtWorkBaseInfoData(getArtWorkBaseInfo);
    getArtWorkDetailData(getArtWorkDetail);

}
function getAuctionTimeResult() {
    if (PageVariable.artWorkInfo.step == "30") {
        countDown(PageVariable.artWorkInfo.auctionStartDatetime);
    } else if (PageVariable.artWorkInfo.step == "31") {
        countDown(PageVariable.artWorkInfo.auctionEndDatetime);
    }
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
    $("#auctionMessage").html(generateDOTTemplateResult(Template.artWorkBaseInfoAuctionMessage, artWorkInfo));
    $("#winner").html(generateDOTTemplateResult(Template.artWorkAuctionWinner, artWorkInfo));
    $("#rz").html(generateDOTTemplateResult(Template.artWorkScheduleInvest, artWorkProject));
    $("#cz").html(generateDOTTemplateResult(Template.artWorkScheduleCreate, artWorkProject));
    $("#pm").html(generateDOTTemplateResult(Template.artWorkScheduleAuction, artWorkProject));
    $("#pm2").html(generateDOTTemplateResult(Template.artWorkScheduleAuctionResult));
    $("#dt").html(generateDOTTemplateResult(Template.artWorkScheduleMessage, artWorkProject));
    getCurrentDefaultConsumerAddressData(getBottomButton);
    getAuctionTimeResult();
    getArtWorkAuctionData(getArtWorkAuctionBidding);
    tabsHeight();
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

function getArtWorkAuctionBidding() {
    var artWorkAuction = PageVariable.artWorkAuction;
    $("#auctionNum").html(PageVariable.biddingUsersNum + "人参与拍卖");
    if (pageEntity.pageIndex == 1) {
        $("#auctionList").html(generateDOTTemplateResult(Template.artWorkAuctionBidding, artWorkAuction));
    } else {
        $("#auctionList").append(generateDOTTemplateResult(Template.artWorkAuctionBidding, artWorkAuction));
    }
    $("#auctionMessage").html(generateDOTTemplateResult(Template.artWorkBaseInfoAuctionMessage, PageVariable.artWorkInfo));
    pageEntity.pageIndex = pageEntity.pageIndex + 1;
    getArtWorkAuctionBiddingTopThree()
    tabsHeight();
}

function getArtWorkAuctionBiddingTopThree() {
    var artWorkAuction = PageVariable.artWorkAuction;
    $("#currentAuctionTopThreeRecord").html(generateDOTTemplateResult(Template.artWorkAuctionBiddingTop, artWorkAuction))
}

function getBottomButton() {
    var artWorkBaseInfo = PageVariable.artWorkInfo;
    $("#bottomButton").append(generateDOTTemplateResult(Template.preBottomButton, artWorkBaseInfo));
    $("#bottomButton").append(generateDOTTemplateResult(Template.beingBottomButton, artWorkBaseInfo));
    $("#bottomButton").append(generateDOTTemplateResult(Template.afterBottomButton, artWorkBaseInfo));
    ChooseCountComponent();
}


//出价
function bid() {
    var newPrice = $("input[name=auctionPrice]").val();
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            $("input[name=auctionPrice]").val(getCurrentAuctionPrice(dataTemp.artWorkBidding.price));
            PageVariable.artWorkInfo.currentAuctionPrice = dataTemp.artWorkBidding.price;
            $('#pm-bid-tips').fadeIn('fast').delay(1000).fadeOut('fast');
        }, data, function () {
            refreshPageEntity();
            getArtWorkAuctionData(getArtWorkAuctionBidding);
        }, function (dataTemp) {
            switch (dataTemp["resultCode"]) {
                case "10012" :
                    getAlert("有人出了更高的价格！", function () {
                        refreshPageEntity();
                        getArtWorkAuctionData(getArtWorkAuctionBidding);
                        $('.pm_dialog').remove();
                    });
                    break;
                default:
                    getAlert(dataTemp["resultMsg"], function () {
                        refreshPageEntity();
                        getArtWorkAuctionData(getArtWorkAuctionBidding);
                        $('.pm_dialog').remove();
                    })
            }
        })
    }
    var requestParam = getParamObject();
    requestParam.price = Number(newPrice);
    ajaxRequest(hostName + RequestUrl.bid, dealRequestParam(requestParam), success, function () {
    }, "post");
}


function artWorkProjectPanelAction() {
    loadDataAction = function () {
        console.log("bottom");
    }
}                   //页面滑到项目进度时候的action
function artWorkCommentPanelAction() {
    //初始化页面分页信息
    getArtWorkCommentData(getArtWorkComment); //页面从新加载评论的数据
    loadDataAction = function () {
        getArtWorkCommentData(getArtWorkComment);
    }
}                   //页面滑到用户评价时候的action
function artWorkViewPanelAction() {
    loadDataAction = function () {
        console.log("bottom");
    }
}                      //页面滑到项目详情时候的action
function artWorkAuctionPanelAction() {
    //初始化页面分页信息
    getArtWorkAuctionData(getArtWorkAuctionBidding); //页面从新加载拍卖的数据
    loadDataAction = function () {
        getArtWorkAuctionData(getArtWorkAuctionBidding);
    }
}                   //页面滑到拍卖纪录时候的action

function getAlert(content, callback) {
    $(".pm_dialog").remove();
    var out = ' <div class="pm_dialog"> <div class="content" style="height: auto;"> <div class="close"></div> <div class="info" style="text-align: center;padding-bottom: 25px;height: auto"> <p>' + content + '</p> </div> ';
    out += '<a id="confirm" class="btn" title="去充值" style="margin-top: 10px;margin-bottom: 10px;">确认</a> </div> </div>';
    var ParentDiv2 = $("#dialog");
    var dailogClick = function () {
        $('.pm_dialog').remove();
    }
    if (typeof callback == "function") {
        ParentDiv2.on("click", "#confirm", callback);
    } else {
        ParentDiv2.on("click", "#confirm", dailogClick);
    }
    ParentDiv2.html(out);
    // return out;
}