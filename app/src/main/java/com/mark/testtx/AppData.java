package com.mark.testtx;

import android.os.Environment;

/**
 * Created by Mark.Han on 2017/4/20.
 */
public class AppData {

    public static String BASE_URL = "http://vod.zfwx.com/interface.php";
    public static String playUrl = "rtmp://play-lss.qupaicloud.com/spaceName/streamName";
    //    public static String playUrl_flv = "https://play-lss.qupaicloud.com/be07feff063ab9819ec76f350dea9097/be07feff063ab9819ec76f350dea9097-23E0E.flv?auth_key=2092656256-0-3939-226b362efbb0acaea8a2412d242328f7";
    public static String playUrl_m3 = "rtmp://play-lss.qupaicloud.com/spaceName/streamName.m3u8";


    //    public static final String pushUrl = "rtmp://push-lss.qupaicloud.com/be07feff063ab9819ec76f350dea9097/be07feff063ab9819ec76f350dea9097-23E12?auth_key=2092668672-0-3939-8dd77d9d366a796c9ee126269b158494";
    public static final String pushUrl = "rtmp://8982.livepush.myqcloud.com/live/8982_2201b2773a?bizid=8982&txSecret=b42e396fb6cd7a8662bb9f2cbe95fbc8&txTime=58FA2C7F";
    //    public static final String pullUrl = "https://play-lss.qupaicloud.com/be07feff063ab9819ec76f350dea9097/be07feff063ab9819ec76f350dea9097-23E12.flv?auth_key=2092668672-0-3939-2682ec9a664bff744d016a5e0c2fc081";
//    public static final String pullUrl = "http://8982.liveplay.myqcloud.com/live/8982_94e32e9fd2.flv";

    public static final String pullUrl = "http://8982.liveplay.myqcloud.com/live/8982_2201b2773a.flv";

    // 商城图片拼接URL;
    public static final String MARKET_IMAGE_URL = "http://admin.zfwx.com/GI/";//测试
    public static final String MARKET_DOWNLODFIRE_URL = "http://admin.zfwx.com/GF/";//测试
    public static final String SERVER_URL = "https://devapi.zfwx.com/"; // 普通服务器接口调用地址前缀，发布前请确认

//	public static final String MARKET_IMAGE_URL = "http://adminn.zfwx.com/GI/";//正式
//	public static final String MARKET_DOWNLODFIRE_URL = "http:/http/admin.zfwx.com/GF/";//正式
    //	public static final String SERVER_URL = "http://api.zfwx.com/"; // 普通服务器接口调用地址前缀，发布前请确认

    public static final String ANSWER_HEADER_URL = "http://admin.zfwx.com/upload/";
    public static final String WXDY_SERVER_URL = "http://wxdy.zfwx.com/"; // 普通服务器接口调用地址前缀，发布前请确认
//	public static final String SERVER_URL = "http://devapi.zfwx.com/"; // 正式
//	public static final String SERVER_URL = "http://preapi.zfwx.com/"; // 灰度


    public static final String V3_SERVER_URL = "http://cas.zfwx.com/"; // cas服务器接口调用地址前缀，发布前请确认
    public static final String SERVER_NEW_URL = "http://base.zfwx.com/";//dev 测试
//	public static final String SERVER_NEW_URL = "http://base.zfwx.com/";//dev 正式
//	public static final String SERVER_NEW_URL = "http://prebase.zfwx.com/";//灰x度


    public static final String IMAGE_URL = "http://www.zfwx.com/";// upload/";
    // //普通课程图片地址前缀，发布前请确认
//	public static f inal String IMAGE_URL = "http://zjwww.zfwx.com/";// upload/"; 语音图片测试地址
    // //普通课程图片地址前缀，发布前请确认
    public static final String IMAGE_MAJOR_URL = "http://www.zfwx.com"; // 专业课程图片地址前缀，发布前请确认
    public static final String IMAGE_URL_TEACHER = "http://www.zfwx.com/";
    public static final String CODE_URL = "http://admin.zfwx.com"; // 二维码图片地址前缀，发布前请确认
//	public static final String CODE_URL = "http://preadmin.zfwx.com"; // 二维码图片地址前缀，发布前请确认 灰度

