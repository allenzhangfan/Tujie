package com.netposa.common.constant;

import android.os.Environment;

import com.netposa.common.BuildConfig;
import com.netposa.common.utils.SPUtils;

import java.io.File;


/**
 * @class GlobalConstants
 * @brief 定义客户端全局常量
 */
public final class GlobalConstants {

    /**
     * 外部存储区默认主目录
     */
    public static final String DEFAULT_EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * 主数据库名字
     */
    public static final String MAIN_DB_NAME = "Tujie.db";

    /**
     * 应用程序主目录（外部存储区）
     */
    public static final String MAIN_PATH = "Tujie/";

    /**
     * 应用程序的默认根目录
     */
    public static final String ROOT_PATH = DEFAULT_EXTERNAL_PATH + MAIN_PATH;

    /**
     * 下载目录
     */
    public static final String DOWNLOAD_PATH = ROOT_PATH + "download/";

    /**
     * 缓存目录
     */
    public static final String CACHE_PATH = ROOT_PATH + "cache/";

    /**
     * 头像目录
     */
    public static final String AVATAR_PATH = ROOT_PATH + "avatar/";

    /**
     * 拍照目录
     */
    public static final String PICTURE_PATH = ROOT_PATH + "picture/";

    /**
     * apk 更新下载包目录
     */
    public static final String APK_PATH = ROOT_PATH + "apk/";

    /**
     * video 暂存目录
     */
    public static final String VIDEO_PATH = ROOT_PATH + "video/";

    /**
     * 日志目录
     */
    public static final String LOG_PATH = ROOT_PATH + "log/";

    /**
     * db目录
     */
    public static final String DB_PATH = ROOT_PATH + "db/";

    /**
     * 图片压缩后的最大文件大小，单位为KB
     */
    public static final int MAX_IMAGE_SIZE = 300;

    /**
     * Log输出开关,true为输出log，false为关闭。上线时候需要改为false
     */
    public static final boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 国际版为true ｜ 国内版为false
     */
    public static final boolean INTERNATIONAL = false;

    public static final String PLATFORM_TYPE = "android";

    /**
     * 常量token
     */
    public static final String TOKEN = "token";

    /**
     * 常量uid
     */
    public static final String UID = "uid";

    /**
     * 服务器时间
     */
    public static final String SERVER_DATE = "server_date";

    /**
     * 极光推送成功的标示字符
     */
    public static final String JPUSH_SET_SUCCESS = "jpush_alias_success";

    /**
     * 再次运行程序
     */
    public static final String RUN_AGAIN = "isAgainRun";

    /**
     * 网络连接超时时间，单位为毫秒（15s）
     */
    public static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000;

    /**
     * 网络读写超时时间，单位为毫秒(15s)
     */
    public static final int READ_TIMEOUT_MILLIS = 15 * 1000;


    /**
     * session
     */
    public static final String SESSION = "SESSION";

    /**
     * 日志名字
     */
    public static final String LOG_FILE_NAME = "netposa_tujie.log";

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
    // 用户id
    public static final String CONFIG_LAST_USER_LOGIN_ID = "config_last_user_login_id";
    // 性别
    public static final String CONFIG_LAST_USER_GENDER = "config_last_user_gender";
    // 组织
    public static final String CONFIG_LAST_USER_GROUP = "config_last_user_group";
    // 警号
    public static final String CONFIG_LAST_USER_POLICE_NO = "config_last_user_police_no";
    // 电话
    public static final String CONFIG_LAST_USER_TEL_NO = "config_last_user_tel_no";
    // 密码
    public static final String LOGIN_PASSWORD = "login_password";
    // 是否登陆 标志位
    public static final String HAS_LOGIN = "has_login";
    // 是否第一次获取字典表
    public static final String HAS_FIRST_DICTIONARY = "has_first_dictionary";
    // 是否刷脸 标志位
    public static final String HAS_FACE = "has_face";
    //类型 人脸/车辆
    public static final String FACE_TYPE = "FACE";
    public static final String VEHICLE_TYPE = "VEHICLE";
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

    public static final String USER_NAME = "user_name";

    //性别
    public static final String TYPE_MALE = "0";//男性
    public static final String TYPE_FAMALE = "1";//女性
    public static final String TYPE_ALL = "";//所有
    //眼镜
    public static final String TYPE_WEAR_GLASS = "1";//戴眼镜
    public static final String TYPE_NO_WEAR_GLASS = "0";//未戴眼镜
    public static final String TYPE_OTHER = "-1";//其它

    //布控类型
    public static final int TYPE_CAR_DEPLOY = 1;//车辆布控
    public static final int TYPE_FACE_DEPLOY = 2;//人脸布控

    //警情处理状态
    public static final int TYPE_VALID = 2;//有效
    public static final int TYPE_INVALID = 1;//无效
    public static final int TYPE_SUSPENDING = 0;//待处理

    /**
     * 羚羊事件上传的token
     */
    public static final String CONFIG_CYQZ_LY_TOKEN = "config_cyqz_ly_token";

