
package com.jovision.tools;

import android.util.Log;

import com.jovision.Jni;
import com.jovision.beans.Device;
import com.jovision.beans.RemoteVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 播放相关功能
 * 
 * @author Administrator
 */

public class PlayUtil {

    /**
     * 获取设备的云视通组
     * 
     * @param deviceNum
     */
    public static String getGroup(String deviceNum) {

        StringBuffer groupSB = new StringBuffer();
        if (!"".equalsIgnoreCase(deviceNum)) {
            for (int i = 0; i < deviceNum.length(); i++) {
                if (Character.isLetter(deviceNum.charAt(i))) { // 用char包装类中的判断字母的方法判断每一个字符
                    groupSB = groupSB.append(deviceNum.charAt(i));
                }
            }
        }
        return groupSB.toString();
    }

    /**
     * 获取设备的云视通组和号码
     * 
     * @param deviceNum
     */
    public static int getYST(String deviceNum) {
        int yst = 0;

        StringBuffer ystSB = new StringBuffer();
        if (!"".equalsIgnoreCase(deviceNum)) {
            for (int i = 0; i < deviceNum.length(); i++) {
                if (Character.isDigit(deviceNum.charAt(i))) {
                    ystSB = ystSB.append(deviceNum.charAt(i));
                }
            }
        }

        if ("".equalsIgnoreCase(ystSB.toString())) {
            yst = 0;
        } else {
            yst = Integer.parseInt(ystSB.toString());
        }
        return yst;
    }

