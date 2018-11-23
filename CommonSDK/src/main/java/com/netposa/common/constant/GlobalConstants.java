package com.netposa.common.constant;

import com.netposa.common.utils.SPUtils;


/**
 * @class GlobalConstants
 * @brief 定义客户端全局常量
 */
public final class GlobalConstants {

    /**
     * 主数据库名字
     */
    public static final String MAIN_DB_NAME = "Tujie.db";

    /**
     * 后门Key
     */
    public static final String BACKDOOR_KEY = "backdoor_key";


    private static String getBaseUrl() {
        return SPUtils.getInstance().getString(BACKDOOR_KEY);
    }

    /**
     * 是否显示后门，允许用户修改服务器地址
     */
    public static final boolean SHOW_BACKDOOR = false;

    /**
     * callId
     */
    public static final String CALL_ID = "callId";

    /**
     * 事件存储的附件是否是缩略图，"0"，不是缩略图，"1"，是缩略图
     */
    public static final String EVENT_ATTACHMENT_NOT_THUMBNAIL = "0";

    /**
     * 事件存储的附件是否是缩略图，"0"，不是缩略图，"1"，是缩略图
     */
    public static final String EVENT_ATTACHMENT_IS_THUMBNAIL = "1";

    /**
     * 手机号码的长度PHONE_NUMBER_LENGHT
     */
    public static final int PHONE_NUMBER_LENGHT = 11;

    /**
     * 消息推送的类型，线索类型
     */
    public static final String PUSH_TYPE_CLUE = "cyqz_clue";

    /**
     * 消息推送
     */
    public static final String PUSH = "1";

    /**
     * 消息静默推送
     */
    public static final String NOT_PUSH = "2";

    // 用户类型
    public static final String USER_TYPE = "4";

    // 验证码类型，注册
    public static final String SMS_CODE_REGISTER = "1";

    // 验证码类型，修改密码
    public static final String SMS_CODE_CHANGE_PWD = "2";

    public static final String CONFIG_OPERATION_ID = "config_operation_id";

    /// 首次启动标志键名(值为boolean类型）
    public static final String CONFIG_FIRST_START_APP = "first_start_app";

    public static final String CONFIG_LAST_LY_TOKEN = "last_ly_token";

    public static final String CONFIG_LAST_LOGIN_USER_ACCOUNT = "config_last_login_user_account";
    //头像
    public static final String CONFIG_LAST_USER_AVATAR = "config_last_user_avatar";
    //昵称
    public static final String CONFIG_LAST_USER_NICKNAME = "config_last_user_nickname";
    // 登录名
    public static final String CONFIG_LAST_USER_LOGIN_NAME = "config_last_user_login_name";
    // 密码
    public static final String LOGIN_PASSWORD="login_password";
    // 是否登陆 标志位
    public static final String HAS_LOGIN = "has_login";

    // 是否刷脸 标志位
    public static final String HAS_FACE = "has_face";

    //  消息开关
    public static final String CONFIG_MESSAGE_SWITCH = "config_message_switch";

    public static final String GOTO_MAIN = "goto_main";

    public static final String DEFAULT_IMAGE_SUFFIX = ".jpeg";
    public static final String DEFAULT_VIDEO_SUFFIX = ".mp4";
    public static final String DEFAULT_IMAGE_GIF_SUFFIX = ".gif";

    public static final String DEFAULT_VIDEO_MIME_TYPE = "video/mp4";
    public static final String DEFAULT_PICTURE_MIME_TYPE = "image/jpeg";

    /**
     * 警情分享
     */
    public static final String CASE_SHARE = "1";

    /**
     * APP分享
     */
    public static final String APP_SHARE = "2";


    /**
     * 通知栏的通知id
     */
    public static final String NOTIFICATION_ID = "notification_id";

    /**
     * Token失效的字符串
     */
    public static final CharSequence TOKEN_INVALIDATE = "notification.token_invalid.login";

    public static final String EXTRA_NICKNAME = "extra_nickname";

    /**
     * 直播录像的录制存储路径
     */
    public static final String RECORD_PATH = "records/";

    /**
     * 直播录像的截图存储路径
     */
    public static final String SNAP_PATH = "snaps/";

    /**
     * 服务器时间
     */
    public static final String SERVER_DATE = "server_date";

