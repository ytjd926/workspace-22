
package com.jovision;

import android.os.Environment;

import java.io.File;

public class Consts {
    public static final String SD_CARD_PATH = Environment
            .getExternalStorageDirectory().getPath() + File.separator;

    public static final String LOG_PATH = SD_CARD_PATH + "JVDemo";

    public static final int CALL_CONNECT_CHANGE = 0xA1;
    public static final int CALL_NORMAL_DATA = 0xA2;
    public static final int CALL_CHECK_RESULT = 0xA3;
    public static final int CALL_CHAT_DATA = 0xA4;
    public static final int CALL_TEXT_DATA = 0xA5;
    public static final int CALL_DOWNLOAD = 0xA6;
    public static final int CALL_PLAY_DATA = 0xA7;
    public static final int CALL_LAN_SEARCH = 0xA8;
    public static final int CALL_NEW_PICTURE = 0xA9;
    public static final int CALL_STAT_REPORT = 0xAA;
    public static final int CALL_GOT_SCREENSHOT = 0xAB;
    public static final int CALL_PLAY_DOOMED = 0xAC;
    public static final int CALL_PLAY_AUDIO = 0xAD;
    public static final int CALL_QUERY_DEVICE = 0xAE;
    public static final int CALL_HDEC_TYPE = 0xAF;
    public static final int CALL_LIB_UNLOAD = 0xB0;
    public static final int CALL_GEN_VOICE = 0xB1;

    public static final int ARG1_PLAY_BAD = 0x01;

    public static final int PLAYBACK_DONE = 0x10;

    public static final int BAD_HAS_CONNECTED = -1;
    public static final int BAD_CONN_OVERFLOW = -2;
    public static final int BAD_NOT_CONNECT = -3;
    public static final int BAD_ARRAY_OVERFLOW = -4;
    public static final int BAD_CONN_UNKOWN = -5;

    public static final int ARG2_REMOTE_PLAY_OVER = 0x32;
    public static final int ARG2_REMOTE_PLAY_ERROR = 0x39;
    public static final int ARG2_REMOTE_PLAY_TIMEOUT = 0x77;

    public static final int WHAT_DUMMY = 0x04;

    // RemotePlayBackActivity
    public static final int WHAT_REMOTE_DATA_SUCCESS = 0x29;// 数据加载成功
    public static final int WHAT_REMOTE_DATA_FAILED = 0x2A;// 数据加载失败
    public static final int WHAT_REMOTE_NO_DATA_FAILED = 0x2B;// 暂无远程视频
    public static final int WHAT_REMOTE_START_PLAY = 0x2C;// 开始播放远程视频
    public static final int WHAT_REMOTE_PLAY_FAILED = 0x2D;// 播放失败
    public static final int WHAT_REMOTE_PLAY_NOT_SUPPORT = 0x2E;// 手机暂不支持播放1080p的视频
    public static final int WHAT_REMOTE_PLAY_EXCEPTION = 0x2F;// 与主控断开连接
    public static final int WHAT_REMOTE_PLAY_DISMISS_PROGRESS = 0x30;// 隐藏远程回放进度条

    public static final int WHAT_PLAY_AUDIO_WHAT = 0x3A;
    public static final int WHAT_SEND_WAVE_FINISHED = 0x3B;// 声波发送完毕
    public static final int WHAT_SEND_WAVE = 0x3F;// 发送声波命令

    public static final String FORMATTER_AP_SWITCH = "apModeOn=%d";
    public static final String FORMATTER_SET_BPS_FPS = "[CH%d];width=%d;height=%d;nMBPH=%d;framerate=%d;rcMode=%d;";
    public static final int TYPE_SET_PARAM = 0x03;

    public static final int EX_COMTRANS_SEND = 0x26; // 串口发
    public static final int EX_COMTRANS_RESV = 0x27; // 串口收

    public static final int PLAY_FINISH = 0x28; // finish 播放界面

    /********************************* 　以下修改设备用户名密码需要的宏定义　 ***************************************/
    public static final int SECRET_KEY = 0x1053564A;
    public static final int MAX_ACCOUNT = 13;
    public static final int SIZE_ID = 20;
    public static final int SIZE_PW = 20;
    public static final int SIZE_DESCRIPT = 32;

    // 用户组定义
    public static final int POWER_GUEST = 0x0001;
    public static final int POWER_USER = 0x0002;
    public static final int POWER_ADMIN = 0x0004;
    public static final int POWER_FIXED = 0x0010;

