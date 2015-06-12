
package com.jovision;

import com.jovision.tools.MainApplication;

/**
 * 所有与 NDK 交互的接口都在这儿
 * 
 * @author neo
 */
public class Jni {

    /**
     * 1.初始化，参考 {@link JVSUDT#JVC_InitSDK(int, Object)}
     * 
     * @param handle 回调句柄，要传 MainApplication 的实例对象哦，因为回调方式是：<br />
     *            {@link MainApplication#onJniNotify(int, int, int, Object)}
     * @param port 本地端口
     * @param path 日志路径
     * @return true：成功，false：失败
     */
    public static native boolean init(Object handle, int port, String path);

    /**
     * 2.卸载，参考 {@link JVSUDT#JVC_ReleaseSDK()}
     */
    public static native void deinit();

    /**
     * 3.（新增接口）获取底层库版本,为了防止库用错，建议将该版本号打印出来
     * 
     * @return json: {"jni":"xx","net":"xx"}
     */
    public static native String getVersion();

    /**
     * 4.启用底层日志打印，参考 {@link JVSUDT#JVC_EnableLog(boolean)}
     * 
     * @param enable
     */
    public static native void enableLog(boolean enable);

    /**
     * 5.（新增接口）删除底层保存的错误日志
     */
    public static native void deleteLog();

    /**
     * 6.新 连接，参考
     * {@link JVSUDT#JVC_Connect(int, int, String, int, String, String, int, String, boolean, int, boolean, int, Object)}
     * 
     * @param window 窗口索引，从 0 开始
     * @param channel 设备通道，从 1 开始
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param cloudSeeId
     * @param groupId
     * @param isLocalDetect
     * @param turnType
     * @param isPhone
     * @param connectType
     * @param surface
     * @param isVip
     * @param isTcp
     * @param isAp
     * @param isTryOmx
     * @param thumbName
     * @return 连接结果，成功时返回窗口索引，失败时返回原因值
     */
    public static native int connect(int window, int channel, String ip,
            int port, String username, String password, int cloudSeeId,
            String groupId, boolean isLocalDetect, int turnType,
            boolean isPhone, int connectType, Object surface, boolean isVip,
            boolean isTcp, boolean isAp, boolean isTryOmx, String thumbName);

    /**
     * 7.暂停底层显示
     * 
     * @param window 窗口索引
     * @return true：成功，false：失败
     */
    public static native boolean pause(int window);

    /**
     * 8.恢复底层显示
     * 
     * @param window 窗口索引
     * @param surface
     * @return true：成功，false：失败
     */
    public static native boolean resume(int window, Object surface);

    /**
     * 9.断开，参考 {@link JVSUDT#JVC_DisConnect(int)}
     * 
     * @param window 窗口索引
     * @return true：成功，false：失败
     */
    public static native boolean disconnect(int window);

    /**
     * 10.发送字节数据，参考 {@link JVSUDT#JVC_SendData(int, byte, byte[], int)}
     * 
     * @param window 窗口索引
     * @param uchType
     * @param data
     * @param size
     * @return true：成功，false：失败
     */
    public static native boolean sendBytes(int window, byte uchType,
            byte[] data, int size);

    /**
     * 11.发送整数数据（远程回放进度调节），参考
     * {@link JVSUDT#JVC_SendPlaybackData(int, byte, int, int)} 实际调用
     * {@link #sendCmd(int, byte, byte[], int)}
     * 
     * @param window 窗口索引
     * @param uchType
     * @param data
     */
    public static native boolean sendInteger(int window, byte uchType, int data);

    /**
     * 12.修改指定窗口播放标识位，是否启用远程回放，参考 {@link JVSUDT#ChangePlayFalg(int, int)}
     * 
     * @param window 窗口索引
     * @param enable
     * @return true：启用远程回放，false：不启用远程回放
     */
    public static native boolean enablePlayback(int window, boolean enable);

    /**************** 2015-02-09 V2.2.0 新增功能 ********************/

    /**
     * 13.初始化音频编码
     * 
     * @param type 类型，amr/alaw/ulaw，参考 {@link Consts#JAE_ENCODER_SAMR},
     *            {@link Consts#JAE_ENCODER_ALAW},
     *            {@link Consts#JAE_ENCODER_ULAW}
     * @param sampleRate
     * @param channelCount
     * @param bitCount
     * @param block PCM 640
     * @return
     */
    public static native boolean initAudioEncoder(int type, int sampleRate,
            int channelCount, int bitCount, int block);

    /**
     * 14.编码一帧
     * 
     * @param data
     * @return 失败的话返回 null
     */
    public static native byte[] encodeAudio(byte[] data);

    /**
     * 15.销毁音频编码，如果要切换编码参数，必须销毁的重新创建
     * 
     * @return
     */
    public static native boolean deinitAudioEncoder();

    /**
     * 16.发送字符串数据
     * 
     * @param window 窗口索引
     * @param uchType 发送类型
     * @param isExtend 是否扩展消息
     * @param count 扩展包数量
     * @param type 扩展消息类型
     * @param data 数据
     */
    public static native boolean sendString(int window, byte uchType,
            boolean isExtend, int count, int type, String data);

