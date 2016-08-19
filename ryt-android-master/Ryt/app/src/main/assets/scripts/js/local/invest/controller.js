/**
 * Created by Administrator on 2016/5/31 0031.
 */
function initPage() {
    var paramStr = window.demo1.fetchParamObject1();
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
}  //初始化页面
function loginCallback() {
    refreshPageEntity();
    getArtWorkBaseInfoData(getArtWorkBaseInfo);
    getArtWorkDetailData(getArtWorkDetail);
    getArtWorkCommentData(getArtWorkComment);
}   //登录回调函数
function getPraiseList(message) {
    return generateDOTTemplateResult(Template.messagePraise, message);
}
function getMessageButtons(message) {
    return generateDOTTemplateResult(Template.messageButtons, message);
}
function getAuctionTimeResult() {

    var countDownCallback = function () {
        var style = "";
        var currentTime = new Date();
        var time = 0;
        if (PageVariable.artWorkInfo.step == "30") {
            time = PageVariable.artWorkInfo.auctionStartDatetime - currentTime.getTime();
        } else if (PageVariable.artWorkInfo.step == "31") {
            time = PageVariable.artWorkInfo.auctionEndDatetime - currentTime.getTime();
        }
        if (time > 86400000) {
            style = "display:none";
        } else if (time < 86400000) {
            var pesent = (86400000 - time) / 86400000 * 100;
            style = "width:" + pesent + "%";
        }
        $("#countDown").attr("style", style);
    }
    var currentDatetime = new Date().getTime();
    if (PageVariable.artWorkInfo.step == "30") {
        if (currentDatetime < PageVariable.artWorkInfo.auctionStartDatetime) {
            countDown(PageVariable.artWorkInfo.auctionStartDatetime, countDownCallback);
        }
    } else if (PageVariable.artWorkInfo.step == "31") {
        if (currentDatetime < PageVariable.artWorkInfo.auctionEndDatetime) {
            countDown(PageVariable.artWorkInfo.auctionEndDatetime, countDownCallback);
        }
    }
}   //获得拍卖倒计时的结果
function getArtWorkBaseInfo() {
    var artWorkInfo = PageVariable.artWorkInfo;
    var artWorkProject = PageVariable.artWorkProject;
    if (!artWorkProject.messageList.length > 0) {
        $("#dt").hide();
    }
    $("#mainInfo").html(generateDOTTemplateResult(Template.artWorkBaseInfoMain, artWorkInfo));
    $("#mainPicture2").html(generateDOTTemplateResult(Template.artWorkBaseInfoPicture, artWorkInfo));
    $("#mainInfo2").html(generateDOTTemplateResult(Template.artWorkBaseInfoMain2, artWorkInfo));
    $("#auctionMessage").html(generateDOTTemplateResult(Template.artWorkBaseInfoAuctionMessage, artWorkInfo));
    $("#winner").html(generateDOTTemplateResult(Template.artWorkAuctionWinner, artWorkInfo));
    $("#rz").html(generateDOTTemplateResult(Template.artWorkScheduleInvest, artWorkProject));
    $("#cz").html(generateDOTTemplateResult(Template.artWorkScheduleCreate, artWorkProject));
    $("#pm").html(generateDOTTemplateResult(Template.artWorkScheduleAuction, artWorkProject));
    $("#pm2").html(generateDOTTemplateResult(Template.artWorkScheduleAuctionResult));
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

    getCurrentDefaultConsumerAddressData(getBottomButton);
    getAuctionTimeResult();
    getArtWorkAuctionData(getArtWorkAuctionBidding);
    // getArtFollowMasterListData(checkArtFollowMaster);
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
        var vm = $(this),
            reply = vm.find('.reply'), //按钮
            speed = 500,
            layer = vm.find('.layer');//内容
        //初始化
        reply.click(function () {
            var a = $(this)
            if (a.hasClass('current')) {
                a.siblings('.horizontal').find(".layer").css({Transform: "translate(110%,0)"});
                a.removeClass('current')
            } else {
                $('.reply').removeClass('current')
                $('.layer').css({Transform: "translate(110%,0)"})
                a.addClass('current')
                a.siblings('.horizontal').find(".layer").css({Transform: "translate(0,0)"})
            }

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


}       //项目基本信息的controller
function getArtWorkDetail() {
    var artWorkView = PageVariable.artWorkView;
    $("#artWorkView").html(generateDOTTemplateResult(Template.artWorkDetail, artWorkView));
}      //项目详情的controller
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
}       //项目评论的controller
function getArtWorkAuctionBidding() {
    var artWorkAuction = PageVariable.artWorkAuction;
    if (typeof artWorkAuction.biddingTopThree != "undefined" && artWorkAuction.biddingTopThree.length > 0) {
        var currentPrice = artWorkAuction.biddingTopThree[0].price;
        PageVariable.currentAuctionPrice = currentPrice;
        $("#currentPrice").html("￥" + PageVariable.currentAuctionPrice);
    }
    $("#auctionNum").html(PageVariable.biddingUsersNum + "人参与拍卖");
    $("#auctionList").html(generateDOTTemplateResult(Template.artWorkAuctionBidding, artWorkAuction));
    pageEntity.pageIndex = pageEntity.pageIndex + 1;
    tabsHeight();
    if (PageVariable.step == "31" || new Date().getTime() < PageVariable.artWorkInfo.auctionEndDatetime) {
        setTimeout("getArtWorkAuctionData(getArtWorkAuctionBidding)", 1000);
    }
}   //项目拍卖纪录的controller
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
}       //判断当前大师是否被当前用户关注
function getBottomButton() {
    var artWorkBaseInfo = PageVariable.artWorkInfo;
    $("#bottomButton").append(generateDOTTemplateResult(Template.preBottomButton, artWorkBaseInfo));
    $("#bottomButton").append(generateDOTTemplateResult(Template.beingBottomButton, artWorkBaseInfo));
    $("#bottomButton").append(generateDOTTemplateResult(Template.afterBottomButton, artWorkBaseInfo));
    ChooseCountComponent();
}       //页面底部按钮的controller
function bid() {
    var newPrice = $("input[name=auctionPrice]").val();
    window.bidSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            $("input[name=auctionPrice]").val(getCurrentAuctionPrice(dataTemp.artWorkBidding.price));
            PageVariable.artWorkInfo.currentAuctionPrice = dataTemp.artWorkBidding.price;
        }, data, function () {
            refreshPageEntity();
        }, function (dataTemp) {
            switch (dataTemp["resultCode"]) {
                case "10012" :
                    getAlert("有人出了更高的价格！", function () {
                        refreshPageEntity();
                    });
                    break;
                default:
                    getAlert(dataTemp["resultMsg"], function () {
                        refreshPageEntity();
                    })
            }
        })
    }
    var requestParam = getParamObject();
    requestParam.price = Number(newPrice);
    ajaxRequest(hostName + RequestUrl.bid, dealRequestParam(requestParam), window.bidSuccess, function () {
    }, "bidSuccess");
}                   //出价
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
}           //添加新的评论
function getAlert(content, callback) {
    $("#dialogContent").html(content);
    $('.ui.modal').modal("show");
}   //触发提示框
