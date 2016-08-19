/**
 * Created by Administrator on 2016/5/31 0031.
 */
//页面的初始化和渲染页面(统一调配函数)
function initPage() {
    var paramStr = window.demo.fetchParamObject();
                        var paramObject = JSON.parse(paramStr);
                        var param = new Object();
                        //param.artWorkId = paramObject.artWorkId;
                        param.artWorkId = paramObject.artWorkId;
                        param.username = paramObject.username;
                        //param.currentUserId = paramObject.currentUserId;
                        param.currentUserId = paramObject.currentUserId;
                        param.password = paramObject.password;
                        console.log("==============================");
                        console.log("param.artWorkId"+param.artWorkId+"param.currentUserId"+param.currentUserId);
                        console.log("==============================");
                        PageVariable.param = param;
                        PageVariable.artWorkId = param.artWorkId;
                        PageVariable.username = param.username;
                        PageVariable.password = param.password;
                        console.log("==============================");
                        console.log("PageVariable.password"+PageVariable.password+"PageVariable.username"+PageVariable.username);
                        console.log("==============================");
                        loginCallback();
}


function loginCallback() {
    refreshPageEntity();
    getArtWorkBaseInfoData(getArtWorkBaseInfo);
    getArtWorkDetailData(getArtWorkDetail);
    getArtWorkCommentData(getArtWorkComment);
    getArtWorkInvestRecordData(getArtWorkInvestRecord);
}

