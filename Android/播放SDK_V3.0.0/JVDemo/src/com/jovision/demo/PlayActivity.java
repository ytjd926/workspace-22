
package com.jovision.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jovision.Consts;
import com.jovision.Jni;
import com.jovision.beans.Channel;
import com.jovision.beans.Device;
import com.jovision.tools.AppConsts;
import com.jovision.tools.JVNetConst;
import com.jovision.tools.MyAudio;
import com.jovision.tools.PlayUtil;
import com.jovision.tools.WifiAdmin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayActivity extends BaseActivity {

    private static final String TAG = "PlayActivity";
    private static final int DISMISS_DIALOG = 0x01;

    private SurfaceView playSurface;// 视频播放view
    private TextView linkState;// 连接状态
    private SurfaceHolder holder;
    private Button checkRemote;// 检索远程回放列表
    private Button audioListening;// 音频监听功能
    private Button voiceCall, record, changeStream, wifiSet, throughAgreementBtn, osdFunc,
            modifypass, netInfo, changeMode;// 单向对讲功能，录像，切换码流，无线配置,透传协议，隐藏显示osd，修改用户密码，网络信息,切换到Ap、STA模式
    private Channel connectChannel;
    private boolean apConnect = false;// 是否Ap直连设备
    private int wifiMode = -1;// 当前是Ap还是Sta模式

    /** 云台操作 */
    private LinearLayout ytLayout;
    protected ImageView autoimage, zoomIn, zoomout, scaleSmallImage,
            scaleAddImage, upArrow, downArrow, leftArrow, rightArrow,
            yt_cancle;

    /**** 对讲用 ******/
    public static MyAudio playAudio;
    protected RelativeLayout voiceTip;// 单向对讲提示
    protected boolean realStop = false;
    public static boolean AUDIO_SINGLE = false;// 单向对讲标志
    public static boolean VOICECALL_LONG_CLICK = false;// 语音喊话flag长按状态,长按发送数据
    public static boolean VOICECALLING = false;// 对讲功能已经开启
    public static boolean GATHER_AUDIO_DATA = true;// 是否采集音频数据
    public static boolean showingOsd = true;// 是否显示着osd true显示 false没显示

    /**** 码流切换 ******/
    private PopupWindow streamPopWindow;
    private Button hdBtn;
    private Button sdBtn;
    private Button fluentBtn;

    /**** 无线配置 ******/
    private LinearLayout wifiSetLayout;
    private EditText wifiName;
    private EditText wifiPass;
    private Button saveSet;
    private WifiAdmin wifiAdmin;

    /**
     * 修改密码输入框
     */
    private int power;
    public String descript = "";
    public String params;

    private EditText userET;
    private EditText pswET;

    /**
     * 视频连接
     * 
     * @param device
     * @param channel
     * @param surface
     * @return
     */
    private int connect(Device device, Channel channel, Surface surface) {
        int result = -1;
        if (null != device && null != channel) {
            if ("".equalsIgnoreCase(device.getIp()) || 0 == device.getPort()) {// 无ip和端口走云视通连接
                result = Jni.connect(channel.getIndex(), channel.getChannel(),
                        device.getIp(), device.getPort(), device.getUser(),
                        device.getPwd(), device.getNo(), device.getGid(), true,
                        1, true, JVNetConst.TYPE_3GMO_UDP, surface, false,
                        false, apConnect, false, null);
            } else {// 有Ip用ip连接，云视通号字段需要传-1，否则仍然走的云视通连接
                result = Jni.connect(channel.getIndex(), channel.getChannel(),
                        device.getIp(), device.getPort(), device.getUser(),
                        device.getPwd(), -1, device.getGid(), true, 1, true,
                        JVNetConst.TYPE_3GMO_UDP, surface, false, false,
                        apConnect, false, null);
            }
        }

        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {
        handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
    }

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {
        switch (what) {
            case Consts.PLAY_FINISH: {
                finish();
                break;
            }
            case DISMISS_DIALOG: {
                linkState.setVisibility(View.GONE);
                dismissDialog();
                break;
            }
            case AppConsts.CALL_NORMAL_DATA: {// O 帧 视频数据回调
                connectChannel.setConnected(true);
                connectChannel.setPaused(false);
                // handler.sendEmptyMessage(DISMISS_DIALOG);
                try {
                    JSONObject jobj;
                    jobj = new JSONObject(obj.toString());
                    int type = jobj.optInt("device_type");
                    if (null != jobj) {
                        connectChannel.getParent().setType(type);
                        connectChannel.getParent()
                                .setJFH(jobj.optBoolean("is_jfh"));
                        connectChannel.getParent().set05(jobj.optBoolean("is05"));
                        connectChannel.setAudioType(jobj.getInt("audio_type"));
                        connectChannel.setAudioByte(jobj.getInt("audio_bit"));
                        connectChannel.setAudioEncType(jobj
                                .getInt("audio_enc_type"));
                        connectChannel.setSingleVoice(true);
                        if (8 == connectChannel.getAudioByte()
                                && AppConsts.DEVICE_TYPE_DVR == type) {
                            connectChannel.setSupportVoice(false);
                        } else {
                            connectChannel.setSupportVoice(true);
                        }

                        // 请求文本聊天
                        Jni.sendBytes(arg1, JVNetConst.JVN_REQ_TEXT, new byte[0], 8);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            }

            case Consts.CALL_NEW_PICTURE: {// I帧
                linkState.setVisibility(View.GONE);
                break;
            }

            case Consts.CALL_TEXT_DATA: {
                Log.i(TAG, "CALL_TEXT_DATA: " + what + ", " + arg1 + ", " + arg2
                        + ", " + obj);

                switch (arg2) {
                    case JVNetConst.JVN_RSP_TEXTACCEPT:// 同意文本聊天
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        // 获取主控码流信息请求
                        Jni.sendTextData(arg1, JVNetConst.JVN_RSP_TEXTDATA, 8,
                                JVNetConst.JVN_STREAM_INFO);

                        // 2015-4-22 获取设备用户名密码
                        Jni.sendSuperBytes(arg1, JVNetConst.JVN_RSP_TEXTDATA, true,
                                Consts.RC_EX_ACCOUNT, Consts.EX_ACCOUNT_REFRESH,
                                Consts.POWER_ADMIN, 0, 0, new byte[0], 0);
                        connectChannel.setAgreeTextData(true);
                        break;
                    case JVNetConst.JVN_CMD_TEXTSTOP:// 不同意文本聊天
                        connectChannel.setAgreeTextData(false);
                        break;

                    case JVNetConst.JVN_RSP_TEXTDATA:// 文本数据
                        String allStr = obj.toString();
                        Log.v(TAG, "文本数据--" + allStr);
                        try {
                            JSONObject dataObj = new JSONObject(allStr);

                            switch (dataObj.getInt("flag")) {
                            // 远程配置请求，获取到配置文本数据
                                case JVNetConst.JVN_GET_USERINFO: {// 20
                                    int extend_type = dataObj.getInt("extend_type");
                                    if (Consts.EX_ACCOUNT_REFRESH == extend_type) {
                                        // CALL_TEXT_DATA: 165, 0, 81,
                                        // {"extend_arg1":64,"extend_arg2":0,"extend_arg3":0,
                                        // "extend_msg":
                                        // "+ID=admin;POWER=4;DESCRIPT=新帐户;+ID=abc;POWER=4;DESCRIPT=新帐户;",
                                        // "extend_type":3,"flag":20,"packet_count":4,"packet_id":0,"packet_length":0,"packet_type":6}

                                        // CALL_TEXT_DATA: 165, 0, 81,
                                        // {"extend_arg1":54,"extend_arg2":0,"extend_arg3":0,"extend_msg":"ID=admin;POWER=4;DESCRIPT=;ID=guest;POWER=1;DESCRIPT=;",
                                        // "extend_type":6,"flag":20,"packet_count":4,
                                        // "packet_id":0,"packet_length":0,"packet_type":6}

                                        String InfoJSON = dataObj.getString("extend_msg");
                                        InfoJSON = InfoJSON.replaceAll("ID", "+ID");
                                        String[] array = InfoJSON.split("\\+");
                                        for (int i = 1; i < array.length; i++) {
                                            if (null != array[i] && !array[i].equals("")) {
                                                HashMap<String, String> idomap = new HashMap<String, String>();
                                                idomap = genMsgMap(array[i]);
                                                int getPower = Integer.parseInt(idomap
                                                        .get("POWER"));
                                                String getDescript = idomap.get("DESCRIPT");
                                                if (idomap.get("ID").equals("admin")
                                                        && 4 == (0x04 & getPower)) {
                                                    Log.e("power-", "" + power);
                                                    power = getPower;
                                                    if (null == getDescript
                                                            || "".equals(getDescript)) {
                                                        descript = "";
                                                    } else {
                                                        descript = getDescript;
                                                    }

                                                }
                                            }
                                        }

                                    } else if (Consts.EX_ACCOUNT_MODIFY == extend_type) {// 修改成功
                                        showTextToast(R.string.modifypass_success);
                                    }
                                    break;
                                }
                                // case JVNetConst.JVN_GET_USERINFO:
                                // showTextToast("设置成功");
                                // break;
                                case JVNetConst.JVN_REQ_CALLBACK: {// 0
                                    // CALL_TEXT_DATA: 165, 0, 81,
                                    // {"extend_arg1":0,"extend_arg2":0,"extend_arg3":9,"extend_type":39,"flag":0,"msg":"abc123456","packet_count":18,"packet_id":0,"packet_length":0,"packet_type":6}
                                    int extendType = dataObj.getInt("extend_type");
                                    if (extendType == Consts.EX_COMTRANS_RESV) {// 透传回调,串口收的回调
                                        String msg = dataObj.getString("msg");
                                        int msgLength = dataObj.getInt("extend_arg3");
                                        Log.v(TAG, "透传回调数据--" + msg + "；透传回调长度--" + msgLength);
                                    } else if (extendType == Consts.EX_START_AP) {// 开启AP请求回调
                                        int nParam1 = dataObj.getInt("extend_arg1");
                                        Log.v(TAG, "nParam1=" + nParam1);
                                        // （2）手机端接受设备端回应，解析pstExt->nParam1的值：
                                        // 若pstExt->nParam1=-1，则不支持wifi；
                                        // 若pstExt->nParam1=1，说明AP模式已经开启；
                                        // 若pstExt->nParam1=0或pstExt->nParam1=2，则可以开启AP。
                                        switch (nParam1) {
                                            case -1:
                                                showTextToast("不支持wifi");
                                                break;
                                            case 1:
                                                showTextToast("AP模式已经开启");
                                                break;
                                            case 0:
                                            case 2:
                                                showTextToast("可以开启AP");
                                                // 发命令开启Ap
                                                Jni.sendSuperBytes(connectChannel.getIndex(),
                                                        JVNetConst.JVN_RSP_TEXTDATA,
                                                        true, Consts.RC_EX_NETWORK,
                                                        Consts.EX_START_AP, 1, 0, 0, null, 0);
                                                handler.sendMessageDelayed(
                                                        handler.obtainMessage(Consts.PLAY_FINISH),
                                                        1000);
                                                break;
                                        }

                                    } else if (extendType == Consts.EX_START_STA) {// 开启STA请求的回调
                                        int nParam1 = dataObj.getInt("extend_arg1");
                                        Log.v(TAG, "nParam1=" + nParam1);
                                        // （2）手机端接受设备端回应，解析pstExt->nParam1的值：
                                        // 若pstExt->nParam1=-1，则不支持wifi；
                                        // 若pstExt->nParam1=0，说明目前尚未配置wifi，无法开启STA（开启了也没用）；
                                        // 若pstExt->nParam1=1，说明STA模式已经开启；
                                        // 若pstExt->nParam1=2，则可以开启STA。
                                        switch (nParam1) {
                                            case -1:
                                                showTextToast("不支持wifi");
                                                break;
                                            case 0:
                                                showTextToast("目前尚未配置wifi，无法开启STA（开启了也没用）");
                                                break;
                                            case 1:
                                                showTextToast("STA模式已经开启");
                                                break;
                                            case 2:
                                                showTextToast("可以开启STA");
                                                // 发命令开启Ap
                                                Jni.sendSuperBytes(connectChannel.getIndex(),
                                                        JVNetConst.JVN_RSP_TEXTDATA,
                                                        true, Consts.RC_EX_NETWORK,
                                                        Consts.EX_START_STA, 1, 0, 0, null, 0);
                                                handler.sendMessageDelayed(
                                                        handler.obtainMessage(Consts.PLAY_FINISH),
                                                        1000);
                                                break;
                                        }

                                    }
                                    break;
                                }
                                case JVNetConst.JVN_REMOTE_SETTING: {// 1--

                                    break;
                                }
                                case JVNetConst.JVN_WIFI_INFO:// 2-- AP,WIFI热点请求
                                    break;
                                case JVNetConst.JVN_STREAM_INFO:// 3-- 码流配置请求
                                    Log.i(TAG, "JVN_STREAM_INFO:TEXT_DATA: " + what + ", "
                                            + arg1 + ", " + arg2 + ", " + obj);
                                    String streamJSON = dataObj.getString("msg");
                                    HashMap<String, String> streamMap = genMsgMap(streamJSON);

                                    if (null != streamMap.get("MobileQuality")
                                            && !"".equalsIgnoreCase(streamMap
                                                    .get("MobileQuality"))) {
                                        Log.v(TAG,
                                                "MobileQuality="
                                                        + streamMap.get("MobileQuality"));
                                        int mobileQuality = Integer.parseInt(streamMap
                                                .get("MobileQuality"));
                                        if (1 == mobileQuality) {
                                            hdBtn.setTextColor(Color.BLUE);
                                            sdBtn.setTextColor(Color.BLACK);
                                            fluentBtn.setTextColor(Color.BLACK);
                                        } else if (2 == mobileQuality) {
                                            hdBtn.setTextColor(Color.BLACK);
                                            sdBtn.setTextColor(Color.BLUE);
                                            fluentBtn.setTextColor(Color.BLACK);
                                        } else if (3 == mobileQuality) {
                                            hdBtn.setTextColor(Color.BLACK);
                                            sdBtn.setTextColor(Color.BLACK);
                                            fluentBtn.setTextColor(Color.BLUE);
                                        }

                                    }

                                    if (null != streamMap.get("nPosition")
                                            && "4".equalsIgnoreCase(streamMap
                                                    .get("nPosition"))
                                            && null != streamMap.get("nTimePosition")
                                            && "4".equalsIgnoreCase(streamMap
                                                    .get("nTimePosition"))) {

                                        showingOsd = false;
                                        osdFunc.setText(R.string.show_osd);
                                    } else {
                                        osdFunc.setText(R.string.dismiss_osd);
                                    }

                                    Jni.sendString(connectChannel.getIndex(),
                                            JVNetConst.JVN_RSP_TEXTDATA,
                                            true,
                                            Consts.RC_EX_NETWORK, Consts.EX_NW_REFRESH, null);

                                    break;
                                case Consts.EX_NW_REFRESH: {// 8 获取WiFi信息
                                    Log.i(TAG, "EX_NW_REFRESH:TEXT_DATA: " + what + ", "
                                            + arg1 + ", " + arg2 + ", " + obj);
                                    String wifiJSON = dataObj.getString("msg");
                                    HashMap<String, String> wifiMap = genMsgMap(wifiJSON);

                                    // {"extend_arg1":0,"extend_arg2":394,"extend_arg3":0,"extend_type":8,"flag":8,"msg":"bIPSelfAdapt=0;ACTIVED=2;bDHCP=1;ETH_IP=172.16.34.152;ETH_NM=255.255.255.0;ETH_GW=172.16.34.1;ETH_DNS=172.18.1.254;
                                    // ETH_MAC=ac:a2:13:55:1f:05;WIFI_IP=172.16.34.152;WIFI_NM=255.255.255.0;WIFI_GW=172.16.34.1;
                                    // WIFI_DNS=172.18.1.254;WIFI_MAC=ac:a2:13:55:1f:05;WIFI_ID=tp-wy;WIFI_PW=87654321;
                                    // WIFI_Q=0;WIFI_AUTH=4;WIFI_ENC=3;WIFI_ON=1;WIFI_MODE=1;YSTGROUP=66;YSTID=95064839;
                                    // YSTSTATUS=1;RTMP_bSupport=0;","packet_count":2,"packet_id":0,"packet_length":0,"packet_type":6}
                                    if (null != wifiMap.get("ACTIVED")
                                            && !"".equalsIgnoreCase(wifiMap
                                                    .get("ACTIVED"))) {
                                        int actived = Integer.parseInt(wifiMap
                                                .get("ACTIVED"));// 0 有线， 1
                                                                 // PPPOE， 2
                                                                 // wifi
                                        Log.v(TAG,
                                                "ACTIVED="
                                                        + actived);

                                    }

                                    if (null != wifiMap.get("WIFI_ID")
                                            && !"".equalsIgnoreCase(wifiMap
                                                    .get("WIFI_ID"))) {
                                        String wifiName = wifiMap
                                                .get("WIFI_ID");//
                                        Log.v(TAG,
                                                "WIFI_ID="
                                                        + wifiName);

                                    }

                                    if (null != wifiMap.get("WIFI_MODE")
                                            && !"".equalsIgnoreCase(wifiMap
                                                    .get("WIFI_MODE"))) {
                                        Log.v(TAG,
                                                "WIFI_MODE="
                                                        + wifiMap.get("WIFI_MODE"));
                                        wifiMode = Integer.parseInt(wifiMap
                                                .get("WIFI_MODE"));// 1:STA,2:AP

                                        if (Consts.WIFI_MODE_AP == wifiMode) {// 当前AP状态
                                            changeMode.setText(R.string.changetosta);
                                        } else if (Consts.WIFI_MODE_STA == wifiMode) {// 当前sta状态
                                            changeMode.setText(R.string.changetoap);
                                        }
                                    }
                                    break;
                                }
                                case JVNetConst.EX_WIFI_AP_CONFIG:// 11
                                    // ---新wifi配置流程
                                    if (0 == dataObj.getInt("result")) {// 配置成功
                                        showTextToast(R.string.set_success);
                                    } else {// 配置失败
                                        showTextToast(R.string.set_failed);
                                    }

                                    break;
                                default:
                                    break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }
                break;
            }
            case AppConsts.STOP_AUDIO_GATHER: {// 停止采集音频数据
                GATHER_AUDIO_DATA = false;
                break;
            }
            case AppConsts.START_AUDIO_GATHER: {// 开始采集音频数据
                GATHER_AUDIO_DATA = true;
                break;
            }
            case AppConsts.CALL_PLAY_AUDIO: {// 音频数据
                if (!GATHER_AUDIO_DATA) {
                    break;
                }

                if (null != obj && null != playAudio) {
                    if (AUDIO_SINGLE) {// 单向对讲长按才发送语音数据
                        if (VOICECALL_LONG_CLICK) {
                            // 长按时只发送语音，不接收语音
                        } else {
                            byte[] data = (byte[]) obj;
                            // audioQueue.offer(data);
                            // [Neo] 将音频填入缓存队列
                            playAudio.put(data);
                        }
                    } else {// 双向对讲直接播放设备传过来的语音
                        byte[] data = (byte[]) obj;
                        // audioQueue.offer(data);
                        // [Neo] 将音频填入缓存队列
                        playAudio.put(data);
                    }
                }

                break;
            }

            case Consts.CALL_CHAT_DATA: {
                dismissDialog();
                Log.i(TAG, "CALL_CHAT_DATA:arg1=" + arg1 + ",arg2=" + arg2);
                switch (arg2) {
                // 语音数据
                    case JVNetConst.JVN_RSP_CHATDATA: {
                        Log.i(TAG, "JVN_RSP_CHATDATA");
                        break;
                    }

                    // 同意语音请求
                    case JVNetConst.JVN_RSP_CHATACCEPT: {
                        if (connectChannel.isSingleVoice()) {
                            voiceCall.setText(R.string.voice_send);
                            showTextToast(R.string.voice_tips2);
                        }
                        // recorder.start(channelList.get(lastClickIndex).getAudioType(),
                        // channelList.get(lastClickIndex).getAudioByte());

                        if (AppConsts.JAE_ENCODER_G729 == connectChannel
                                .getAudioEncType()) {
                            // 开启语音对讲
                            playAudio.startPlay(16, true);
                            playAudio.startRec(connectChannel.getIndex(),
                                    connectChannel.getAudioEncType(), 16,
                                    connectChannel.getAudioBlock(), true);
                        } else {
                            // 开启语音对讲
                            playAudio.startPlay(connectChannel.getAudioByte(), true);
                            playAudio.startRec(connectChannel.getIndex(),
                                    connectChannel.getAudioEncType(),
                                    connectChannel.getAudioByte(),
                                    connectChannel.getAudioBlock(), true);

                        }
                        connectChannel.setVoiceCall(true);
                        VOICECALLING = true;

                        break;
                    }

                    // 暂停语音聊天
                    case JVNetConst.JVN_CMD_CHATSTOP: {
                        if (realStop) {
                            realStop = false;
                        } else {
                            showTextToast(R.string.has_calling);
                        }

                        break;
                    }
                }
                break;
            }

            case AppConsts.CALL_CONNECT_CHANGE: {// 连接结果
                switch (arg2) {
                // 1 -- 连接成功
                    case JVNetConst.CONNECT_OK: {
                        connectChannel.setConnected(true);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.GONE);

                        // 视频连接成功，如果连接的用户名密码是默认的建议用户修改管理员密码
                        if (AppConsts.DEFAULT_USER.equalsIgnoreCase(MainActivity.device.getUser())
                                && AppConsts.DEFAULT_PWD.equalsIgnoreCase(MainActivity.device
                                        .getPwd())) {
                            modifyDialog(R.string.edit_user_pass_tips1);
                        } else {
                            linkState.setVisibility(View.VISIBLE);
                            linkState.setText(R.string.buffering);
                        }

                        // TODO
                        // Jni.enablePlayAudio(connectChannel.getIndex(),
                        // true);//
                        // 指定某个窗口是否要播声音，如果想播声音传true，不播声音传false，如果录像想录声音，一定要传true
                        break;
                    }
                    // 2 -- 断开连接成功
                    case JVNetConst.DISCONNECT_OK: {
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        JSONObject connectObj;
                        try {
                            connectObj = new JSONObject(obj.toString());
                            String errorMsg = connectObj.getString("msg");
                            linkState.setVisibility(View.VISIBLE);
                            linkState.setText(errorMsg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                    // 4 -- 连接失败
                    case JVNetConst.CONNECT_FAILED: {
                        try {
                            JSONObject connectObj = new JSONObject(obj.toString());
                            String errorMsg = connectObj.getString("msg");
                            if ("password is wrong!".equalsIgnoreCase(errorMsg)
                                    || "pass word is wrong!".equalsIgnoreCase(errorMsg)) {// 密码错误时提示身份验证失败
                                linkState.setText(getResources()
                                        .getString(R.string.connfailed_auth));

                            } else if ("channel is not open!"
                                    .equalsIgnoreCase(errorMsg)) {// 无该通道服务
                                linkState.setText(getResources().getString(
                                        R.string.connfailed_channel_notopen));
                            } else if ("connect type invalid!"
                                    .equalsIgnoreCase(errorMsg)) {// 连接类型无效
                                linkState.setText(getResources().getString(
                                        R.string.connfailed_type_invalid));
                            } else if ("client count limit!".equalsIgnoreCase(errorMsg)) {// 超过主控最大连接限制
                                linkState.setText(getResources().getString(
                                        R.string.connfailed_maxcount));
                            } else if ("connect timeout!".equalsIgnoreCase(errorMsg)) {//
                                linkState.setText(getResources().getString(
                                        R.string.connfailed_timeout));
                            } else if ("check password timeout!"
                                    .equalsIgnoreCase(errorMsg)) {// 验证密码超时
                                linkState.setText(getResources().getString(
                                        R.string.connfailed_checkpass_timout));
                            } else {// "Connect failed!"
                                linkState
                                        .setText(getResources().getString(R.string.connect_failed));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            linkState.setText(getResources().getString(R.string.closed));
                        }
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.VISIBLE);

                        break;
                    }
                    // 6 -- 连接异常断开
                    case JVNetConst.ABNORMAL_DISCONNECT: {
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.VISIBLE);
                        linkState.setText(getResources().getString(R.string.abnormal_closed1));

                        break;
                    }
                    // 7 -- 服务停止连接，连接断开
                    case JVNetConst.SERVICE_STOP: {
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.VISIBLE);
                        linkState.setText(getResources().getString(R.string.abnormal_closed2));
                        break;
                    }
                    // 3 -- 不必要重复连接
                    case JVNetConst.NO_RECONNECT: {
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.VISIBLE);
                        linkState.setText(getResources().getString(R.string.abnormal_closed3));
                        break;
                    }
                    // 5 -- 没有连接
                    case JVNetConst.NO_CONNECT: {
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.VISIBLE);
                        linkState.setText(getResources().getString(R.string.abnormal_closed4));
                        break;
                    }
                    // 8 -- 断开连接失败
                    case JVNetConst.DISCONNECT_FAILED: {
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.VISIBLE);
                        linkState.setText(getResources().getString(R.string.abnormal_closed5));
                        break;
                    }

                    // 9 -- 其他错误
                    case JVNetConst.OHTER_ERROR: {
                        connectChannel.setConnected(false);
                        connectChannel.setPaused(false);
                        linkState.setVisibility(View.VISIBLE);
                        linkState.setText("其他错误");
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void initSettings() {
        playAudio = MyAudio.getIntance(AppConsts.PLAY_AUDIO_WHAT,
                PlayActivity.this, 8000);
        apConnect = getIntent().getBooleanExtra("APConnect", false);
        wifiAdmin = new WifiAdmin(PlayActivity.this);
    }

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_play);
        playSurface = (SurfaceView) findViewById(R.id.playsurface);
        linkState = (TextView) findViewById(R.id.linkstate);
        linkState.setTextColor(Color.GREEN);
        audioListening = (Button) findViewById(R.id.audiolistener);
        voiceCall = (Button) findViewById(R.id.voicecall);
        record = (Button) findViewById(R.id.record);
        changeStream = (Button) findViewById(R.id.changestream);
        checkRemote = (Button) findViewById(R.id.checkremote);
        wifiSet = (Button) findViewById(R.id.wifiset);
        wifiSetLayout = (LinearLayout) findViewById(R.id.wifisetlayout);
        wifiName = (EditText) wifiSetLayout.findViewById(R.id.wifiname);
        wifiPass = (EditText) wifiSetLayout.findViewById(R.id.wifipass);
        saveSet = (Button) wifiSetLayout.findViewById(R.id.saveset);
        throughAgreementBtn = (Button) findViewById(R.id.through_agreement);
        osdFunc = (Button) findViewById(R.id.osd_visibility);
        modifypass = (Button) findViewById(R.id.modifypass);
        changeMode = (Button) findViewById(R.id.changemode);
        netInfo = (Button) findViewById(R.id.netinfo);
        saveSet.setOnClickListener(myOnClickListener);// 配置
        throughAgreementBtn.setOnClickListener(myOnClickListener);
        osdFunc.setOnClickListener(myOnClickListener);
        modifypass.setOnClickListener(myOnClickListener);
        changeMode.setOnClickListener(myOnClickListener);
        netInfo.setOnClickListener(myOnClickListener);
        ytLayout = (LinearLayout) findViewById(R.id.ytlayout);
        autoimage = (ImageView) findViewById(R.id.autoimage);
        zoomIn = (ImageView) findViewById(R.id.zoomin);
        zoomout = (ImageView) findViewById(R.id.zoomout);
        scaleSmallImage = (ImageView) findViewById(R.id.scaleSmallImage);
        scaleAddImage = (ImageView) findViewById(R.id.scaleAddImage);
        upArrow = (ImageView) findViewById(R.id.upArrow);
        downArrow = (ImageView) findViewById(R.id.downArrow);
        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);

        autoimage.setOnClickListener(imageOnClickListener);
        zoomIn.setOnClickListener(imageOnClickListener);
        zoomout.setOnClickListener(imageOnClickListener);
        scaleSmallImage.setOnClickListener(imageOnClickListener);
        scaleAddImage.setOnClickListener(imageOnClickListener);
        upArrow.setOnClickListener(imageOnClickListener);
        downArrow.setOnClickListener(imageOnClickListener);
        leftArrow.setOnClickListener(imageOnClickListener);
        rightArrow.setOnClickListener(imageOnClickListener);

        autoimage.setOnTouchListener(new LongClickListener());
        zoomIn.setOnTouchListener(new LongClickListener());
        zoomout.setOnTouchListener(new LongClickListener());
        scaleSmallImage.setOnTouchListener(new LongClickListener());
        scaleAddImage.setOnTouchListener(new LongClickListener());
        upArrow.setOnTouchListener(new LongClickListener());
        downArrow.setOnTouchListener(new LongClickListener());
        leftArrow.setOnTouchListener(new LongClickListener());
        rightArrow.setOnTouchListener(new LongClickListener());

        voiceTip = (RelativeLayout) findViewById(R.id.voicetip);
        audioListening.setOnClickListener(myOnClickListener);
        voiceCall.setOnClickListener(myOnClickListener);
        record.setOnClickListener(myOnClickListener);
        changeStream.setOnClickListener(myOnClickListener);
        checkRemote.setOnClickListener(myOnClickListener);
        wifiSet.setOnClickListener(myOnClickListener);

        holder = playSurface.getHolder();
        initStreamPopWindowView();
        hdBtn.setOnClickListener(myOnClickListener);
        sdBtn.setOnClickListener(myOnClickListener);
        fluentBtn.setOnClickListener(myOnClickListener);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                linkState.setVisibility(View.VISIBLE);
                linkState.setText(R.string.connectting);

                connectChannel = MainActivity.device.getChannelList().get(
                        MainActivity.channelIndex);
                if (!connectChannel.isConnected()) {
                    connectChannel.setParent(MainActivity.device);
                    connect(MainActivity.device, connectChannel,
                            holder.getSurface());
                } else if (connectChannel.isConnected()
                        && connectChannel.isPaused()) {
                    boolean result = Jni.sendBytes(connectChannel.getIndex(),
                            JVNetConst.JVN_CMD_VIDEO, new byte[0], 8);
                    connectChannel.setPaused(false);
                    Log.v(TAG, "onResume=" + result);

                    if (result) {
                        boolean resumeRes = Jni.resume(
                                connectChannel.getIndex(), holder.getSurface());
                        Log.v(TAG, "JNI-Play-Resume=" + resumeRes);
                        if (resumeRes) {
                            linkState.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
            }
        });

        voiceCall.setOnClickListener(myOnClickListener);
        voiceCall.setOnTouchListener(callOnTouchListener);
        voiceCall.setOnLongClickListener(callOnLongClickListener);

    }

    /**
     * 开始远程回放
     */
    public void startRemote() {
        if (null != connectChannel && connectChannel.isConnected()) {
            Intent remoteIntent = new Intent();
            remoteIntent.setClass(PlayActivity.this, RemoteListActivity.class);
            remoteIntent.putExtra("IndexOfChannel", connectChannel.getIndex());
            remoteIntent.putExtra("ChannelOfChannel",
                    connectChannel.getChannel());
            remoteIntent.putExtra("is05", connectChannel.getParent().is05());
            remoteIntent.putExtra("DeviceType", connectChannel.getParent()
                    .getType());
            remoteIntent.putExtra("isJFH", connectChannel.getParent().isJFH());
            PlayActivity.this.startActivity(remoteIntent);
        } else {
            Toast.makeText(PlayActivity.this, R.string.not_connected,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void saveSettings() {

    }

    @Override
    protected void freeMe() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (null != connectChannel && connectChannel.isConnected()
                && !connectChannel.isPaused()) {
            connectChannel.setPaused(true);
            boolean result = Jni.sendBytes(connectChannel.getIndex(),
                    JVNetConst.JVN_CMD_VIDEOPAUSE, new byte[0], 8);
            Log.v(TAG, "onPause=" + result);
        }
        boolean pauseRes = Jni.pause(connectChannel.getIndex());
        Log.v(TAG, "pauseRes=" + pauseRes);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        createDialog("", false);
        Thread thread = new Thread() {

            @Override
            public void run() {
                Jni.disconnect(MainActivity.channelIndex);
                handler.sendEmptyMessage(DISMISS_DIALOG);
                super.run();
            }

        };
        thread.start();
        super.onBackPressed();
    }

    /**
     * 云台按钮事件
     */
    ImageView.OnClickListener imageOnClickListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View arg0) {
        }
    };

    /**
     * 长按--云台事件
     * 
     * @author Administrator
     */
    class LongClickListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            int cmd = 0;

            switch (v.getId()) {
                case R.id.upArrow: // up
                    cmd = JVNetConst.JVN_YTCTRL_U;
                    break;
                case R.id.downArrow: // down
                    cmd = JVNetConst.JVN_YTCTRL_D;
                    break;
                case R.id.leftArrow: // left
                    cmd = JVNetConst.JVN_YTCTRL_L;
                    break;
                case R.id.rightArrow:// right
                    cmd = JVNetConst.JVN_YTCTRL_R;
                    break;
                case R.id.autoimage: // auto
                    if (action == MotionEvent.ACTION_DOWN) {
                        if (connectChannel.isAuto()) {// 已经开启自动巡航，发送关闭命令
                            cmd = JVNetConst.JVN_YTCTRL_AT;
                            connectChannel.setAuto(false);
                        } else {// 发开始命令
                            cmd = JVNetConst.JVN_YTCTRL_A;
                            connectChannel.setAuto(true);
                        }
                    }
                    break;
                case R.id.zoomout: // bb+
                    cmd = JVNetConst.JVN_YTCTRL_BBD;
                    break;
                case R.id.zoomin: // bb-
                    cmd = JVNetConst.JVN_YTCTRL_BBX;
                    break;
                case R.id.scaleAddImage: // bj+
                    cmd = JVNetConst.JVN_YTCTRL_BJD;
                    break;
                case R.id.scaleSmallImage: // bj-
                    cmd = JVNetConst.JVN_YTCTRL_BJX;
                    break;
            }
            try {
                if (action == MotionEvent.ACTION_DOWN) {
                    if (connectChannel != null && connectChannel.isConnected()) {
                        PlayUtil.sendCtrlCMDLongPush(connectChannel.getIndex(),
                                cmd, true);
                    }
                } else if (action == MotionEvent.ACTION_UP) {

                    if (connectChannel != null && connectChannel.isConnected()) {
                        PlayUtil.sendCtrlCMDLongPush(connectChannel.getIndex(),
                                cmd, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    private OnClickListener myOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            boolean isLisening = PlayUtil
                    .isPlayAudio(connectChannel.getIndex());
            switch (view.getId()) {
                case R.id.netinfo: {// 网络信息
                    Jni.sendString(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA, true,
                            Consts.RC_EX_NETWORK, Consts.EX_NW_REFRESH, null);
                    break;
                }
                case R.id.changemode: {// 切换到Ap
                    if (Consts.WIFI_MODE_AP == wifiMode) {// 当前AP状态，切换到sta模式
                        // 开启sta请求，看回调结果
                        Jni.sendSuperBytes(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA,
                                true, Consts.RC_EX_NETWORK, Consts.EX_START_STA, 0, 0, 0, null, 0);
                        changeMode.setText(R.string.changetoap);
                    } else if (Consts.WIFI_MODE_STA == wifiMode) {// 当前sta状态，切换到Ap模式
                        // 开启AP请求，看回调结果
                        Jni.sendSuperBytes(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA,
                                true, Consts.RC_EX_NETWORK, Consts.EX_START_AP, 0, 0, 0, null, 0);
                        changeMode.setText(R.string.changetosta);
                    }

                    break;
                }
                case R.id.modifypass:// 修改密码
                    // TODO
                    if (connectChannel.isConnected()) {
                        // 视频连接成功后才可以修改设备的用户名密码
                        modifyDialog(R.string.edit_user_pass_tips2);
                    }
                    break;
                case R.id.osd_visibility: {

                    if (showingOsd) {
                        showingOsd = false;
                        osdFunc.setText(R.string.show_osd);
                        // 隐藏OSD
                        String dismiss = "[ALL];nPosition=4;nTimePosition=4;";

                        Jni.sendSuperBytes(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA,
                                false, dismiss.getBytes().length, JVNetConst.RC_SETPARAM, 0, 0, 0,
                                dismiss.getBytes(), dismiss.getBytes().length);

                    } else {
                        showingOsd = true;
                        osdFunc.setText(R.string.dismiss_osd);
                        // 显示OSD,1,2 的值代表
                        String show = "[ALL];nPosition=1;nTimePosition=2;";

                        Jni.sendSuperBytes(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA,
                                false, show.getBytes().length, JVNetConst.RC_SETPARAM, 0, 0, 0,
                                show.getBytes(), show.getBytes().length);

                    }

                    break;
                }
                case R.id.audiolistener: {// 音频监听
                    if (connectChannel.isVoiceCall()) {// 对讲时不允许开监听
                        break;
                    }
                    if (isLisening) {// 正在监听
                        audioListening.setText(R.string.audiolistener);
                        stopAudio(connectChannel.getIndex());
                    } else {
                        audioListening.setText(R.string.audiolistening);
                        startAudio(connectChannel.getIndex(),
                                connectChannel.getAudioByte());
                    }
                    break;
                }
                case R.id.voicecall: {// 语音对讲
                    if (isLisening) {// 正在监听,先停止监听
                        audioListening.setText(R.string.audiolistener);
                        stopAudio(connectChannel.getIndex());
                    }

                    if (AppConsts.JAE_ENCODER_G729 != connectChannel
                            .getAudioEncType()
                            && 8 == connectChannel.getAudioByte()) {
                        showTextToast("不支持此设备");
                        return;
                    } else {
                        if (connectChannel.isVoiceCall()) {
                            voiceCall.setText(R.string.voicecall);
                            stopVoiceCall(connectChannel.getIndex());
                            connectChannel.setVoiceCall(false);
                            realStop = true;
                            VOICECALLING = false;
                        } else {
                            PlayActivity.AUDIO_SINGLE = connectChannel
                                    .isSingleVoice();
                            createDialog("", true);
                            startVoiceCall(connectChannel.getIndex(),
                                    connectChannel);
                        }
                    }

                    break;
                }
                case R.id.record: {// 录像
                    String savePath = Environment.getExternalStorageDirectory()
                            .getPath() + File.separator + "sdkvideo";

                    File saveFile = new File(savePath);
                    if (!saveFile.exists()) {
                        saveFile.mkdir();
                    }

                    if (Jni.checkRecord(connectChannel.getIndex())) {// 正在录像
                        Jni.stopRecord();
                        showTextToast(getResources().getString(
                                R.string.record_savedas)
                                + savePath);
                        record.setText(R.string.record);
                    } else {
                        Jni.startRecord(connectChannel.getIndex(), savePath
                                + File.separator + System.currentTimeMillis()
                                + ".mp4", true, true);
                        record.setText(R.string.recording);
                    }
                    break;
                }
                case R.id.changestream: {// 切换码流
                    /** 如果录像的过程中切换码流，录像会自动停止 */
                    if (null == streamPopWindow) {
                        initStreamPopWindowView();
                        streamPopWindow.showAsDropDown(changeStream);
                    } else {
                        if (streamPopWindow.isShowing()) {
                            streamPopWindow.dismiss();
                        } else {
                            streamPopWindow.showAsDropDown(changeStream);
                        }

                    }

                    break;
                }
                case R.id.wifiset: {// 无线配置
                    if (View.VISIBLE == wifiSetLayout.getVisibility()) {
                        wifiSetLayout.setVisibility(View.GONE);
                        ytLayout.setVisibility(View.VISIBLE);
                    } else {
                        wifiSetLayout.setVisibility(View.VISIBLE);
                        ytLayout.setVisibility(View.GONE);
                    }
                    break;
                }
                case R.id.checkremote: {// 远程回放检索
                    startRemote();
                    break;
                }
                case R.id.through_agreement: {// 透传协议
                    String params = "ff 01 02 03 f0";
                    byte[] data = params.getBytes();
                    Jni.sendSuperBytes(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA,
                            true, Consts.RC_EX_COMTRANS, Consts.EX_COMTRANS_SEND, 0, 0,
                            data.length, data, data.length);// 前进
                    break;
                }
                case R.id.hd: {// 高清
                    Jni.sendString(connectChannel.getIndex(),
                            JVNetConst.JVN_RSP_TEXTDATA, false, 0,
                            Consts.TYPE_SET_PARAM, "MobileQuality=1;");
                    if (null != streamPopWindow && streamPopWindow.isShowing()) {
                        streamPopWindow.dismiss();
                    }
                    break;
                }

                case R.id.sd: {// 标清
                    Jni.sendString(connectChannel.getIndex(),
                            JVNetConst.JVN_RSP_TEXTDATA, false, 0,
                            Consts.TYPE_SET_PARAM, "MobileQuality=2;");
                    if (null != streamPopWindow && streamPopWindow.isShowing()) {
                        streamPopWindow.dismiss();
                    }
                    break;
                }

                case R.id.fluent: {// 流畅
                    Jni.sendString(connectChannel.getIndex(),
                            JVNetConst.JVN_RSP_TEXTDATA, false, 0,
                            Consts.TYPE_SET_PARAM, "MobileQuality=3;");
                    if (null != streamPopWindow && streamPopWindow.isShowing()) {
                        streamPopWindow.dismiss();
                    }
                    break;
                }
                case R.id.saveset: {// 配置
                    if ("".equalsIgnoreCase(wifiName.getText().toString().trim())) {
                        showTextToast(R.string.wifi_name_notnull);
                    } else if ("".equalsIgnoreCase(wifiPass.getText().toString()
                            .trim())) {
                        showTextToast(R.string.wifi_pass_notnull);
                    } else {
                        saveWifi();
                        if (View.VISIBLE == wifiSetLayout.getVisibility()) {
                            wifiSetLayout.setVisibility(View.GONE);
                            ytLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                }

            }
        }

    };

    /**
     * 修改密码的dialog
     */
    private void modifyDialog(int stringId) {

        View views = LayoutInflater.from(PlayActivity.this).inflate(
                R.layout.dialog_layout, null);
        userET = (EditText) views.findViewById(R.id.devname);
        userET.setEnabled(false);
        pswET = (EditText) views.findViewById(R.id.devpass);
        AlertDialog.Builder dialog = new AlertDialog.Builder(PlayActivity.this);
        dialog.setTitle(R.string.alert).setMessage(stringId).
                setView(views)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modifypass();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    /**
     * 2015-04-21 修改设备密码
     */
    public void modifypass() {
        if (0 == power) {
            return;
        }
        String userName = userET.getText().toString();
        String userPwd = pswET.getText().toString();
        String desUTF8 = descript;
        byte[] paramByte = new byte[Consts.SIZE_ID + Consts.SIZE_PW
                + Consts.SIZE_DESCRIPT];
        byte[] userNameByte = userName.getBytes();
        byte[] userPwdByte = userPwd.getBytes();
        byte[] desByte;
        try {
            desByte = desUTF8.getBytes("GBK");
            // 设备端是GBK编码
            System.arraycopy(userNameByte, 0, paramByte, 0,
                    userNameByte.length);
            System.arraycopy(userPwdByte, 0, paramByte, Consts.SIZE_ID,
                    userPwdByte.length);
            System.arraycopy(desByte, 0, paramByte, Consts.SIZE_ID
                    + Consts.SIZE_PW, desByte.length);

            // 2014-12-25 修改设备用户名密码
            // //CALL_TEXT_DATA: 165, 0, 81,
            // {"extend_arg1":58,"extend_arg2":0,"extend_arg3":0,"extend_type":6,"flag":0,"packet_count":4,"packet_id":0,"packet_length":0,"packet_type":6,"type":81}
            Jni.sendSuperBytes(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA, true,
                    Consts.RC_EX_ACCOUNT, Consts.EX_ACCOUNT_MODIFY, power,
                    0, 0, paramByte, paramByte.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存wifi配置信息
     */
    public void saveWifi() {
        JSONObject newObj = new JSONObject();
        try {
            newObj.put("wifiSsid", wifiName.getText().toString());
            newObj.put("wifiPwd", wifiPass.getText().toString());
            newObj.put("nPacketType", JVNetConst.RC_EXTEND);
            newObj.put("packetCount", JVNetConst.RC_EX_NETWORK);
            newObj.put("nType", JVNetConst.EX_WIFI_AP_CONFIG);
            int[] data = wifiAdmin
                    .getWifiAuthEnc(wifiName.getText().toString());
            newObj.put("wifiAuth", data[0]);
            newObj.put("wifiEncryp", data[1]);
            newObj.put("wifiIndex", 0);
            newObj.put("wifiChannel", 0);
            newObj.put("wifiRate", 0);//
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        Jni.setAccessPoint(connectChannel.getIndex(),
                JVNetConst.JVN_RSP_TEXTDATA, newObj.toString());
        Log.e("配置wifi请求", newObj.toString());
    }

    /**
     * 开始语音对讲
     */
    public static void startVoiceCall(int index, Channel channel) {
        Jni.sendBytes(index, JVNetConst.JVN_REQ_CHAT, new byte[0], 8);
    }

    /**
     * 停止语音对讲
     */
    public static void stopVoiceCall(int index) {
        Jni.sendBytes(index, JVNetConst.JVN_CMD_CHATSTOP, new byte[0], 8);
        // 关闭语音对讲
        playAudio.stopPlay();
        playAudio.stopRec();

    }

    /**
     * 单向对讲用功能
     */
    OnTouchListener callOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {

            if (connectChannel.isSingleVoice() && VOICECALLING) {// 单向对讲
                if (arg1.getAction() == MotionEvent.ACTION_UP
                        || arg1.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
                    handler.sendMessage(handler
                            .obtainMessage(AppConsts.STOP_AUDIO_GATHER));
                    new TalkThread(connectChannel.getIndex(), 0).start();
                    VOICECALL_LONG_CLICK = false;
                    voiceCall.setText(R.string.voice_send);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    voiceTip.setVisibility(View.GONE);
                    handler.sendMessageDelayed(
                            handler.obtainMessage(AppConsts.START_AUDIO_GATHER),
                            2 * 1000);
                }
            }
            return false;
        }

    };

    /**
     * 单向对讲用功能
     */
    OnLongClickListener callOnLongClickListener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View arg0) {
            startSendVoice();
            return true;
        }

    };

    /** 开关对讲线程 */
    class TalkThread extends Thread {
        private int index = 0;
        private int param = 0;

        TalkThread(int index, int param) {
            this.index = index;
            this.param = param;
        }

        @Override
        public void run() {
            // "talkSwitch=" + tag;// 1开始 0关闭
            for (int i = 0; i < 3; i++) {
                Jni.sendString(index, JVNetConst.JVN_RSP_TEXTDATA, false, 0,
                        AppConsts.TYPE_SET_PARAM,
                        String.format(AppConsts.FORMATTER_TALK_SWITCH, param));
            }
            super.run();
        }

    }

    /**
     * 长按发送语音数据
     */
    private void startSendVoice() {
        if (connectChannel.isSingleVoice() && VOICECALLING) {// 单向对讲且正在对讲
            VOICECALL_LONG_CLICK = true;
            voiceCall.setText(R.string.voice_stop);

            voiceTip.setVisibility(View.VISIBLE);
            new TalkThread(connectChannel.getIndex(), 1).start();
        }
    }

    /**
     * 码流切换dialog
     */
    public void initStreamPopWindowView() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.stream_list,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        streamPopWindow = new PopupWindow(customView, 350, 350);

        /** 在这里可以实现自定义视图的功能 */
        hdBtn = (Button) customView.findViewById(R.id.hd);
        sdBtn = (Button) customView.findViewById(R.id.sd);
        fluentBtn = (Button) customView.findViewById(R.id.fluent);
    }

    /**
     * 特定 json 转 HashMap
     * 
     * @param json
     * @param keyOfMsg 消息的键名
     * @return
     */
    public HashMap<String, String> genMsgMap(String msg) {
        HashMap<String, String> map = new HashMap<String, String>();

        if (null == msg || "".equalsIgnoreCase(msg)) {
            return null;
        }
        Matcher matcher = Pattern.compile("([^=;]+)=([^=;]+)").matcher(msg);
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    /**
     * 应用层开启音频监听功能
     * 
     * @param index
     * @return
     */
    public static boolean startAudio(int index, int audioByte) {
        boolean open = false;
        if (PlayUtil.isPlayAudio(index)) {// 正在监听,确保不会重复开启
            open = true;
        } else {
            PlayUtil.startAudioMonitor(index);// enable audio
            playAudio.startPlay(audioByte, true);
            open = true;
        }
        return open;
    }

    /**
     * 应用层关闭音频监听功能
     * 
     * @param index
     * @return
     */
    public static boolean stopAudio(int index) {
        boolean close = false;
        if (PlayUtil.isPlayAudio(index)) {// 正在监听，停止监听
            PlayUtil.stopAudioMonitor(index);// stop audio
            playAudio.stopPlay();
            close = true;
        } else {// 确保不会重复关闭
            close = true;
        }
        return close;
    }

    /**
     * 切换到Ap模式
     */
    public void changeToAp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PlayActivity.this);
        // 对话框的标题
        builder.setTitle(R.string.alert);
        // 显示的信息内容
        builder.setMessage(R.string.changetoap_tips);
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which) {
                        // 开启AP请求，看回调结果
                        Jni.sendSuperBytes(connectChannel.getIndex(), JVNetConst.JVN_RSP_TEXTDATA,
                                true, Consts.RC_EX_NETWORK, Consts.EX_START_AP, 0, 0, 0, null, 0);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