    // public static final String ALLIANCE_URL = "http://devlm.51dj.cn";
    // //联盟图片地址前缀
    public static final String HEAD_URL = SERVER_NEW_URL + "photo/"; // 头像图片地址前缀，发布前请确认
    public static final String BAIDU_STRKEY = "760bab3d82c29c767eae291008aa242d"; // 百度地图唯一key，发布前请确认
    // 打包发布
    public static final String SERVER_HOST = "114.112.88.244"; // openfire服务器所在ip
    // http://114.112.88.244:9090/login.jsp
    // 测试环境121.40.51.45
    public static final String SERVER_NAME = "localhost.localdomain"; // 设 置openfire时的服务器名
    public static final int SERVER_PORT = 5222; // 服务端口 可以在openfire上设置s
    public static final String PAY_ZFB_CASH = "0"; // 支付宝支付金额，测试用0.01，发布时请设置为0
    public static final String WEBPAY_VIEW_NOTIFY_URL = SERVER_URL
            + "v3/system/zhifubao.json";// 后台接收支付宝异步通知地址前缀，发布前请确认
    public static Object isxmppon = null; // XMPP监听线程是否已启动，若为true（启动），则不再在初始页面重复启动线程
    // public static final String SERVER_URL =
    // "http://192.168.1.107:8280/zfwx-api/";
    // public static final String V3_SERVER_URL =
    // "http://192.168.1.107:8280/cas/";
    // public static final String SERVER_NEW_URL = "http://192.168.1.107:9084/";
    // public static final String IMAGE_URL =
    // "http://192.168.1.107:9080";//"http://www.zfwx.com/upload/";
    // public static final String IMAGE_URL_TEACHER =
    // "http://www.zfwx.com/upload/";
    // public static final String IMAGE_MAJOR_URL = "http://192.168.1.107:9080";
    // public static final String CODE_URL = "http://192.168.1.107:9080/";
    // public static final String HEAD_URL = "http://192.168.1.107:9084/photo/";
    // public static final String BAIDU_STRKEY =
    // "2KSBXNMEt8APu4Tfj5yWoaV8";//手机测试
    // public static final String SERVER_HOST = "192.168.1.107";
    // //http://192.168.1.107:9898/login.jsp
    // public static final String SERVER_NAME = "namenode";
    // public static final int SERVER_PORT = 5222;
    // public static final String PAY_ZFB_CASH = "0.01";
    // public static final String WEBPAY_VIEW_NOTIFY_URL =
    // "http://114.252.27.141:8280/zfwx-api/v3/system/zhifubao.json";
    //
    /*---------------------------------------分隔-----------------------------------------------*/
    // TODO 灰度发布下载地址
    public static final String UPDATE_PRE_URL = "http://download.zfwx.com/zfwx-android-pre.apk";
    public static final String UPDATE_URL = "http://download.zfwx.com/zfwx-android.apk";
    public static final String PACKAGE_NAME = "com.dj.zfwx.client.activity";
    public static final String PREF_SETTING_FILENAME = "zfwx_setting";
    public static final int PULL_SIZE = 20;
    public static final int FIRST_PAGE_SIZE = 10;
    public static final int COURSE_PAGE_SIZE = 70;
    public static final String PAGE_SIZE = "10";
    public static final String OPENFIRE_PWD = "12345678";
    public static final int START_PLAY_VIDEOVIEW = 12994;

    /**
     * 百度云服务 开发者服务管理 ID 2806375 http://developer.baidu.com/console#app/2806375
     * SK CraSf9TnQNOTdeLdpWEzm8laDCpLCBnB
     */
    public static final String BAIDU_APP_KEY = "X5Sw1y7yycWCdoybqbu1iBNh";
    public static final String BAIDU_SECRET_KEY = "CraSf9TnQNOTdeLdpWEzm8laDCpLCBnB";

    public static final String WEIBO_APP_KEY = "3412043748";// 当前应用的微博
    // APP_KEY，发布前请仔细检查