//获得项目基本信息的controller
function getArtWorkBaseInfo() {
    var artWorkInfo = PageVariable.artWorkInfo || PageVariable.artWorkProject;
    var artWorkProject = PageVariable.artWorkProject;
    if (!artWorkProject.messageList.length > 0) {
        $("#dt").hide();
    }
    $("#mainInfo").html(generateDOTTemplateResult(Template.artWorkBaseInfoMain, artWorkInfo));
    $("#rz").html(generateDOTTemplateResult(Template.artWorkScheduleInvest, artWorkProject));
    $("#cz").html(generateDOTTemplateResult(Template.artWorkScheduleCreate, artWorkProject));
    $("#pm").html(generateDOTTemplateResult(Template.artWorkScheduleAuction, artWorkProject));
    $("#dt").html(generateDOTTemplateResult(Template.artWorkScheduleMessage, artWorkProject));

    //项目模版
    // $("#mainPicture,#projectPicture").append(generateDOTTemplateResult(Template.artWorkBaseInfoPicture, artWorkInfo));
    $("#mainPicture").html(generateDOTTemplateResult(Template.artWorkProgressPicture, artWorkInfo));
    $("[data-name=artWorkProjectPicture]").html(generateDOTTemplateResult(Template.artWorkProjectPicture, artWorkInfo));
    $('[data-name=name]').html(generateDOTTemplateResult(Template.artWorkBaseInfoTitle, artWorkInfo)); //项目名称
    $('[data-name=masterName]').html(generateDOTTemplateResult(Template.artWorkMasterName, artWorkInfo)); //大师名称
    $('[data-name=dayBefore]').html(generateDOTTemplateResult(Template.artWorkDaybefore, artWorkProject)); //多少天前
    $('[data-name=brief]').html(generateDOTTemplateResult(Template.artWorkBrief, artWorkInfo)); //项目简介
    $('[data-name=price]').html(generateDOTTemplateResult(Template.investGoalMoney, artWorkInfo)); //项目简介
    $('[data-name=headUrl]').html(generateDOTTemplateResult(Template.artWorkHeadUrl, artWorkInfo)); //大师头像
    $('[data-name=title]').html(generateDOTTemplateResult(Template.artWorkMasterTitle, artWorkInfo)); //大师头衔
    $('[data-name=masterWorkNum]').html(generateDOTTemplateResult(Template.artWorkMasterWorkNum, artWorkInfo)); //大师作品总数
    $('[data-name=fansNum]').html(generateDOTTemplateResult(Template.artWorkMasterFansNum, artWorkInfo)); //大师粉丝总数
    $('[data-name=dataInfo]').html(generateDOTTemplateResult(Template.artWorkDataInfo, artWorkProject)); //项目投资截止时间和投资信息

    // getArtFollowMasterListData(checkArtFollowMaster);
    // setTimeout("tabsHeight()", 1000);
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
    //弹出相册
        var aImg = $('.creation .ui.comments .text .swiper-slide'),
            oSwiper = $('.swiper2');
        aImg.click(function () {
            var index = $(this).index();
            var d = document.body.clientWidth,  //获取屏幕宽度
                oSwiperWrapper = oSwiper.find('.swiper-wrapper'),  //相册弹出层
                oSwiperPagination = oSwiper.find('.swiper-pagination'),  //圆点
                html = $(this).parents('.text').html();  //追加的元素

            //显示相册弹出层
            oSwiper.fadeIn();
            //控制相册弹出层的位置大小
            oSwiperWrapper.append(html).css({'width': d + 'px', 'height': d + 'px', 'margin-top': -d / 2 + 'px'});

            //点击黑色背景
            oSwiper.find('.overbg').click(function () {
                oSwiper.fadeOut();  //隐藏相册弹出层
                oSwiperWrapper.html('').attr('style', '');  //去掉swiper初始化的属性
                oSwiperPagination.html('');  //去掉生成的元素
                swiper2.detachEvents(); //移除所有slide监听事件
            });

                   //点击图片
                   oSwiper.find('.swiper-slide').click(function() {
                                                       oSwiper.fadeOut();  //隐藏相册弹出层
                                                       oSwiperWrapper.html('').attr('style', '');  //去掉swiper初始化的属性
                                                       oSwiperPagination.html('');  //去掉生成的元素
                                                       swiper2.detachEvents(); //移除所有slide监听事件
                                                       });

            var swiper2 = new Swiper('.swiper2', {
                speed: 500,
                pagination: '.swiper-pagination',
                onSlideChangeStart: function () {
                    oSwiperPagination.find('span').eq(swiper2.activeIndex).addClass('active').siblings('span').removeClass('active');
                },
            });
            swiper2.slideTo(index, 0, false);
            //如果是一张图片则隐藏下面的圆点
            if (oSwiper.find('.swiper-slide').length > 2) {
                oSwiper.find('.swiper-pagination').show();
            } else {
                oSwiper.find('.swiper-pagination').hide();
            }


        });

    //点赞和评论
    var actions = $('.creation .ui.comments .actions');
    actions.each(function () {
        var reply = $(this).find('.reply'), //按钮
            layer = $(this).find('.layer');//内容

        //初始化
        reply.click(function () {

            var a = $(this)
            if (a.hasClass('current')) {
                //hide
                setTimeout(function () {
                    a.siblings('.horizontal').find(".layer").css({Transform: "translate(110%,0)"});
                }, 500);
                a.removeClass('current');
            } else {
                //show
                $('.reply').removeClass('current');
                $('.layer').css({Transform: "translate(110%,0)"});
                a.addClass('current');
                setTimeout(function () {
                    a.siblings('.horizontal').find(".layer").css({Transform: "translate(0,0)"});
                }, 500);
            }


            $('.layer').on('click', function () {
                setTimeout(function () {
                    a.siblings('.horizontal').find(".layer").css({Transform: "translate(110%,0)"});
                }, 500);
                a.removeClass('current');
                return false;
            });

            return false;

        });


    });


    //圆环进度
    $('#indicatorContainer').radialIndicator({
        radius: 20,  //定义圆形指示器的内部的圆的半径
        barWidth: 2, //定义圆形指示器的刻度条的宽度
        barBgColor: '#e5e5e5',  //定义圆形指示器的刻度条的背景颜色
        percentage: true,  //设置为true显示圆形指示器的百分比数值
        initValue: 100, //圆形指示器初始化的值
    });


    //
    // if($('.creation .ui.comments .ui.message .feed .content').length>0){
    //     // console.log(11111)
    //     $(this).find('.event').show()
    // }


}
//获得项目详情的controller
function getArtWorkDetail() {
    var artWorkView = PageVariable.artWorkView;
    $("#artWorkView").html(generateDOTTemplateResult(Template.artWorkDetail, artWorkView));
    // tabsHeight();
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


function newComment() {
    window.createCommentSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function () {
            $("#commentContent").val("").attr("placeholder", "我也来说两句...");
            PageVariable.fatherCommentId = "";
            getArtWorkCommentData(getArtWorkComment);
        }, data, function () {
        }, function () {
        })
    };
    var requestParam = getParamObject();
    requestParam["fatherCommentId"] = PageVariable.fatherCommentId;
    requestParam["content"] = $("#commentContent").val();
    requestParam["artworkId"] = requestParam.artWorkId;
    if (requestParam.content != "null" && requestParam.content != "") {
        ajaxRequest(hostName + RequestUrl.createComment, dealRequestParam(requestParam), window.createCommentSuccess, function () {
        }, "createCommentSuccess");
    }
}

