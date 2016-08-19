package com.yxh.ryt;

public class Constants {
    //网络请求基地址
    public static final String BASE_PATH="http://192.168.1.75:8080/app/";
    //public static final String BASE_PATH="http://192.168.1.75:8080/app/";
    //public static final String BASE_PATH="http://craft.efeiyi.com/app-wikiServer/app/";
    //public static final String BASE_PATH="http://ryt.efeiyi.com/app/";
    //融艺投APP_KEY
    public static final String APP_KEY="BL2QEuXUXNoGbNeHObD4EzlX+KuGc70U";
    //微信APP_ID
    public static final String APP_ID = "wxc7f08565e496134b";
    public static final String APP_SECRET = "9a4bf578e18230df0e6d4074d94fc40d";
    // 微信请求基本路径
    public static final String WX_BASE_PATH = "https://api.weixin.qq.com/sns/";
    // 微信登陆请求路径
    public static final String WX_LOGIN_PATH = "http://192.168.1.212";
    //商户号
    public static final String MCH_ID = "1243015702";
    // 微信登陆成功广播ACTION
    public static final String WX_LOGIN_ACTION = "wx_login_success";
    public static final String WX_PAY_ACTION_SUCCESS = "wx_pay_success";
    public static final String WX_PAY_ACTION_FAILURE = "wx_pay_failure";
    public static final String WX_INSTALL_ACTION = "wx_install_action";
    public static final String WX_NOTINSTALL_ACTION = "wx_notinstall_action";
    public static final String[] INDEX_TITLE = new String[] { "融资", "创作", "拍卖" };
    public static final String[] RZ_TITLE = new String[] { "项目", "详情", "投资","历史作品" };
    public static final String[] CS_TITLE = new String[] { "进度", "项目", "详情"};
    public static final String[] MYPROJECT_TITLE = new String[] { "未审核", "融资", "创作", "拍卖", "已结束"};
    public static final String[] AS_TITLE = new String[] { "拍卖", "项目", "详情"};
    public static final String[] lottery_TITLE = new String[] { "抽奖", "项目", "详情" };
    public static  String[] ATTENTION_TITLE = new String[] { "艺术家(0)", "用户(0)" };

    public static final String[] PAIHANG_TITLE = new String[] { "投资者", "艺术家" };
    public static final String[] RONGZI_XQ_TITLE = new String[] { "项目详情","投资流程","用户评论", "投资记录" };
    public static final String[] USER_YSJ_TITLE = new String[] { "主页","简介","作品", "投过的","赞过的" };
    public static final String[] ARTIST_TITLE = new String[] { "主页","简介","作品", "投过的","赞过的"};
    public static final String[] USER_PT_TITLE = new String[] { "投过的","赞过的","简介"};
    public static final int pageSize=20;

    /**
     * 是否第一次登录的key
     */
    public static final String ISFIRSTENTER="isfirstenter";
    /*自动更新*/
    public static final String AUTO_UPDATE = "auto_update";

    // 编辑昵称
    public static final String EDIT_NICKNAME_SUC = "edit_nickname_suc";
}
