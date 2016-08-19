/**
 * Created by Administrator on 2016/6/2 0002.
 */

hostName = "http://ryt.efeiyi.com";//服务器域名
//hostName = "http://192.168.1.75:8080";                           //服务器域名

function redirectUser(userid) {
    //window.location.href = "rongyitou://jumpToUserHome_?" + userid;
    console.log("XXXXXXXXXXXXXXXXXXXXXXXX"+userid);
            window.demo1.clickOnAndroid1(userid);
}          // 跳转到用户的个人主页


function redirectPay(price, action) {
    var artWorkId = getParamObject()["artWorkId"];
    var currentUserId = getCurrentUserId();
    window.demo1.finalPayment(price,action,artWorkId);
    //去充值 去付尾款
}      // 跳转到付款的页面


function redirectComment(artworkId, currentUserId, messageId, fatherCommentId,name) {
    console.log("artworkId: "+artworkId);
    console.log("currentUserId: "+currentUserId);
    console.log("messageId: "+messageId);
    console.log("fatherCommentId: "+fatherCommentId);
    window.demo1.comment(artworkId, currentUserId, messageId, fatherCommentId,name);
}       //跳转到评论

function redirectConsumerAddress() {
    var userId = getCurrentUserId();
    window.location.href = "";
}