function checkArtFollowMaster() {
    //判断当前用户是否已经关注了当前的大师
    for (var i = 0; i < PageVariable.artFollowMasterList.length; i++) {
        var artFollowMaster = PageVariable.artFollowMasterList[i];
        if (artFollowMaster.follower.id == PageVariable.artWorkInfo.masterId) {
            PageVariable.isFollow = true;
            break;
        }
    }
    $("#master").html(generateDOTTemplateResult(Template.artWorkMaster, PageVariable.artWorkInfo.author))
}

function getArtFollowMasterListData(callback) {
    window.artFollowMasterListSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            PageVariable.artFollowMasterList = dataTemp["artUserFollowedList"];
        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.artFollowMasterList, getParamObject(), window.artFollowMasterListSuccess, false, "artFollowMasterListSuccess");
}
//添加对当前大师的关注
function newArtFollowMasterData(callback) {
    window.newArtFollowMasterSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            PageVariable.artFollowMasterList = dataTemp["artUserFollowedList"];
        }, data, callback);
    }
    var param = getParamObject();
    param["masterUserId"] = PageVariable.artWorkInfo.masterId;
    ajaxRequest(hostName + RequestUrl.newArtFollowMaster, param, window.newArtFollowMasterSuccess, function () {
    }, "newArtFollowMasterSuccess");
}

//获得项目投资记录的controller
function getArtWorkInvestRecord() {
    var artWorkInvestRecord = PageVariable.artWorkInvestRecord;

    /*if (pageEntity.pageIndex == 1) {
     $("#investList").html(generateDOTTemplateResult(Template.artWorkInvestRecordList, artWorkInvestRecord));
     } else {
     $("#investList").append(generateDOTTemplateResult(Template.artWorkInvestRecordList, artWorkInvestRecord));
     }*/
    /*pageEntity.pageIndex = pageEntity.pageIndex + 1;*/

    $("#investList").html(generateDOTTemplateResult(Template.artWorkInvestRecordList, artWorkInvestRecord));
    $('#btn-mores').click(function () {
        $(this).hide();
        $("#investList").append(generateDOTTemplateResult(Template.artWorkInvestRecordListAll, artWorkInvestRecord));
    })


}


function getPraiseList(message) {
    return generateDOTTemplateResult(Template.messagePraise, message);
}

function getMessageButtons(message) {
    return generateDOTTemplateResult(Template.messageButtons, message);
}


//获得项目的基本信息
function getArtWorkBaseInfoData(callback) {
    window.artWorkBaseInfoSuccess = function (data) {
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
            PageVariable.artWorkInfo = new ArtWorkInfo(artWork.picture_url, artWork.author.name, artWork.brief, auctionStartDatetimeStr, artWork.step, artWork.title, artWork.author.master.title, artWork.author.pictureUrl, artWork.author.id, artWork.author.masterWorkNum, artWork.author.fansNum, artWork.author);
            PageVariable.artWorkProject = new ArtWorkProject(artWork.investEndDatetime, artWork.step, artWork.investNum, artWork.investStartDatetime, msgList, artWork.auctionStartDatetime, artWork.investGoalMoney, artWork.investsMoney, artWork.createDatetime);

        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.initPage, dealRequestParam(getParamObject()), window.artWorkBaseInfoSuccess, function () {
    }, "artWorkBaseInfoSuccess");
}
//获得项目详情信息数据
function getArtWorkDetailData(callback) {
    window.artWorkDetailSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"]
            PageVariable.artWorkView = new ArtWorkView(obj.artworkAttachmentList, obj.artWork.description, obj.artworkdirection.make_instru, obj.artworkdirection.financing_aq);
        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.artWorkViewTab, dealRequestParam(getParamObject()), window.artWorkDetailSuccess, function () {
    }, "artWorkDetailSuccess");
}
//获得项目评价信息数据
function getArtWorkCommentData2(callback) {
    window.artWorkCommentSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkComment = new ArtWorkComment(obj.artworkCommentList);
        }, data, callback)
    }
    var param = getParamObject();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;

    ajaxRequest(hostName + RequestUrl.commentTab, dealRequestParam(param), window.artWorkCommentSuccess, function () {
    }, "artWorkCommentSuccess");
}