    /**
     * 枪机
     */
    public static final int CAMERA_QIANG_JI = 0;

    /**
     * 球机
     */
    public static final int CAMERA_QIU_JI = 1;

    /**
     * 摄像机在线
     */
    public static final int CAMERA_ONLINE = 0;

    /**
     * 摄像机离线
     */
    public static final int CAMERA_OFFLINE = 1;

    /**
     * 新的抓拍机
     */
    public static final int ORG_CAMERA_ZHUAPAIJI = 100603;

    /**
     * 新的球机
     */
    public static final int ORG_CAMERA_QIUJI = 100602;

    /**
     * 大于此，为在线
     */
    public static final int ORG_CAMERA_ON_LINE = 100502;


    /**
     * 收藏摄像机
     */
    public static final String CAMERA_FAVORITE = "1";

    /**
     * 取消收藏摄像机
     */
    public static final String CAMERA_NOT_FAVORITE = "0";

    /**
     * 单兵功能的cid
     */
    public static final String CONFIG_SOLO_CID = "config_solo_cid";

    /**
     * 单兵功能的sn
     */
    public static final String CONFIG_SOLO_SN = "config_solo_sn";

    /**
     * 单兵功能的brand
     */
    public static final String CONFIG_SOLO_BRAND = "config_solo_brand";

    /**
     * 是否支持单兵功能
     */
    public static final String CONFIG_IS_SOLO = "config_is_solo";

    /**
     * 支持单兵功能
     */
    public static final int CONFIG_SUPPORT_SOLO = 1;

    /**
     * 不支持单兵功能
     */
    public static final int CONFIG_NOT_SUPPORT_SOLO = 0;

    public static final String SELECT_CAMERA = "selectCamera";

    public static final String CONFIG_CASE_ID = "config_case_id";

    public static final String CONFIG_CLUE_ID = "config_clue_id";

    /**
     * 本地存储的案件的最近更新时间
     */
    public static final String CONFIG_LAST_CASE_TIME = "config_last_case_time";
    /**
     * 本地存储的线索的最近更新时间
     */
    public static final String CONFIG_LAST_CLUE_TIME = "config_last_clue_time";

    /**
     * 重大警情级别
     */
    public static final String CASE_LEVEL_IMPORT = "02";

    /**
     * 是否是封面图片.
     */
    public static final String IS_FRONT_COVER = "1";

    /**
     * 是否已回复评论，0 未回复，1 已回复
     */
    public static final String REPLY_HAS_ANSWERED = "1";

    /**
     * 是否已审核，0 未审核 1已审核
     */
    public static final String HAS_AUDIT = "1";

    /**
     * 是否已审核通过，0 审核未通过，1审核通过
     */
    public static final String HAS_AUDIT_PASS = "1";

    /**
     * 点赞
     */
    public static final String LIKE_IT = "1";

    /**
     * 取消点赞
     */
    public static final String UNLIKE_IT = "0";

    /**
     * 是否允许评论，0 不允许，1 允许，默认是1
     */
    public static final String IS_ALLOW_REPLY = "1";

    /**
     * 是否允许提交警情关联线索，0 不允许，1 允许，默认是1
     */
    public static final String IS_ALLOW_CASE_REPORT = "1";

    public static final String CASE_ID = "case_id";

    /**
     * 是否置顶，0 不置顶，1 置顶，默认是0
     */
    public static final String OBJECT_STORAGE_IS_TOP = "0";

    /**
     * 是否需要审核，0 不需要，1 需要，默认是0
     */
    public static final String IS_ALLOW_AUDIT = "0";

    /**
     * 是否是有价值的线索
     */
    public static final String IS_VALUED_REPORT = "02";


    public static final String EXTRA_ADDR = "extra_location";
    public static final String EXTRA_LAT = "extra_lat";
    public static final String EXTRA_LON = "extra_lon";

    /**
     * 朝阳群众的token
     */
    public static final String CONFIG_CYQZ_TOKEN = "config_cyqz_token";

    /**
     * 智慧云眼token
     */
    public static final String UID = "uid";

    /**
     * token
     */
    public static final String TOKEN = "netposa_token";

    public static final String USER_NAME = "user_name";

    /**
     * 羚羊事件上传的token
     */
    public static final String CONFIG_CYQZ_LY_TOKEN = "config_cyqz_ly_token";

