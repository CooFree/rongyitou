/**
 * Created by Administrator on 2016/5/31 0031.
 */

//ajax请求相关
function ajaxRequest(url, param, success, error, callback) {
    $.ajax({
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        type: "post",
        url: url,
        cache: false,
        dataType: "jsonp",
        jsonpCallback: callback,
        data: param,
        success: function (data) {
            console.log(JSON.stringify(data)+"klajflsjflsjflsjlgjslgjslgjlsjglsjfl");
            if (error != false) {
                if (data["resultCode"] == "10004" || data["resultCode"] == "000000") {
                console.log(url+"kljljlsjglksjdfljs");
                    redirectLogin(url, param, success, error, callback);
                }
            }
        },
        error: function () {
            // var callback = function () {
            //     ajaxRequest(url, param, success, error, callback);
            // };
            // login(callback);
        }
    })
    ;
}

function ajaxSuccessFunctionTemplage(dataDealFunction, data, callback, dealError, callbackParam) {
    switch (data["resultCode"]) {
        case "0" :
            dataDealFunction(data);
            if (typeof callback == "function") {
                callback(callbackParam);
            }
            break;
        default :
            if (typeof dealError == "function") {
                dealError(data);
            }
            break;
    }
}
//异步请求处理模板
function appInterfaceReturnDataCheck(data) {
    if (data["resultCode"] == 0) {
        return true;
    } else {
        console.log(data);
        console.log("数据获取失败");
    }
}   //app服务端接口返回数据处理模板
function login(callback) {
    window.loginSuccess = function (data) {
        console.log(data);
        callback();
    };
    ajaxRequest(hostName + "/app/j_spring_security_check", {
        username: PageVariable.username,
        password: PageVariable.password
    }, window.loginSuccess, function () {
        console.log("数据获取失败！！！");
    }, "loginSuccess");
}    //通用的登录接口

function loginFunction(artWorkId, currentUserId, username, password, callbackStr) {
    PageVariable.username = username;
    PageVariable.password = password;
    window.loginSuccess = function (data) {
        console.log(data);
        // initPage(artWorkId, currentUserId)
        eval(callbackStr);
    };
    ajaxRequest(hostName + "/app/j_spring_security_check", {
        username: PageVariable.username,
        password: PageVariable.password
    }, window.loginSuccess, function () {
        console.log("数据获取失败！！！");
    }, "loginSuccess");
}    //通用的登录接口

function generateDOTTemplateResult(templateId, data) {
    var viewFunction = doT.template($("#" + templateId).text())
    var text = viewFunction(data);
    return text;
}  //dot.js 通用模板工具
//请求参数相关
function createSignmsg(requestParam) {
    var paramNameList = [];
    for (var key in requestParam) {
        paramNameList.push(key);
    }
    paramNameList = paramNameList.sort();
    var msg = "";
    for (var i = 0; i < paramNameList.length; i++) {
        var key = paramNameList[i];
        msg += key + "=" + requestParam[key] + "&";
    }
    msg += "key=BL2QEuXUXNoGbNeHObD4EzlX+KuGc70U"
    return $.md5(msg);
}   //创建签名
function dealRequestParam(requestParam) {
    //筛选参数
    //设置签名
    //设置时间戳
    var paramResult = {};
    for (var key in requestParam) {
        if (requestParam[key] != null && requestParam[key] != "" && typeof requestParam[key] != "undefined") {
            paramResult[key] = requestParam[key];
        }
    }
    paramResult.timestamp = new Date().getTime();
    paramResult.signmsg = createSignmsg(paramResult);
    return paramResult;
}   //处理请求模板
function getParamObject() {
    var artWorkId = "ipeulrut2z8gf2s0";
    var currentUserId = "ih33g5t18ge151fg";
    var artWorkOrderId = "123";
    var param = PageVariable.param;
    if (typeof param["currentUserId"] != "undefined" && param["currentUserId"] != null && param["currentUserId"] != "") {
        currentUserId = param["currentUserId"];
    }
    if (typeof param["artWorkOrderId"] != "undefined" && param["artWorkOrderId"] != null && param["artWorkOrderId"] != "") {
        artWorkOrderId = param["artWorkOrderId"];
    }
    if (typeof param["artWorkId"] != "undefined" && param["artWorkId"] != null && param["artWorkId"] != "") {
        artWorkId = param["artWorkId"];
    }
    return {artWorkId: artWorkId, currentUserId: currentUserId, artWorkOrderId: artWorkOrderId}
}//获得请求参数对象
//时间格式化相关
Date.prototype.format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}   //时间格式化工具
function getDayNums(times) {
    return parseInt(times / 1000 / 3600 / 24);
} //获得天数字符串
function getDateStr(times) {
    var date = new Date();
    date.setTime(times);
    return date.format("MM月dd日");
} //获得月份字符串
function getTimePre(times) {
    var result = "";
    var currentDatetime = new Date().getTime();
    var preTime = currentDatetime - times;
    var ss = 60 * 1000
    var mm = 60 * 60 * 1000;
    var hh = 60 * 60 * 24 * 1000;
    var MM = 60 * 60 * 24 * 30 * 1000;
    if (preTime > mm && preTime < hh) {
        result = parseInt(preTime / mm) + "小时前";
    } else if (preTime < mm) {
        var resultTime = preTime / ss;
        if (resultTime < 1) {
            result = "刚才"
        } else {
            result = parseInt(resultTime) + "分钟前"

        }
    } else if (preTime > hh) {
        result = parseInt(preTime / hh) + "天前"
    } else if (preTime > MM) {
        result = getDateFormatStr(times, "yyyy年MM月dd日");
    }
    return result;
}

