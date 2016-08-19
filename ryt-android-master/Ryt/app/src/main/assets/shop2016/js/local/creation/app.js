/**
* Created by Administrator on 2016/6/7 0007.
*/


function redirectUser(userid) {
    console.log("test");
//        window.location.href = "rongyitou://userid:skldjflksdjflk"
        console.log("XXXXXXXXXXXXXXXXXXXXXXXX"+userid);
        window.demo.clickOnAndroid(userid);
}

function redirectComment(artworkId, currentUserId, messageId, fatherCommentId,name) {
    console.log("artworkId: "+artworkId);
    console.log("currentUserId: "+currentUserId);
    console.log("messageId: "+messageId);
    console.log("fatherCommentId: "+fatherCommentId);
    window.demo.comment(artworkId, currentUserId, messageId, fatherCommentId,name);
}       //跳转到评论

//hostName = "http://192.168.1.75:8080";
hostName = "http://ryt.efeiyi.com";//服务器域名