    public static final String WECHAT_PAY_APPID = "wxafbc1048f4a2ffb5"; // 微信支付AppID
    public static final String WECHAT_PAY_APPSECRET = "c731f2fc91b2d521ed2e18fbbd504f30"; // 微信支付AppSecret
    public static final String WECHAT_PAY_PARTNERID = "1218565601"; // 微信支付商户号
    public static final String WECHAT_LOGIN_STATE = "wechat_get_userinfo_login";// 微信第三方登录，用于保持请求和回调的状态，授权请求后原样带回给第三方。
    public static final String WECHAT_BIND_STATE = "wechat_get_userinfo_bind";// 微信帐号绑定，用于保持请求和回调的状态，授权请求后原样带回给第三方。
    public static final String WECHAT_SHARE_STATE = "wechat_discuss_share_bind";// 微信分享，用于保持请求和回调的状态，授权请求后原样带回给第三方。

    public static final String QQ_APPID = "101073317"; // QQAppID
    public static final String QQ_APPKEY = "3494137bea731fe48150e8b3a1566660"; // QQAppKey

    public static final int TAB_NO = 5; // 选课页一条中显示几个页卡
    public static final int NOTIFICATION_ID = 101; // 消息id，用于删除
    public static final int PAGE_TOTAL = 5;
    public static final int CASHTOHOUR = 150; // 点币相对于课时的换算率
    public static final int CANCELSUSWINDOW = 10033; // 还原购物车icon上的数字
    public static final int PHONE_WIDTH = 720;// 标准屏幕宽度
    public static final int CWJ_HEAP_SIZE = 6 * 1024 * 1024; // 设置最小堆内存
    public static final int REMAIN_COURSE_THRESHOLD = 3; // 会员升级，课时判断阀值

    public static final int CHANGE_COURSE_TAB_VIEW = 10217;

    public static final int CHANGE_COURSE_COMMON = 10218;
    public static final int VIP_TO_LECTURE_VIDEO = 10219;
    public static final int VIP_TO_SHOWCARD = 10220;

    // public static final int VIP_TO_LECTURE_PPT = 10236; //vip跳到相关下载

    public static final int WEBPAY_FAILED = 10220;
    public static final int WEBPAY_SERVICE_SUCCESS = 10221;

    public static final int VIDEO_PLAY_SERVICE_STOP = 10223;
    public static final int RESTART_VIDEO_SUCCESS = 10224;
    public static final int VIDEO_PLAY_SERVICE_CREATE = 10225;
    public static final int VIDEO_START_SUCCESS = 10226;
    public static final int VIDEO_PAUSE_SUCCESS = 10227;
    public static final int VIDEO_SEND_SUCCESS = 10228;

    public static final int VIDEO_GET_STOP_SUCCESS = 10229;
    public static final int VIDEO_COMPLETION_SUCCESS = 10230;
    public static final int VIDEO_VIEW_ERROR = 10231;

    public static final int VIDEO_PAUSE_NORMAL = 10232;

    public static final int SUSPENTIONWINDOW_VIEW_HIDE = 10233;// 悬浮窗消失
    public static final int SUSPENTIONWINDOW_VIEW_CANCEL = 10234;// 悬浮窗关闭
    // 12-6新增 音视频播放切换（解决三星5.0音视频播放失败）
    public static final int VIDEO_SWITCH = 10235; // 切换音视频

    public static final String WEBPAY_VIEW_BACKURL_SUCCESS = "http://www.huangqian.com/success.html";
    public static final String WEBPAY_VIEW_BACKURL_FAILED = "http://www.huangqian.com/failed.html";
    /**
     * 当前应用的回调页，发布前请仔细检查。 调整后请在新浪微博开放平台进行同步修改
     */
    public static final String REDIRECT_URL = "http://api.weibo.com/oauth2/default.html";

    public static final String PATH_VOICE = Environment
            .getExternalStorageDirectory() + "/zfwx/voice/";
    public static final String PATH_PLAY = Environment
            .getExternalStorageDirectory() + "/zfwx/play";
    public static final String PATH_IMG = Environment
            .getExternalStorageDirectory() + "/zfwx/img";
    public static final String PATH_MEDIA = Environment
            .getExternalStorageDirectory() + "/media";

    public static final String COURSE_GROUP_CHOOSE = "course_group_choose";