    public static final String INTENT_SHOW_SUCCESS_DIALOG = "intent_show_success_dialog";

    /**
     * 线索的类型.
     * "11": "一般违法线索",
     * "12": "严重违法线索",
     * "13": "各类安全隐患",
     * "14": "其它"
     */
    public static final int TYPE_1 = 11;
    public static final int TYPE_2 = 12;
    public static final int TYPE_3 = 13;
    public static final int TYPE_4 = 14;

    public static final String EXTRA_START_FROM_CLUE = "extra_start_from_clue";

    /**
     * 火眼金睛截图后目录
     */
    public static final String CROP_PIC_PATH = "crop_img/";

    public static final String AFTER_LOGIN_SET_JPUSH_ALIAS_SUCCESS = "after_login_set_jpush_alias";

    /**
     * 收藏的JSON串
     */
    public static final String COLLECT_JSON = "collect_json";

    /**
     * 评论类型，1.关注人提交线索 2.新闻评论 3.新闻回复  4.线索回复
     */
    public static final String REPLY_TYPE_NEWS_COMMENT = "2";

    public static final String CONFIG_REPLY_ID = "config_reply_id";

    public static final String CONFIG_CONTENT_ID = "config_content_id";


    /**
     * 评论类型，1.关注人提交线索 2.新闻评论 3.新闻回复  4.线索回复
     */
    public static final String REPLY_TYPE_COMMENT_REPLY = "3";


    /**
     * 评论类型，1.关注人提交线索 2.新闻评论 3.新闻回复  4.线索回复
     */
    public static final String REPLY_TYPE_CLUE_REPLY = "4";

    public static final String CAMERA_STATUS_CHANGE = "camera_status_change";

    public static final String REPLY_NEWS = "reply_news";

    //图片预览
    public static final String EXTRA_SHOW_DOWNLOAD = "extra_show_download";
    public static final String EXTRA_SAVE_DIR = "extra_save_dir";

    /**
     * 图片压缩后的最大文件大小，单位为KB
     */
    public static final int MAX_IMAGE_SIZE = 300;

    /**
     * 图片最大边界
     */
    public static final int[] MAX_IMAGE_BOUNDS = new int[]{720, 1280};

    /**
     * 是否有火眼金睛
     */
    public static final String PERMISSION_HAS_HYJJ = "permission_hyjj";

    /**
     * int类型默认值
     */
    public static final int INT_DEFAULT_VALUE = 0;

    /**
     * 默认加载页大小
     */
    public static final int PAGE_SIZE_DEFAULT = 20;

    /**
     * 视频身份默认的加载页大小
     */
    public static final int VID_PAGE_SIZE = 20;

    /**
     * 无效计数
     */
    public static final int VID_COUNT_INVALID = -1;

    /**
     * 彩云档案无效计数
     */
    public static final int CYDA_COUNT_INVALID = -1;

    public static final int COUNT_INVALID = -1;

    /**
     * 图片地址
     */
    public static final String IMAGE_URL = "image_url";

    /**
     * VID号
     */
    public static final String KEY_VID_NUMER = "key_vid_numer";
    /**
     * 身份证号
     */
    public static final String KEY_ID_NUMER = "key_id_numer";

    public static final int NOTIFY_ID_DCJ = 0xf1;
    public static final int NOTIFY_ID_ALARM = 0xf2;

    /**
     * 用户ID
     */
    public static final String USER_ID = "user_id";

    /**
     * MAPBOX地图服务器
     */
    public static final String MAP_KEY="pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4M29iazA2Z2gycXA4N2pmbDZmangifQ.-g_vE53SD2WrJ6tFX7QHmA";

    /**
     * Baseurl 的key
     */
    public static final String KEY_BASE_URL = "baseUrl";

    /**
     * BASE URL
     */
    public static final String URL_BASE = "url_base";

    /**
     * 域名header
     */
    public static final String DOMAIN_NAME_HEADER = "Domain-Name: ";

    /**
     * 内外网标记
     * true为内网，false为外网
     */
    public static final String IS_LOCAL_NET = "is_local_net";

    /**
     * 外网ip 58.49.28.186:58287
     * 本地服务器调试loginIp="192.168.14.46:12000";
     */
    public static final String VIDEO_PLAY_OUTER_NET = "58.49.28.186:58287";
}

