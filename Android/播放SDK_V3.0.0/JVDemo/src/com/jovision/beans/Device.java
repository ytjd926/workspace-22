
package com.jovision.beans;

import java.util.ArrayList;

/**
 * 简单的设备集合类
 * 
 * @author neo
 */
public class Device {

    private ArrayList<Channel> channelList;

    private String ip;
    private int port;

    private String gid;
    private int no;
    private String fullNo;

    private String user;
    private String pwd;

    private boolean isHelperEnabled;

    /** 设备类型 */
    private int type;// 云视通端定义 4：家用设备 其他值 非家用
    /** 0几版解码器 */
    private boolean is05;
    /** 是否带帧头 */
    private boolean isJFH;
    /** 是否是板卡 */
    private boolean isCard = false;

    /**
     * 创建指定通道个数的设备
     * 
     * @param ip
     * @param port
     * @param gid
     * @param no
     * @param user
     * @param pwd
     * @param isHomeProduct
     * @param channelCount
     */
    public Device(String ip, int port, String gid, int no, String user,
            String pwd, int channelCount) {
        this.ip = ip;
        this.port = port;
        this.gid = gid;
        this.no = no;
        this.fullNo = gid + no;
        this.user = user;
        this.pwd = pwd;

        isHelperEnabled = false;
        channelList = new ArrayList<Channel>();

        for (int i = 0; i < channelCount; i++) {
            Channel channel = new Channel();
            channel.setIndex(i);
            channel.setChannel(i + 1);
            channelList.add(channel);
        }
    }

    public ArrayList<Channel> getChannelList() {
        return channelList;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getGid() {
        return gid;
    }

    public int getNo() {
        return no;
    }

    public String getFullNo() {
        return fullNo;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setHelperEnabled(boolean isHelperEnabled) {
        this.isHelperEnabled = isHelperEnabled;
    }

    public boolean isHelperEnabled() {
        return isHelperEnabled;
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder(1024);
        sBuilder.append(fullNo).append("(").append(ip).append(":").append(port)
                .append("): ").append("user = ").append(user)
                .append(", pwd = ").append(pwd).append(", enabled = ")
                .append(isHelperEnabled);
        if (null != channelList) {
            int size = channelList.size();
            for (int i = 0; i < size; i++) {
                sBuilder.append("\n").append(channelList.get(i).toString());
            }
        }
        return sBuilder.toString();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean is05() {
        return is05;
    }

    public void set05(boolean is05) {
        this.is05 = is05;
    }

    public boolean isJFH() {
        return isJFH;
    }

    public void setJFH(boolean isJFH) {
        this.isJFH = isJFH;
    }

    public boolean isCard() {
        return isCard;
    }

    public void setCard(boolean isCard) {
        this.isCard = isCard;
    }

    public void setChannelList(ArrayList<Channel> channelList) {
        this.channelList = channelList;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setFullNo(String fullNo) {
        this.fullNo = fullNo;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