    public static final String ORDER_IDS = "order_ids"; // 面授订单序列
    public static final String COURSEID = "courseid";
    // TODO 灰度发布的全开放列表和部分开放列表
    public static final String GRAY_RELEASE_ALL_LIST = "gray_release_all_list";
    public static final String GRAY_RELEASE_PART_LIST = "gray_release_part_list";

    // 面授、专业教室 调起支付使用的 原订单号
    public static final String PAY_M_ORDERID = "pay_m_orderid";
    // 新增（后台获取）org_allbelongs
    public static final String ORG_ALLBELONGS = "org_allbelongs";
    public static final String ACCOUNT_ID = "account_id"; // 新增（后台获取）account_id
    public static final String PREF_ACCOUNT = "account";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_AUTO_LOGIN = "autologin";
    public static final String PREF_HAS_WELCOME = "Welcome";
    public static final String PREF_JSESSION_TOKEN = "Jsession_token";
    public static final String PREF_BAIDU_PUSH_CHANNELID = "Baidu_push_channelid";
    public static final String PREF_USER_JOB = "user_job";
    public static final String PREF_USER_ROLE = "user_permission";
    public static final String FEEDBACK_SIZE_PRE = "fbVector_size";
    public static final String FEEDBACK_NAME_PRE = "fbVector_";
    public static final String MESSAGE_SIZE_PRE = "messageVector_size";
    public static final String MESSAGE_NAME_PRE = "messageVector_";

    public static final String SYSTEM_SINA_WEIBO_TOKEN = "system_sina_weibo_token";
    public static final String SYSTEM_SINA_WEIBO_TIME = "system_sina_weibo_time";
    public static final String SYSTEM_SINA_WEIBO_UID = "system_sina_weibo_uid";

    public static final String SYSTEM_QQ_TOKEN = "system_qq_token";
    public static final String SYSTEM_QQ_TIME = "system_qq_time";
    public static final String SYSTEM_QQ_UID = "system_qq_uid";

    public static final String SYSTEM_WX_TOKEN = "system_wx_token";
    public static final String SYSTEM_WX_TIME = "system_wx_time";
    public static final String SYSTEM_WX_UID = "system_wx_uid";

    public static final String SYSTEM_ALERT_PLAY = "system_alert_play";
    public static final String SYSTEM_SEND_MESSAGE = "system_send_message";
    public static final String SYSTEM_SHOW_WINDOW = "system_show_window";
    public static final String SYSTEM_START_UPDATE = "system_start_update";
    public static final String SYSTEM_START_UPDATE_DESC = "system_start_update_desc";
    public static final String SYSTEM_ANDROID_APPID = "system_android_appid";
    public static final String SYSTEM_ANDROID_SESSIONID = "system_android_sessionid";
    public static final String SYSTEM_ACCOUNT_ISOPENRES = "system_account_isopenres_n";

    public static final String PREF_VERSION_NAME = "versionName";
    public static final String PREF_VERSION_CODE = "versionCode";
    public static final String PREF_LAST_UPGRAGE_CHECK_TIME = "lastupgradechecktime";
    public static final String PREF_JSONSERVERURL = "jsonserverurl";
    public static final String PREF_VERSION = "version";
    public static final String PREF_DEVICEMODEL = "deviceModel";
    public static final String PREF_DEVICETYPE = "deviceType";
    public static final String PREF_OSVERSION = "osVersion";
    public static final String PREF_UPGRADE = "upgrade";

    // 是否显示欢迎体验新功能dialog
    public static final String WHETHE_RSHOW_WEL_USE_NEW = "whetheshowhwelcomeusenew";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
     * <p/>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p/>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = "";

    // 页面跳转时翻转
    public static final int VIEW_FIRST_INVERSE = 1;
    public static final int VIEW_FIRST_START_INVERSE = 21;
    public static final int VIEW_WELCOME_TO_MAIN = 20011;
    public static final int VIEW_WELCOME_TO_COURSEMAIN = 20003;
    public static final int VIEW_SECOND_INVERSE = 3;

