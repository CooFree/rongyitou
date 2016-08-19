/**
 * Created by Administrator on 2016/5/31 0031.
 */
//页面上需要的数据
var ArtWorkInfo = function (picture_url, masterName, brief, auctionStartDatetime, step, name, title, headUrl, masterId, startingPrice, winner, auctionEndDatetime, currentAuctionPrice, creationEndDatetime, author) {
    this.picture_url = picture_url;   //主图
    this.masterName = masterName;       //大师名称
    this.brief = brief;                 //项目简介
    this.auctionStartDatetime = auctionStartDatetime; //拍卖开始时间
    this.step = step;     //项目阶段
    this.name = name;       //项目名称
    this.title = title;     //大师头衔
    this.headUrl = headUrl; //头像图片链接
    this.masterId = masterId; //大师的id
    this.startingPrice = startingPrice;  //起拍价
    this.winner = winner;    //竞拍得主
    this.auctionEndDatetime = auctionEndDatetime; //拍卖结束时间
    this.currentAuctionPrice = currentAuctionPrice;
    this.creationEndDatetime = creationEndDatetime;
    this.author = author;
}
//项目主要信息（项目进度专用）
var ArtWorkProject = function (investEndDatetime, step, artworkInvestsSize, investStartDatetime, messageList, auctionStartDatetime, creationEndDatetime, investGoalMoney, investsMoney, createDatetime) {
    this.investEndDatetime = investEndDatetime;   //融资结束时间
    this.step = step;                             //项目审核状态
    this.artworkInvestsSize = artworkInvestsSize;  //项目投资人数
    this.investStartDatetime = investStartDatetime; //投资开始时间
    this.messageList = messageList;           //状态列表
    this.auctionStartDatetime = auctionStartDatetime; //拍卖开始时间
    this.creationEndDatetime = creationEndDatetime;
    this.investGoalMoney = investGoalMoney; //项目目标金额
    this.investsMoney = investsMoney; //项目投资金额
    this.createDatetime = createDatetime; //动态发布时间//创作结束时间
}
//项目详情（项目详情tab页专用）
var ArtWorkView = function (artworkAttachmentList, description, make_instru, financing_aq) {
    this.artworkAttachmentList = artworkAttachmentList;//1.图片列表
    this.description = description;//2.项目描述
    this.make_instru = make_instru;//3.项目制作说明
    this.financing_aq = financing_aq//4.项目融资答疑
}
//用户评价
var ArtWorkComment = function (commentList) {
    this.commentList = commentList;//1.用户评价列表
}
//投资记录（投资记录tab页专用）
var ArtWorkAuction = function (artWorkBiddingList, biddingTopThree) {
    this.artWorkBiddingList = artWorkBiddingList;
    this.biddingTopThree = biddingTopThree;
}
//通用的全局变量
var PageVariable = {
    username: "13693097151",
    password: "123123",
    biddingUsersNum: "",      //竞拍人数
    isSubmitDepositPrice: "1",//是否交了保证金 0 已交 1 未交
    viewNum: "",            //浏览次数
    startingPrice: "",      //起拍价格
    auctionNum: "",         //竞拍次数
    artWorkId: "qydeyugqqiugd2",          //当前项目的id
    artWorkInfo: "",        //当前项目的基本信息对象
    artWorkProject: "",     //当先项目的项目进度以及动态对象
    artWorkView: "",        //当前项目详情的对象
    artWorkComment: "",     //当前项目的用户评价对象
    artWorkInvestRecord: "", //当前项目投资记录对象
    artWorkAuctionStatus: "", //当前项目的拍卖状态
    artWorkAuction: "",
    consumerAddress: null, //默认地址
    commentMap: {},
    messageMap: {},
    fatherCommentId: "",
    artFollowMasterList: [],
    isFollow: false
};
//当前用户关注大师的列表
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
//获得项目的基本信息
function getArtWorkBaseInfoData(callback) {
    window.artWorkBaseInfoSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp;
            var msgList = obj["artWorkMessage"];
            var artWork = obj["artwork"];
            var auctionStartDatetime = new Date();
            auctionStartDatetime.setTime(artWork.auctionStartDatetime);
            PageVariable.artWorkInfo = new ArtWorkInfo(artWork.picture_url, artWork.author.name, artWork.brief, artWork.auctionStartDatetime, artWork.step, artWork.title, artWork.author.master.title, artWork.author.pictureUrl, artWork.author.id, artWork.startingPrice, artWork.winner, artWork.auctionEndDatetime, artWork.newBidingPrice, artWork.creationEndDatetime, artWork.author);
            PageVariable.artWorkProject = new ArtWorkProject(artWork.investEndDatetime, artWork.step, artWork.investNum, artWork.investStartDatetime, msgList, artWork.auctionStartDatetime, artWork.creationEndDatetime, artWork.investGoalMoney, artWork.investsMoney, artWork.createDatetime);
            PageVariable.viewNum = artWork.viewNum;
            PageVariable.auctionNum = artWork.auctionNum;
            PageVariable.startingPrice = artWork.startingPrice;
            PageVariable.isSubmitDepositPrice = obj["isSubmitDepositPrice"];
            // PageVariable.isSubmitDepositPrice = "1";
        }, data, callback);
    };
    ajaxRequest(hostName + RequestUrl.initPage, dealRequestParam(getParamObject()), window.artWorkBaseInfoSuccess, function () {
    }, "artWorkBaseInfoSuccess");
}
//获得项目详情信息数据
function getArtWorkDetailData(callback) {
    window.artWorkDetailSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkView = new ArtWorkView(obj.artworkAttachmentList, obj.artWork.description, obj.artworkdirection.make_instru, obj.artworkdirection.financing_aq);
        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.artWorkViewTab, dealRequestParam(getParamObject()), window.artWorkDetailSuccess, function () {
    }, "artWorkDetailSuccess");
}
//获得项目评价信息数据
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
//拍卖纪录
function getArtWorkAuctionData(callback) {
    window.artWorkAuctionSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp;
            PageVariable.artWorkAuction = new ArtWorkAuction(obj.artworkBiddingList, obj.biddingTopThree);
            PageVariable.auctionNum = obj.auctionNum;
            PageVariable.biddingUsersNum = obj.biddingUsersNum;
        }, data, callback)
    };
    var param = getParamObject();
    refreshPageEntity();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;
    ajaxRequest(hostName + RequestUrl.auctionTab, dealRequestParam(param), window.artWorkAuctionSuccess, function () {
    }, "artWorkAuctionSuccess");
}
//当前用户的默认收货地址
function getCurrentDefaultConsumerAddressData(callback) {
    window.currentDefaultConsumerAddressSuccess = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp;
            PageVariable.consumerAddress = obj.defaultAddress;
        }, data, callback)
    };
    ajaxRequest(hostName + RequestUrl.consumerAddress, dealRequestParam(getParamObject()), window.currentDefaultConsumerAddressSuccess, function () {
    }, "currentDefaultConsumerAddressSuccess");
}

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
    ajaxRequest(hostName + RequestUrl.newArtWorkPraise, param, window.newArtWorkPraiseSuccess, function () {
    }, "newArtWorkPraiseSuccess");
}