//获得月份字符串
function getDateStr(times) {
    var date = new Date();
    date.setTime(times);
    var m = date.format("M")
    switch (m) {
        case "1":
            return "一月";
            break;
        case "2":
            return "二月";
            break;
        case "3":
            return "三月";
            break;
        case "4":
            return "四月";
            break;
        case "5":
            return "五月";
            break;
        case "6":
            return "六月";
            break;
        case "7":
            return "七月";
            break;
        case "8":
            return "八月";
            break;
        case "9":
            return "九月";
            break;
        case "10":
            return "十月";
            break;
        case "11":
            return "十一月";
            break;
        case "12":
            return "十二月";
            break;
    }
}


//获得时间日期的格式化 格式化样式 ： 几分钟前 几小时前 几天前
function getDateFormatStr(times, format) {
    var date = new Date();
    date.setTime(times);
    return date.format(format);
}  //获得制定模板的时间格式化字符串
function getTimeStr(times) {
    var date = new Date();
    date.setTime(times);
    return date.format("hh:mm");
} //获得时间字符串
function getDayStr(times) {
    var date = new Date();
    date.setTime(times);
    return date.format("dd");
}

//获得日字符串
function getDayStr(times) {
    var date = new Date();
    date.setTime(times);
    return date.format("dd");
}


//计算时间
function getTimePre(times) {
    var result = "";
    var currentDatetime = new Date().getTime();
    var preTime = currentDatetime - times;
    var ss = 60 * 1000
    var mm = 60 * 60 * 1000;
    var hh = 60 * 60 * 24 * 1000;
    var MM = 60 * 60 * 24 * 30 * 1000;
    if (preTime > mm && preTime < hh) {
        result = parseInt(preTime / mm) + "小时前";
    } else if (preTime < mm) {
        var resultTime = preTime / ss;
        if (resultTime < 1) {
            result = "刚才"
        } else {
            result = parseInt(resultTime) + "分钟前"

        }
    } else if (preTime > hh) {
        result = parseInt(preTime / hh) + "天前"
    } else if (preTime > MM) {
        result = "很久以前"
    }
    return result;
}


