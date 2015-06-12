
package com.jovision.beans;

import android.view.Surface;
import android.view.SurfaceView;

import com.jovision.tools.AppConsts;

/**
 * 简单的通道集合类
 * 
 * @author neo
 */
public class Channel {

    /** 窗口索引 */
    private int index;
    /** 设备通道，从 0 开始 */
    private int channel;

    private boolean isConnected = false;
    private boolean isPaused = false;

    private boolean isRemotePlay;
    private boolean isConfigChannel;

    private Surface surface;
    private SurfaceView surfaceView;

    private boolean isAuto = false;// 是否开启自动巡航
    private Device parent;
    private int audioType = 0;// 音频类型
    private int audioByte = 0;// 音频比特率8 16

    // [Neo] 语音对讲时编码类型
    private int audioEncType = 0;
    // [Neo] 语音对讲时编码帧大小
    private int audioBlock = 0;

    private boolean agreeTextData = false;// 是否同意文本聊天
    private boolean supportVoice = true;
    private boolean isVoiceCall = false;// 是否正在对讲
    private boolean singleVoice = false;// 单向对讲标识位，默认是双向的

    public Channel(int index, int channel, boolean isConnected,
            boolean isRemotePlay) {
        this.index = index;
        this.channel = channel;
        this.isConnected = isConnected;
        this.isRemotePlay = isRemotePlay;

        isConfigChannel = false;
    }

    public Channel() {
        this.index = 0;
        this.channel = 0;

        this.isConnected = false;
        this.isRemotePlay = false;
        this.isConfigChannel = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isRemotePlay() {
        return isRemotePlay;
    }

    public void setRemotePlay(boolean isRemotePlay) {
        this.isRemotePlay = isRemotePlay;
    }

    public boolean isConfigChannel() {
        return isConfigChannel;
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder(1024);
        sBuilder.append("Channel-").append((channel < 0) ? "X" : channel)
                .append(", window = ").append(index).append(": isConnected = ")
                .append(isConnected).append(", isPaused: ").append(isPaused)
                .append(", surface = ")
                .append((null != surface) ? surface.hashCode() : "null")
                .append(", hashcode = ").append(hashCode());
        return sBuilder.toString();
    }

    public Device getParent() {
        return parent;
    }

    public void setParent(Device parent) {
        this.parent = parent;
    }

    public int getAudioType() {
        return audioType;
    }

    public void setAudioType(int audioType) {
        this.audioType = audioType;
    }

    public int getAudioByte() {
        return audioByte;
    }

    public void setAudioByte(int audioByte) {
        this.audioByte = audioByte;
    }

    public int getAudioEncType() {
        return audioEncType;
    }

    public int getAudioBlock() {
        return audioBlock;
    }

    public void setAudioEncType(int audioEncType) {
        this.audioEncType = audioEncType;

        switch (audioEncType) {
            case AppConsts.JAE_ENCODER_ALAW:
            case AppConsts.JAE_ENCODER_ULAW:
                audioBlock = AppConsts.ENC_G711_SIZE;
                break;

            case AppConsts.JAE_ENCODER_SAMR:
                audioBlock = AppConsts.ENC_AMR_SIZE;
                break;

            case AppConsts.JAE_ENCODER_G729:
                audioBlock = AppConsts.ENC_G729_SIZE;
                break;

            default:
                audioBlock = AppConsts.ENC_PCM_SIZE;
                break;
        }
    }

    public void setConfigChannel(boolean isConfigChannel) {
        this.isConfigChannel = isConfigChannel;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public boolean isVoiceCall() {
        return isVoiceCall;
    }

    public void setVoiceCall(boolean isVoiceCall) {
        this.isVoiceCall = isVoiceCall;
    }

    public boolean isSingleVoice() {
        return singleVoice;
    }

    public void setSingleVoice(boolean singleVoice) {
        this.singleVoice = singleVoice;
    }

    public boolean isSupportVoice() {
        return supportVoice;
    }

    public void setSupportVoice(boolean supportVoice) {
        this.supportVoice = supportVoice;
    }

    public boolean isAgreeTextData() {
        return agreeTextData;
    }

    public void setAgreeTextData(boolean agreeTextData) {
        this.agreeTextData = agreeTextData;
    }

}
