
package com.jovision.demo;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jovision.Consts;
import com.jovision.Jni;
import com.jovision.beans.Device;
import com.jovision.tools.MainApplication;
import com.jovision.tools.PlayUtil;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {
    static {
        System.loadLibrary("alu");
        System.loadLibrary("play");
    }

    EditText devNumET, devChanET, connChanET, devIP, devPort, devUser, devPwd;
    Button normalConnectBtn, apConnectBtn, allBroad, lanBroad, devWaveSetBtn;
    public static Device device;// 连接的设备
    public static int channelIndex;// 连接的通道号

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {
        handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
    }

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {
        Log.v("MainActivity", "what=" + what + ";arg1=" + arg1 + ";arg2=" + arg2 + ";obj=" + obj);
        switch (what) {
        // 广播回调
            case Consts.CALL_LAN_SEARCH: {
                JSONObject broadObj;
                try {
                    broadObj = new JSONObject(obj.toString());
                    if (0 == broadObj.optInt("timeout")) {// 广播到的数据
                        Log.v("MainActivity", "广播到的数据obj=" + obj);
                    } else if (1 == broadObj.optInt("timeout")) {// 广播完成
                        Log.v("MainActivity", "广播超时");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void initSettings() {

    }

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_main);

        boolean initSdk = Jni.init(((MainApplication) getApplication()), 9200,
                Consts.LOG_PATH);

        boolean enableHeler = Jni.enableLinkHelper(true, 3, 10);// 开小助手
        Log.e("enableHeler", enableHeler + "");
        if (initSdk) {
            Jni.enableLog(true);
            String version = Jni.getVersion();
            Log.e("SDK_VERSION", version);
        } else {
            Log.e("initSDK", "initSDK--failed");
        }
        PlayUtil.openBroadCast();

        devNumET = (EditText) findViewById(R.id.dev_num);
        devChanET = (EditText) findViewById(R.id.channel_num);
        connChanET = (EditText) findViewById(R.id.connect_channel_index);
        devIP = (EditText) findViewById(R.id.dev_ip);
        devPort = (EditText) findViewById(R.id.dev_port);
        devUser = (EditText) findViewById(R.id.dev_user);
        devPwd = (EditText) findViewById(R.id.dev_pwd);

        normalConnectBtn = (Button) findViewById(R.id.normal_connect);
        apConnectBtn = (Button) findViewById(R.id.ap_connect);
        allBroad = (Button) findViewById(R.id.broadcastall);
        lanBroad = (Button) findViewById(R.id.broadcastlan);
        allBroad.setOnClickListener(mOnClickListener);
        lanBroad.setOnClickListener(mOnClickListener);

        normalConnectBtn.setOnClickListener(mOnClickListener);
        apConnectBtn.setOnClickListener(mOnClickListener);

        devWaveSetBtn = (Button) findViewById(R.id.dev_wave_set);
        devWaveSetBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WaveSetActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.broadcastall: {// 广播全部
                    Jni.searchLanDevice("", 0, 0, 0, "", 10000, 2);
                    break;
                }
                case R.id.broadcastlan: {// 局域网广播
                    Jni.queryDevice("A", 361, 2000);
                    break;
                }
                default: {
                    int channel = Integer.parseInt(connChanET.getText().toString());
                    int channelCount = Integer.parseInt(devChanET.getText().toString());

                    if (channel > channelCount) {
                        Toast.makeText(MainActivity.this,
                                R.string.connect_channel_error, Toast.LENGTH_LONG)
                                .show();
                    } else {
                        channelIndex = channel - 1;

                        String devFullNo = devNumET.getText().toString();
                        String group = "";
                        int yst = -1;
                        if (!"".equalsIgnoreCase(devFullNo)) {
                            group = PlayUtil.getGroup(devFullNo);
                            yst = PlayUtil.getYST(devFullNo);
                        }

                        device = new Device(devIP.getText().toString(),
                                Integer.parseInt(devPort.getText().toString()), group,
                                yst, devUser.getText().toString(), devPwd.getText()
                                        .toString(), channelCount);

                        Intent intent = new Intent();

                        switch (view.getId()) {
                            case R.id.normal_connect: {
                                intent.putExtra("APConnect", false);
                                break;
                            }
                            case R.id.ap_connect: {
                                intent.putExtra("APConnect", true);
                                break;
                            }
                        }

                        PlayUtil.setHelperToDevice(device);

                        intent.setClass(MainActivity.this, PlayActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    }
                }

            }
        }
    };

    @Override
    protected void saveSettings() {

    }

    @Override
    protected void freeMe() {
        PlayUtil.stopBroadCast();
        Jni.deleteLog();
        Jni.deinit();
    }

}
