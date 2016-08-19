/**
 * Created by Administrator on 2016/6/6 0006.
 */

function initPage(currentUserId) {
    var param = new Object();
    param.currentUserId = currentUserId;
    PageVariable.param = param;
    getAuctionOrderData("1", function (orderType) {
        getAuctionOrder(orderType);
    })
    getAuctionOrderData("2", function (orderType) {
        getAuctionOrder(orderType);
    })
    getAuctionOrderData("3", function (orderType) {
        getAuctionOrder(orderType);
    })
    getAuctionOrderData("4", function (orderType) {
        getAuctionOrder(orderType);
    })
}


var PageVariable = {
    allOrderList: "",
    wpOrderList: "",
    wrOrderList: "",
    finishedOrderList: ""
};    //页面中主要的全局变量

function getAuctionOrder(param) {
    var orderType = param.orderType;
    switch (orderType) {
        case "1":
            $("#allOrder").html(getAuctionOrderHtml(PageVariable.allOrderList));
            break;
        case "2":
            $("#wpOrder").html(getAuctionOrderHtml(PageVariable.wpOrderList));
            break;
        case "3":
            $("#wrOrder").html(getAuctionOrderHtml(PageVariable.wrOrderList));
            break;
        case "4":
            $("#finishedOrder").html(getAuctionOrderHtml(PageVariable.finishedOrderList));
            break;
    }
    tabsHeight();
}

function getAuctionOrderData(orderType, callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp;
            switch (orderType) {
                case "1":
                    PageVariable.allOrderList = obj.auctionOrderList;
                    break;
                case "2":
                    PageVariable.wpOrderList = obj.auctionOrderList;
                    break;
                case "3":
                    PageVariable.wrOrderList = obj.auctionOrderList;
                    break;
                case "4":
                    PageVariable.finishedOrderList = obj.auctionOrderList;
                    break;
            }
        }, data, callback, function () {
        }, {orderType: orderType});
    }
    var param = getParamObject();
    param["type"] = orderType
    param = dealRequestParam(param);
    ajaxRequest(hostName + RequestUrl.orderTab, param, success, function () {
    }, "post");
}


function getAuctionOrderHtml(it /**/) {
    var out = ' ';
    if (typeof it != "undefined" && it != null && it.length > 0) {
        out += ' ';
        for (var i = 0; i < it.length; i++) {
            var order = it[i];
            out += ' <a onclick="redirectOrderView(\'' + (order.id) + '\')"> <li class="pm_order"> <div class="phead"> <div class="number">订单编号：<span>' + (order.id) + '</span></div> <div class="state"> ';
            if (order.type == "0") {
                out += '待付尾款 ';
            } else if (order.type == "1") {
                out += '待发货 ';
            } else if (order.type == "2") {
                out += '交易成功 ';
            }
            out += ' </div> </div> <div class="pbody"> <div class="ppic"><a href=""><img src="' + (order.artwork.picture_url) + '" alt=""></a> </div> <div class="pcontent"> <div class="hone">' + (order.artwork.title) + '</div> <div class="htext">' + (order.artwork.brief) + '</div> <div class="hmoney">￥<span>' + (order.amount) + '</span></div> </div> </div> </li> </a> ';
        }
        out += ' ';
    } else {
        out += ' <li class="pm_order"> <div class="noorder"> <p>目前没有待收货订单</p> </div> </li> ';
    }
    return out;
}