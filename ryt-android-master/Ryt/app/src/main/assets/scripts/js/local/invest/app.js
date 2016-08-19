/**
 * Created by Administrator on 2016/6/2 0002.
 */


// hostName = "http://192.168.1.75:8080";                           //服务器域名
// hostName = "http://localhost";                           //服务器域名
// hostName = "http://ryt.efeiyi.com";                           //服务器域名

function redirectUser(userid) {
    //window.demo1.clickOnAndroid1(userid);
}          // 跳转到用户的个人主页


function redirectPay(price, action) {
    var artWorkId = getParamObject()["artWorkId"];
    var currentUserId = getCurrentUserId();
    var type = "1"; //alipay
    window.location.href = "pay://jumpToPayUser_andPayArtWorkId_andPayMoney_andPayAction_andPayType_?"+currentUserId + "&" +artWorkId  +"&" + price +"&" + action +"&" + type;
    
    //去充值 去付尾款
}      //@TODO 跳转到付款的页面


//function redirectComment(userid) {
//    //跳转评论
//    window.location.href = "comment://jumpToCommentUser_andCommentArtWorkId_?"+userid + "&qydeyugqqiugd2";
//}       //@TODO 跳转到评论

function redirectComment(artworkId, currentUserId, messageId, commentid) {
    window.location.href = "comment://jumpToCommentUser_andCommentArtWorkId_andCommentMessageId_andCommentFatherCommentId_?"+currentUserId + "&" + artworkId + "&" + messageId + "&" + commentid;
}

function redirectComment(artworkId, currentUserId, messageId, commentid, name) {
    window.location.href = "comment://jumpToCommentUser_andCommentArtWorkId_andCommentMessageId_andCommentFatherCommentId_andName_?"+currentUserId + "&" + artworkId + "&" + messageId + "&" + commentid + "&" + name;
    
}       //@TODO 跳转到评论

function redirectConsumerAddress() {
    //var userId = getCurrentUserId();
    window.location.href = "consumerAddress://jumpToConsumerAddress_?";
}