    // json表字段
    public static final String KEY = "key"; // 相当于表ID
    public static final String VALUE = "value"; // json值
    public static final String TIME = "time"; // 数据修改的最后时间，用于删除老数据

    public static final String VIEW_LIST_JSON_TABLE = "view_list_json_table";// list表名

    // 错误
    public static final int ERROR = 10001;
    public static final int FAILED = 10002;
    public static final int NO_LOGIN = 10008;
    public static final int WILL_LOGIN = 10011;
    public static final int FAILED_PLAY_DAY = 20406;
    public static final int FAILED_PLAY_TIME = 20408;
    public static final int FAILED_GET_CODE_PHONE = 20101;
    public static final int FAILED_GET_CODE_NAME = 20103;
    public static final int FAILED_DIAN_POST_MESSAGE = 60001;

    //听课 讲义模块
    public static final String STUDY_DOMAIN = "study_domain";

    //听课监控
    public static final String WEBSOCKETHEAD = "_online_user_func_";
    public static final String WEBSOCKETSENDMESSAGE = "_online_user_msg_";
    public static final String WEBSOCKETURL = "ws://121.40.34.17:8668";    //测试
    //	public static final String WEBSOCKETURL = "ws://120.55.185.40:8668";    //灰度
//	public static final String WEBSOCKETURL = "ws://djchat.zfwx.com:8668";   //正式
    public static final int VIDEO_PLAY_SERVICE_MONITOR_RESTART = 22222;

    //点读文章审核
    public static final int REVIEW_LEFT = 66660;
    public static final int REVIEW_CENTER = 66661;
    public static final int REVIEW_RIGHT = 66662;

    public static final int REVIEW_PASS_ING = 2;      //待审
    public static final int REVIEW_PASS = 8;          //通过
    public static final int REVIEW_FAIL = 4;          //不通过
    public static final int REVIEW_MODIFY = 1;       //修改

    public static final int REVIEW_RECOMMAND = -1; //推荐

    public static final int REVIEW_DELETE = 7;     //删除

    public static final String ISFROMREVIEWACTIVITY = "isFromReviewActivity";
    public static final String REVIEWCOURSEID = "reviewCoureseId";


    public static final int REVIEWDETAILACTIVITYBACK = 2222;
    public static final int REIVEWACTIVITYREQUESTCODE = 1000;

    public static final String MSG_TYPE_LISREA_VERIFY = "_sys_lisrea_verify";//点读上传文章                  跳转可以编辑的
    public static final String MSG_TYPE_LISREA_RESULT = "_sys_lisrea_result";//点读上传文章-作者-审核完成消息  跳转不可以编辑的
    public static final String MSG_TYPE_LISREA_CAST = "_sys_lisrea_cast";//点读上传文章-作者-广播消息           跳转不可以编辑的
    public static final String MSG_TYPE_SHOP_DIVIDE_FOR_COUNTER = "_sys_shop_divide_for_counter";//钱包消息
    public static final String MSG_TYPE_COURSE_BUY_LDT_FOR_FIN = "_sys_course_buy_ldt_for_fin";//钱包消息
    public static final String MSG_APP_MESSAGE_JGK_CAST = "app_message_jgk_cast";//点视课程APP广告带
    public static final String MSG_APP_MESSAGE_DJK_CAST = "app_message_djk_cast";//点睛课程发布公告广播-点睛
    public static final String MSG_APP_MESSAGE_OPEN_CAST = "app_message_open_cast";//点读
    public static final String MSG_APP_MESSAGE_ACT_CAST = "app_message_act_cast";//面授上线广播通知 x
    public static final String MSG_APP_MESSAGE_DJ_ACT_CAST = "app_message_dj_act_cast";//点睛面授公告发布广播-点睛

    public static final String MSG_TYPE_COURSE_COPYRIGHT = "_sys_shop_dtv_fin_copyright";
    public static final String MSG_TYPE_COURSE_USE = "_sys_shop_dtv_fin_download";
    public static final String MSG_TYPE_PURSE_COPYRIGHT = "_sys_shop_dtv_copyright"; // 跳转到用户钱包
    public static final String MSG_TYPE_PURSE_USE = "_sys_shop_dtv_download"; // 跳转到用户钱包


}