    /**
     * 长按给云台发命令
     * 
     * @param index
     * @param cmd
     * @param stop
     */
    public static void sendCtrlCMDLongPush(final int index, final int cmd,
            final boolean stop) {
        new Thread() {
            @Override
            public void run() {
                byte[] data = new byte[4];
                data[0] = (byte) cmd;
                data[1] = (byte) 0;
                data[2] = (byte) 0;
                data[3] = (byte) 0;
                // 云台命令
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data, 4);
                if (stop)
                    return;
                // 如果不是自动命令 发完云台命令接着发一条停止
                byte[] data1 = new byte[4];
                data1[0] = (byte) (cmd + 20);
                data1[1] = (byte) 0;
                data1[2] = (byte) 0;
                data1[3] = (byte) 0;
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data1, 4);
            }
        }.start();

    }

    /**
     * 云台自动巡航命令
     * 
     * @param index
     * @param cmd
     * @param stop
     */
    public static void sendCtrlCMDAuto(final int index, final int cmd,
            final boolean stop) {

        new Thread() {
            @Override
            public void run() {
                byte[] data = new byte[4];
                data[0] = (byte) cmd;
                data[1] = (byte) 0;
                data[2] = (byte) 0;
                data[3] = (byte) 0;
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data, 4);
                if (cmd == JVNetConst.JVN_YTCTRL_A)
                    return;

                byte[] data1 = new byte[4];
                data1[0] = (byte) (cmd + 20);
                data1[1] = (byte) 0;
                data1[2] = (byte) 0;
                data1[3] = (byte) 0;
                Jni.sendBytes(index, (byte) JVNetConst.JVN_CMD_YTCTRL, data1, 4);
            }
        }.start();

    }

    /***************** 以下为远程回放所有功能 ***************************/

    private static byte[] acFLBuffer = new byte[2048];

    public static void checkRemoteData(int index, String date) {
        Jni.sendBytes(index, (byte) JVNetConst.JVN_REQ_CHECK, date.getBytes(),
                28);
    }

    /**
     * 远程检索回调获取到码流数据list
     * 
     * @param pBuffer
     * @param deviceType
     * @param channelIndex
     * @return
     */
    public static ArrayList<RemoteVideo> getRemoteList(byte[] pBuffer,
            int deviceType, int channelOfChannel) {

        ArrayList<RemoteVideo> datalist = new ArrayList<RemoteVideo>();

        try {
            String textString1 = new String(pBuffer);
            Log.v("远程回放pBuffer", "deviceType=" + deviceType + ";pBuffer="
                    + textString1);

            int nSize = pBuffer.length;
            // 无数据
            if (nSize == 0) {
                return datalist;
            }

            if (deviceType == 0) {
                for (int i = 0; i <= nSize - 7; i += 7) {
                    RemoteVideo rv = new RemoteVideo();
                    rv.remoteChannel = String.format("%02d", channelOfChannel);
                    rv.remoteDate = String.format("%c%c:%c%c:%c%c",
                            pBuffer[i + 1], pBuffer[i + 2], pBuffer[i + 3],
                            pBuffer[i + 4], pBuffer[i + 5], pBuffer[i + 6]);
                    rv.remoteDisk = String.format("%c", pBuffer[i]);
                    datalist.add(rv);
                }
            } else if (deviceType == 1 || deviceType == 4 || deviceType == 5) {
                int nIndex = 0;
                for (int i = 0; i <= nSize - 10; i += 10) {
                    acFLBuffer[nIndex++] = pBuffer[i];// 录像所在盘
                    acFLBuffer[nIndex++] = pBuffer[i + 7];// 录像类型
                    RemoteVideo rv = new RemoteVideo();
                    rv.remoteChannel = String.format("%c%c", pBuffer[i + 8],
                            pBuffer[i + 9]);
                    rv.remoteDate = String.format("%c%c:%c%c:%c%c",
                            pBuffer[i + 1], pBuffer[i + 2], pBuffer[i + 3],
                            pBuffer[i + 4], pBuffer[i + 5], pBuffer[i + 6]);
                    rv.remoteKind = String.format("%c", pBuffer[i + 7]);
                    rv.remoteDisk = String.format("%s%d", "",
                            (pBuffer[i] - 'C') / 10 + 1);
                    datalist.add(rv);
                }

            } else if (deviceType == 2 || deviceType == 3) {
                for (int i = 0; i <= nSize - 7; i += 7) {
                    RemoteVideo rv = new RemoteVideo();
                    rv.remoteChannel = String.format("%02d", channelOfChannel);
                    rv.remoteDate = String.format("%c%c:%c%c:%c%c",
                            pBuffer[i + 1], pBuffer[i + 2], pBuffer[i + 3],
                            pBuffer[i + 4], pBuffer[i + 5], pBuffer[i + 6]);
                    rv.remoteDisk = String.format("%c", pBuffer[i]);
                    datalist.add(rv);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datalist;
    }

    /**
     * 拼接远程回放视频参数
     * 
     * @param videoBean
     * @param is05
     * @param deviceType
     * @param year
     * @param month
     * @param day
     * @param listIndex
     * @return
     */
    public static String getPlayFileString(RemoteVideo videoBean,
            boolean isJFH, int deviceType, int year, int month, int day,
            int listIndex) {
        byte acChn[] = new byte[3];
        byte acTime[] = new byte[10];
        byte acDisk[] = new byte[2];
        String acBuffStr = "";
        if (null == videoBean) {
            return acBuffStr;
        }

        Log.v("远程回放单个文件", "deviceType=" + deviceType + ";isJFH=" + isJFH);
        if (isJFH) {
            if (deviceType == 0) {

                // sprintf(acChn, "%s",videoBean.remoteChannel);
                String channelStr = String
                        .format("%s", videoBean.remoteChannel);
                Log.e("channelStr", channelStr);
                System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                        channelStr.length());

                // sprintf(acTime, "%s",videoBean.remoteDate);
                String acTimeStr = String.format("%s", videoBean.remoteDate);
                System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                        acTimeStr.length());

                // sprintf(acDisk, "%s",videoBean.remoteDisk);
                String acDiskStr = String.format("%s", videoBean.remoteDisk);
                System.arraycopy(acDiskStr.getBytes(), 0, acDisk, 0,
                        acDiskStr.length());
                acBuffStr = String.format(
                        "%c:\\JdvrFile\\%04d%02d%02d\\%c%c%c%c%c%c%c%c.mp4",
                        acDisk[0], year, month, day, acChn[0], acChn[1],
                        acTime[0], acTime[1], acTime[3], acTime[4], acTime[6],
                        acTime[7]);

            } else if (deviceType == 1 || deviceType == 4 || deviceType == 5) {
                String channelStr = String
                        .format("%s", videoBean.remoteChannel);
                System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                        channelStr.length());

                // sprintf(acTime, "%s",videoBean.remoteDate);
                String acTimeStr = String.format("%s", videoBean.remoteDate);
                System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                        acTimeStr.length());

                acBuffStr = String.format(
                        "./rec/%02d/%04d%02d%02d/%c%c%c%c%c%c%c%c%c.mp4",
                        acFLBuffer[listIndex * 2] - 'C', year, month, day,
                        acFLBuffer[listIndex * 2 + 1], acChn[0], acChn[1],
                        acTime[0], acTime[1], acTime[3], acTime[4], acTime[6],
                        acTime[7]);

            }

            Log.v("url: ", acBuffStr);
        } else if (deviceType == 0) {
            String channelStr = String.format("%s", videoBean.remoteChannel);
            System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                    channelStr.length());

            // sprintf(acTime, "%s",videoBean.remoteDate);
            String acTimeStr = String.format("%s", videoBean.remoteDate);
            System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                    acTimeStr.length());

            // sprintf(acDisk, "%s",videoBean.remoteDisk);
            String acDiskStr = String.format("%s", videoBean.remoteDisk);
            System.arraycopy(acDiskStr.getBytes(), 0, acDisk, 0,
                    acDiskStr.length());

            acBuffStr = String.format(
                    "%c:\\JdvrFile\\%04d%02d%02d\\%c%c%c%c%c%c%c%c.sv4",
                    acDisk[0], year, month, day, acChn[0], acChn[1], acTime[0],
                    acTime[1], acTime[3], acTime[4], acTime[6], acTime[7]);

        } else if (deviceType == 1 || deviceType == 4 || deviceType == 5) {
            String channelStr = String.format("%s", videoBean.remoteChannel);
            System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                    channelStr.length());
            Log.v("channelStr:", channelStr);
            // sprintf(acTime, "%s",videoBean.remoteDate);
            String acTimeStr = String.format("%s", videoBean.remoteDate);
            Log.v("acTimeStr:", acTimeStr);
            System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                    acTimeStr.length());
            acBuffStr = String.format(
                    "./rec/%02d/%04d%02d%02d/%c%c%c%c%c%c%c%c%c.sv5",
                    acFLBuffer[listIndex * 2] - 'C', year, month, day,
                    acFLBuffer[listIndex * 2 + 1], acChn[0], acChn[1],
                    acTime[0], acTime[1], acTime[3], acTime[4], acTime[6],
                    acTime[7]);
            Log.v("acBuffStr:", acBuffStr);
        } else if (deviceType == 2 || deviceType == 3) {
            String channelStr = String.format("%s", videoBean.remoteChannel);
            System.arraycopy(channelStr.getBytes(), 0, acChn, 0,
                    channelStr.length());

            // sprintf(acTime, "%s",videoBean.remoteDate);
            String acTimeStr = String.format("%s", videoBean.remoteDate);
            System.arraycopy(acTimeStr.getBytes(), 0, acTime, 0,
                    acTimeStr.length());

            // sprintf(acDisk, "%s",videoBean.remoteDisk);
            String acDiskStr = String.format("%s", videoBean.remoteDisk);
            System.arraycopy(acDiskStr.getBytes(), 0, acDisk, 0,
                    acDiskStr.length());
            acBuffStr = String.format(
                    "%c:\\JdvrFile\\%04d%02d%02d\\%c%c%c%c%c%c%c%c.sv6",
                    acDisk[0], year, month, day, acChn[0], acChn[1], acTime[0],
                    acTime[1], acTime[3], acTime[4], acTime[6], acTime[7]);
            Log.v("url: ", acBuffStr);

        }
        Log.v("tags", "bytesize: " + acBuffStr.getBytes().length + ", url:"
                + acBuffStr);
        acChn = null;
        acTime = null;
        acDisk = null;

        return acBuffStr;
    }

    /**
     * 查询音频监听状态
     */
    public static boolean isPlayAudio(int index) {
        return Jni.isPlayAudio(index);
    }

    /**
     * 开始音频监听
     */
    public static boolean startAudioMonitor(int index) {
        return Jni.enablePlayAudio(index, true);
    }

    /**
     * 停止音频监听
     */
    public static boolean stopAudioMonitor(int index) {
        return Jni.enablePlayAudio(index, false);
    }

    /**
     * 给设备设置小助手
     */
    public static void setHelperToDevice(Device device) {
        if (null == device) {
            return;
        }
        JSONArray array = new JSONArray();
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("gid", device.getGid());
            object.put("no", device.getNo());
            object.put("channel", 1);
            object.put("name", device.getUser());
            object.put("pwd", device.getPwd());
            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!"".equalsIgnoreCase(array.toString())) {
            Log.e("需要设置小助手", array.toString());
            Jni.setLinkHelper(array.toString());
        }
    }

    /**
     * 开启广播
     */
    public static int openBroadCast() {
        return Jni.searchLanServer(9400, 6666);
    }

    /**
     * 停止广播
     */
    public static void stopBroadCast() {
        Jni.stopSearchLanServer();
    }
}