function getArtWorkCommentData(callback) {
    window.artWorkCommentSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkComment = new ArtWorkComment(obj.artworkCommentList);
        }, data, callback)
    };
    var param = getParamObject();
    refreshPageEntity();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;
    ajaxRequest(hostName + RequestUrl.commentTab, dealRequestParam(param), window.artWorkCommentSuccess, function () {
    }, "artWorkCommentSuccess");
}

//获得项目融资记录的数据
function getArtWorkInvestRecordData(callback) {
    window.artWorkInvestRecordSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkInvestRecord = new ArtWorkInvestRecord(obj.artworkInvestList, obj.artworkInvestTopList);
        }, data, callback)
    }
    var param = getParamObject();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;
    ajaxRequest(hostName + RequestUrl.investTab, dealRequestParam(param), window.artWorkInvestRecordSuccess, function () {
    }, "artWorkInvestRecordSuccess");
}

function artWorkProjectPanelAction() {
    loadDataAction = function () {
    }
}
//页面滑到项目进度时候的action
function artWorkCommentPanelAction() {
    //初始化页面分页信息
    getArtWorkCommentData(getArtWorkComment); //页面重新加载评论的数据
    loadDataAction = function () {
        getArtWorkCommentData(getArtWorkComment);
    }
}
//页面滑到用户评价时候的action
function artWorkInvestRecordPanelAction() {
    //初始化页面分页信息
    getArtWorkInvestRecordData(getArtWorkInvestRecord);
    //页面从新加载评论的数据
    loadDataAction = function () {
        getArtWorkInvestRecordData(getArtWorkInvestRecord);
    }
}
//页面滑到投资记录时候的action
function artWorkViewPanelAction() {
    loadDataAction = function () {
        console.log("bottom");
    }
}
//页面滑到项目详情时候的action
function newArtWorkPraise(messageId, action) {
    var thisElement = $(this);
    var thisParent = thisElement.parent();
    thisParent.css({Transform: "translate(0,0)"});
    window.newArtWorkPraiseSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            // getArtWorkBaseInfoData(getArtWorkBaseInfo);
            var message = dataTemp.artworkMessage;
            $("#" + message.id + "praise").html(getPraiseList(message));
            $("#" + message.id + "buttons").html(getMessageButtons(message));
            if (action == "0" && message.artWorkPraiseList.length == 0) {
                $("#" + message.id + "praiseIcon").hide();
            } else {
                $("#" + message.id + "praiseIcon").show();
            }
        }, data)
    }
    var param = getParamObject();
    param["action"] = action;
    param["messageId"] = messageId;
    param["artworkId"] = getParamObject().artWorkId;
    ajaxRequest(hostName + RequestUrl.newArtWorkPraise, param, window.newArtWorkPraiseSuccess, function (request, textStatus, exception) {
        alert("数据获取失败 status:" + request.readyState);
        alert(textStatus);
    }, "newArtWorkPraiseSuccess");
}

/*function submitTest(action,artWorkId,messageId) {
 var data={
 action:action,
 artWorkId:artWorkId,
 messageId:messageId,
 }
 $.ajax({
 url: "http://192.168.1.60:8080/app/artworkPraise.do",
 data: data,
 success: function () {

 },
 dataType: 'json'
 });
 console.log(data);
 }*/

function submitTest(callback) {
    window.artWorkBaseInfoSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {

        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.initPage, dealRequestParam(getParamObject()), window.artWorkBaseInfoSuccess, function () {
    }, "artWorkBaseInfoSuccess")
};