    /**
     * 发送超级字节数组哟
     * 
     * @param window
     * @param uchType
     * @param isExtend
     * @param count
     * @param type
     * @param p1
     * @param p2
     * @param p3
     * @param data
     * @param size
     * @return
     */
    public static native boolean sendSuperBytes(int window, byte uchType,
            boolean isExtend, int count, int type, int p1, int p2, int p3,
            byte[] data, int size);

    public static native boolean sendPrimaryBytes(int window, byte data_type,
            int packet_type, int packet_count, int extend_type, int extend_p1,
            int extend_p2, int extend_p3, byte[] data, int size);

    /**
     * 17.生成声波配置语音数据 生成声波配置数据，重复多次会阻塞执行
     * 
     * @param data：WiFi名称和密码
     * @param times：声波响的次数
     */
    public static native void genVoice(String data, int times);

    /**
     * 18.发送聊天命令，参考 {@link JVSUDT#JVC_SendTextData(int, byte, int, int)}
     * 
     * @param window 窗口索引
     * @param uchType
     * @param size
     * @param flag
     */
    public static native boolean sendTextData(int window, byte uchType,
            int size, int flag);

    /**
     * 19.开始录制，参考
     * {@link JVSUDT#StartRecordMP4(String, int, int, int, int, int, double, int)}
     * 
     * @param window 窗口索引
     * @param path
     * @param video
     * @param audio
     * @return
     */
    public static native boolean startRecord(int window, String path,
            boolean enableVideo, boolean enableAudio);

    /**
     * 20.检查对应窗口是否处于录像状态，TODO: 现在只有单路可用
     * 
     * @param window 窗口索引
     * @return true 正在录像，false 没在录像
     */
    public static native boolean checkRecord(int window);

    /**
     * 21.停止录制，参考 {@link JVSUDT#StopRecordMP4(int)}
     * 
     * @return
     */
    public static native boolean stopRecord();

    /**
     * 22.修改指定窗口音频标识位
     * 
     * @param window 窗口索引
     * @param enable 是否播放 Normaldata 的音频数据
     * @return
     */
    public static native boolean enablePlayAudio(int window, boolean enable);

    /**
     * 23.设置 AP，参考 {@link JVSUDT#JVC_ManageAP(int, byte, String)}
     * 
     * @param window 窗口索引
     * @param uchType
     * @param json
     */
    public static native boolean setAccessPoint(int window, byte uchType,
            String json);

    /**
     * 24.获取指定窗口是否正在播放音频
     * 
     * @param window 窗口索引
     * @param enable
     * @return true:正在监听，false：没有正在监听
     */
    public static native boolean isPlayAudio(int window);

    /**
     * 25.开启快速链接服务，参考 {@link JVSUDT#JVC_EnableHelp(boolean, int)}
     * 
     * @param enable
     * @param typeId enable == ture 时
     *            <ul>
     *            <li>1: 使用者是开启独立进程的云视通小助手</li>
     *            <li>2: 使用者是云视通客户端，支持独立进程的云视通小助手</li>
     *            <li>3: 使用者是云视通客户端，不支持独立进程的云视通小助手</li>
     *            </ul>
     * @param maxLimit 允许最大限制
     * @return
     */
    public static native boolean enableLinkHelper(boolean enable, int typeId,
            int maxLimit);

    /**
     * 26.给设备设置连接小助手，参考 {@link JVSUDT#JVC_SetHelpYSTNO(byte[], int)}
     * 
     * @param json [{gid: "A", no: 361, channel: 1, name: "abc", pwd:
     *            "123"},{gid: "A", no: 362, channel: 1, name: "abc", pwd:
     *            "123"}]
     * @return
     */
    public static native boolean setLinkHelper(String json);

    /**
     * 27.开启广播，参考 {@link JVSUDT#JVC_StartLANSerchServer(int, int)}
     * 
     * @param localPort 默认 9400
     * @param serverPort 默认 6666
     * @return
     */
    public static native int searchLanServer(int localPort, int serverPort);

    /**
     * 28.停止搜索局域网服务端，参考 {@link JVSUDT#JVC_StopLANSerchServer()}
     */
    public static native void stopSearchLanServer();

    /**
     * 29.搜索局域网设备，参考 搜索本局域网
     * {@link JVSUDT#JVC_MOLANSerchDevice(String, int, int, int, String, int)}
     * 跨网段广播
     * 
     * @param group
     * @param cloudSeeId
     * @param cardType
     * @param variety
     * @param deviceName
     * @param timeout 单位是毫秒
     * @param frequence
     * @return
     */
    public static native int searchLanDevice(String group, int cloudSeeId,
            int cardType, int variety, String deviceName, int timeout,
            int frequence);

    /**
     * 30.查询某个设备是否被搜索出来 局域网本网段广播
     * 
     * @param groudId 组标识
     * @param cloudSeeId 云视通编号
     * @param timeout 超时时间，毫秒
     * @return 调用是否成功，等回调
     */
    public static native boolean queryDevice(String groudId, int cloudSeeId,
            int timeout);

}