//通用业务
function refreshPageEntity() {
    pageEntity.pageSize = 10;  //每页中包含多少数据
    pageEntity.pageIndex = 1; //当前属于第几页
}   //刷新分页信息
function redirectProtocol() {
    console.log("协议跳转");
}   //跳转到协议
function createMessageComment(messageId) {
    var artWorkId = getParamObject()["artWorkId"];
    var currentUserId = getCurrentUserId();
    redirectComment(artWorkId, currentUserId, messageId);
}      //新加一个动态的评论
function submitComment(commentid, messageid) {
    var comment = {};
    var message = {};
    var artWorkId = getParamObject()["artWorkId"];
    var currentUserId = getCurrentUserId();
    if (commentid != null && typeof commentid != "undefined") {
        if (typeof PageVariable.commentMap[commentid] != "undefined") {
            comment = PageVariable.commentMap[commentid];
        } else {
            for (var i = 0; i < PageVariable.artWorkComment.commentList.length; i++) {
                var commentTemp = PageVariable.artWorkComment.commentList[i];
                PageVariable.commentMap[commentTemp.id] = commentTemp;
            }
            comment = PageVariable.commentMap[commentid];
        }
    } else {
        commentid = "";
    }
    if (messageid != null && typeof messageid != "undefined") {

        if (typeof PageVariable.messageMap[messageid] != "undefined") {
            message = PageVariable.messageMap[messageid];
        } else {
            for (var i = 0; i < PageVariable.artWorkProject.messageList.length; i++) {
                var messageTemp = PageVariable.artWorkProject.messageList[i];
                PageVariable.messageMap[messageTemp.id] = messageTemp;
            }
            message = PageVariable.messageMap[messageid];
        }
    } else {
        messageid = "";
    }
    var creatorName = "";
    if (comment != null && typeof comment != "undefined" && typeof comment.creator != "undefined" && comment.creator != null) {
        creatorName = comment.creator.name;
    }
    redirectComment(artWorkId, currentUserId, commentid, messageid, creatorName);

}  //提交评论
//通用的工具
function timeEnd() {
    // 从新加载页面数据
    initPage(PageVariable.param.artWorkId, PageVariable.param.currentUserId, PageVariable.param.signmsg, PageVariable.param.timestamp)
}       //倒计时结束触发函数
function countDown(timestamp, callback) {
    var time_start = new Date().getTime(); //设定当前时间
    var time_end = new Date(timestamp).getTime(); //设定目标时间
    // 计算时间差
    var time_distance = time_end - time_start;

    if (time_distance < 0) {
        timeEnd();
    } else {
        // 时
        var int_hour = Math.floor(time_distance / 3600000)
        time_distance -= int_hour * 3600000;
        // 分
        var int_minute = Math.floor(time_distance / 60000)
        time_distance -= int_minute * 60000;
        // 秒
        var int_second = Math.floor(time_distance / 1000)
        // 时分秒为单数时、前面加零
        if (int_hour < 10) {
            int_hour = "0" + int_hour;
        }
        if (int_minute < 10) {
            int_minute = "0" + int_minute;
        }
        if (int_second < 10) {
            int_second = "0" + int_second;
        }
        // 显示时间
        $("#hh").html(int_hour);
        $("#mm").html(int_minute);
        $("#ss").html(int_second);
        // 设置定时器
        if (typeof callback == "function") {
            callback();
        }
        setTimeout(function () {
            countDown(timestamp, callback)
        }, 1000);
    }
}   //倒计时工具
function dealUsername(str, frontLen, endLen) {
    var len = str.length - frontLen - endLen;
    var xing = '';
    for (var i = 0; i < len; i++) {
        xing += '*';
    }
    return str.substr(0, frontLen) + xing + str.substr(str.length - endLen);
}  //处理用户名显示方式 手机号中间部分用*代替
function tabsHeight() {
    var $container = $('#tabs-container'),
        h = $('#tabs-container').find('.swiper-slide-active').height();
    // console.log(h)
    $('.com-nav').css({'height': h + 49 + 'px'});
}   //计算高度
//通用的全局变量（有一部分在各自的model.js中）
var Template = {
    artWorkDetail: "artWorkDetail",
    artWorkBaseInfoMain2: "artWorkBaseInfoMain2",
    artWorkBaseInfoMain: "artWorkBaseInfoMain",
    artWorkBaseInfoPicture: "artWorkBaseInfoPicture",
    artWorkScheduleInvest: "artWorkScheduleInvest",
    artWorkScheduleCreate: "artWorkScheduleCreate",
    artWorkScheduleMessage: "artWorkScheduleMessage",
    artWorkScheduleAuction: "artWorkScheduleAuction",
    artWorkComment: "artWorkComment",
    artWorkInvestRecordTop: "artWorkInvestRecordTop",
    artWorkInvestRecordList: "artWorkInvestRecordList",
    artWorkBaseInfoAuctionMessage: "artWorkBaseInfoAuctionMessage",
    artWorkAuctionBiddingTop: "artWorkAuctionBiddingTop",
    artWorkAuctionWinner: "artWorkAuctionWinner",
    artWorkScheduleAuctionResult: "artWorkScheduleAuctionResult",
    artWorkAuctionBidding: "artWorkAuctionBidding",
    preBottomButton: "preBottomButton",
    beingBottomButton: "beingBottomButton",
    afterBottomButton: "afterBottomButton",
    artWorkMaster: "artWorkMaster",
    artWorkProgressPicture: "artWorkProgressPicture",//进度主图片
    artWorkProjectPicture: "artWorkProjectPicture", //项目主图片
    artWorkBaseInfoTitle: "artWorkBaseInfoTitle",  //标题
    artWorkMasterName: "artWorkMasterName",  //大师名称
    artWorkDaybefore: "artWorkDaybefore",  //大师名称
    artWorkBrief: "artWorkBrief",  //项目简介
    investGoalMoney: "investGoalMoney",  //项目简介
    artWorkHeadUrl: "artWorkHeadUrl",  //项目简介
    artWorkMasterTitle: "artWorkMasterTitle",  //大师简介
    artWorkMasterWorkNum: "artWorkMasterWorkNum",  //大师作品总数
    artWorkMasterFansNum: "artWorkMasterFansNum",  //大师粉丝总数
    artWorkDataInfo: "artWorkDataInfo",  //项目投资截止时间和投资信息
    messagePraise: "messagePraise",
    messageButtons: "messageButtons"
}   //dot.js 模板的映射对象
var pageEntity = {
    pageSize: 10,
    pageIndex: 1
};     //分页信息对象
var swiperContainerOption = {};             //swiper插件使用的配置对象
var hostName ="http://192.168.1.75:8080"; //域名对象
//var hostName = "http://ryt.efeiyi.com";//服务器域名
var RequestUrl = {};  //接口对象