    // 帐户管理指令,lck20120308
    public static final int EX_ACCOUNT_OK = 0x01;
    public static final int EX_ACCOUNT_ERR = 0x02;
    public static final int EX_ACCOUNT_REFRESH = 0x03;
    public static final int EX_ACCOUNT_ADD = 0x04;
    public static final int EX_ACCOUNT_DEL = 0x05;
    public static final int EX_ACCOUNT_MODIFY = 0x06;

    // 操作状态
    public static final int ERR_OK = 0x0; // 修改成功 
    public static final int ERR_EXISTED = 0x1; // 用户已存在 
    public static final int ERR_LIMITED = 0x2; // 用户太多，超出了限制 
    public static final int ERR_NOTEXIST = 0x3; // 指定的用户不存在
    public static final int ERR_PASSWD = 0x4; // 密码错误
    public static final int ERR_PERMISION_DENIED = 0x5;// 无权限

    /********************************* 　以上修改设备用户名密码需要的宏定义　 ***************************************/

    /********************************* 　以下修改设备用户名密码需要的宏定义　 ***************************************/

    // 扩展类型，用于指定哪个模块去处理,lck20120206
    public static final int RC_EX_COVERRGN = 0x05;
    public static final int RC_EX_MDRGN = 0X06;
    public static final int RC_EX_ROI = 0x0d;

    // 视频播放状态
    public static final int TAG_PLAY_CONNECTING = 1;// 连接中
    public static final int TAG_PLAY_CONNECTTED = 2;// 已连接
    public static final int TAG_PLAY_DIS_CONNECTTED = 3;// 断开
    public static final int TAG_PLAY_CONNECTING_BUFFER = 4;// 连接成功，正在缓冲数据。。。
    public static final int TAG_PLAY_STATUS_UNKNOWN = 5;// 未知状态
    public static final int TAG_PLAY_BUFFERING = 6;// 已连接，正在缓冲进度
    public static final int TAG_PLAY_BUFFERED = 7;// 已连接，缓冲成功

    // 命令定义 // 扩展类型，用于指定哪个模块去处理,lck20120206
    public static final int RC_EX_FIRMUP = 0x01;
    public static final int RC_EX_NETWORK = 0x02;
    public static final int RC_EX_STORAGE = 0x03;
    public static final int RC_EX_ACCOUNT = 0x04;
    public static final int RC_EX_PRIVACY = 0x05;
    public static final int RC_EX_MD = 0x06;
    public static final int RC_EX_ALARM = 0x07;
    public static final int RC_EX_SENSOR = 0x08;
    public static final int RC_EX_PTZ = 0x09;
    public static final int RC_EX_AUDIO = 0x0a;
    public static final int RC_EX_ALARMIN = 0x0b;
    public static final int RC_EX_REGISTER = 0x0c;
    public static final int RC_EX_EXPOSURE = 0x0d;
    public static final int RC_EX_QRCODE = 0x0e;
    public static final int RC_EX_IVP = 0x0f;
    public static final int RC_EX_DOORALARM = 0x10;
    public static final int RC_EX_PTZUPDATE = 0x11;
    public static final int RC_EX_COMTRANS = 0x12;

    // ------------------网络设置-------------------------------------
    public static final int EX_ADSL_ON = 0x01; // 连接ADSL消息
    public static final int EX_ADSL_OFF = 0x02; // 断开ADSL消息
    public static final int EX_WIFI_AP = 0x03; // 获取AP消息
    public static final int EX_WIFI_ON = 0x04; // 连接wifi
    public static final int EX_WIFI_OFF = 0x05; // 断开wifi
    public static final int EX_NETWORK_OK = 0x06; // 设置成功
    public static final int EX_UPDATE_ETH = 0x07; // 修改eth网络信息
    public static final int EX_NW_REFRESH = 0x08; // 刷新当前网络信息
    public static final int EX_NW_SUBMIT = 0x09; // 刷新当前网络信息
    public static final int EX_WIFI_CON = 0x0a;
    public static final int EX_WIFI_AP_CONFIG = 0x0b;
    public static final int EX_START_AP = 0x0c; // 开启AP
    public static final int EX_START_STA = 0x0d; // 开启STA

    public static final int WIFI_MODE_STA = 1;// sta模式
    public static final int WIFI_MODE_AP = 2;// AP模式
}