    public static final String INTENT_SHOW_SUCCESS_DIALOG = "intent_show_success_dialog";

    public static final String PART_NAME_IMAGE = "file";

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

    public static final int COUNT_INVALID = -1;

    public static final String FACE_CAPTURE_PIC = "face_capture.jpg";
    public static final String FACE_CAPTURE_PRESS_PIC = "face_capture_press.jpg";

    public static final String DEFAULT_CITY = "武汉市";

    public static final int NOTIFY_ID_ALARM = 0xf2;

    /**
     * 用户ID
     */
    public static final String USER_ID = "user_id";

    /**
     * MAPBOX地图服务器
     */
    public static final String MAP_KEY = "pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4M29iazA2Z2gycXA4N2pmbDZmangifQ.-g_vE53SD2WrJ6tFX7QHmA";

    /**
     * 地图缩小范围最大值
     */
    public static final float ZOOM_IN_MAX = 18;

    /**
     * 地图放大范围最小值
     */
    public static final float ZOOM_OUT_MIN = 5;

    /**
     * xml中默认大小(mapbox_cameraZoom)
     */
    public static final float DEFAULT_MAPBOX_CAMERAZOOM = 13;

    /**
     * BASE URL
     */
    public static final String URL_BASE = "url_base";

    /**
     * 域名header
     */
    public static final String DOMAIN_NAME_HEADER = "Domain-Name: ";

    /**
     * 视频播放外网ip  58.49.28.186:58287
     * 内网ip         192.168.101.31:554
     */
    public static final String OUTER_NET_PLAY_IP = "58.49.28.186:58287";

    /**
     * 图片外网ip   58.49.28.186:57199
     * 内网ip      172.16.90.168:6551
     */
    public static final String OUTER_NET_IMAGE_IP = "58.49.28.186:57199";

    /*********************************权限请求码*********************************/
    public static final int REQUEST_CODE_SPJK = 10001;
    public static final int REQUEST_CODE_YTST = 10002;
    public static final int REQUEST_CODE_SFJB = 10003;
    public static final int REQUEST_CODE_CLCX = 10004;
    public static final int REQUEST_CODE_RLTK = 10005;
    public static final int REQUEST_CODE_JQ = 10006;
    /*********************************权限请求码*********************************/

    /*********************************key值*********************************/

    /**
     * Baseurl 的key
     */
    public static final String KEY_BASE_URL = "baseUrl";

    /**
     * 跳转警情详情的key
     */
    public static final String KEY_JQ_ITEM = "jq_item";

    /**
     * 图片地址
     */
    public static final String IMAGE_URL = "image_url";

    /**
     * 播放地址
     */
    public static final String PLAY_URL = "play_ip_port";

    /**
     * 身份证号
     */
    public static final String KEY_ID_NUMER = "key_id_numer";

    /**
     * 标题
     */
    public static final String KEY_TITLE = "key_title";

    /**
     * 查看详情图片
     */
    public static final String KEY_IV_DETAIL = "key_iv_detail";

    /**
     * 查看图片时人像的名字
     */
    public static final String KEY_FACE_NAME = "key_name";

    /**
     * 查看详情地址
     */
    public static final String KEY_MOTIONNAME_DETAIL = "key_motionname_detail";

    public static final String KEY_SEESION = "key_sessiKEY_POSITIONon";
    public static final String KEY_POSITION = "key_position";
    public static final String KEY_PICPATH = "key_pic_path";
    public static final String KEY_DATA_KEY = "key_data_key";
    public static final String KEY_DATA_TYPE = "key_data_type";


    /**
     * 查看详情时间
     */
    public static final String KEY_TIME_DETAIL = "key_time_detail";

    /**
     * 查看详情类型
     */
    public static final String KEY_TYPE_DETAIL = "key_type_detail";

    /**
     * 查看详情类型(警情)
     */
    public static final String KEY_JIN_QING = "key_jin_qing";

    /**
     * 查看详情类型到以图搜图
     */
    public static final String KEY_TYPE_DETAIL_TO_YTST = "key_type_detail_to_ytst";

    /**
     * 刷脸登录传值的flag
     */
    public static final String FACE_lOGIN = "face_login";

    /**
     * 内外网标记
     * true为内网，false为外网
     */
    public static final String IS_LOCAL_NET = "is_local_net";

    /**
     * 单个摄像头当前位置的经纬度
     */
    public static final String KEY_SINGLE_CAMERA_LOCATION_LATITUDE = "key_single_camera_location_laitude";
    public static final String KEY_SINGLE_CAMERA_LOCATION_LONGITUDE = "key_single_camera_location_longitude";

    /**
     * 身份鉴别featur 的key
     */
    public static final String KEY_FEATUR = "key_featur";

    /**
     * 身份鉴别抓拍图片 的key
     */
    public static final String KEY_ZHUAPAI_URL = "key_zhuapai_url";

    /**
     * 传送参数轨迹的key
     */
    public static final String KEY_TRACK_ENTITY = "key_track_entiy";


    /*********************************key值*********************************/
}

