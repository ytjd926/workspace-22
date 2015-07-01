
package com.jovision.demo;

import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jovision.Consts;
import com.jovision.Jni;
import com.jovision.tools.MyAudio;

public class WaveSetActivity extends BaseActivity {

    private EditText wifiName, wifiPass;
    private Button sendWave;

    protected String params = "";
    protected MyAudio playAudio;
    protected static int audioSampleRate = 48000;
    protected static int playBytes = 16;

    // 播放操作步骤音频
    protected AssetManager assetMgr = null;
    protected MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {
        switch (what) {
            case Consts.WHAT_PLAY_AUDIO_WHAT: {
                switch (arg2) {
                    case MyAudio.ARG2_START: {
                        break;
                    }
                    case MyAudio.ARG2_FINISH: {
                        break;
                    }
                    case MyAudio.ARG2_WAVE_FINISH: {// 声波播放完毕
                        handler.sendMessageDelayed(
                                handler.obtainMessage(Consts.WHAT_SEND_WAVE_FINISHED),
                                500);
                        break;
                    }
                }
                break;
            }
            // 获取到声波音频
            case Consts.CALL_GEN_VOICE: {
                if (1 == arg2) {// 数据
                    if (null != obj && null != playAudio) {
                        byte[] data = (byte[]) obj;
                        playAudio.put(data);
                    }
                } else if (0 == arg2) {// 结束
                    byte[] data = {
                            'F', 'i', 'n'
                    };
                    playAudio.put(data);
                }
                break;
            }
            default: {
                handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
                break;
            }
        }
    }

    @Override
    protected void initSettings() {
        assetMgr = this.getAssets();
        playAudio = MyAudio.getIntance(Consts.WHAT_PLAY_AUDIO_WHAT,
                WaveSetActivity.this, audioSampleRate);
    }

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_waveset);
        wifiName = (EditText) findViewById(R.id.wifiname);
        wifiPass = (EditText) findViewById(R.id.wifipass);
        sendWave = (Button) findViewById(R.id.send_wave);

        sendWave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("".equalsIgnoreCase(wifiName.getText().toString().trim())) {
                    showTextToast(R.string.wifi_name_notnull);
                } else if ("".equalsIgnoreCase(wifiPass.getText().toString()
                        .trim())) {
                    showTextToast(R.string.wifi_pass_notnull);
                } else {
                    try {
                        if (null != mediaPlayer) {
                            mediaPlayer.stop();
                        }
                        playAudio.startPlay(playBytes, true);
                        params = wifiName.getText() + ";" + wifiPass.getText();
                        Log.v("soundWave-params", "params:" + params);
                        Jni.genVoice(params, 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void saveSettings() {

    }

    @Override
    protected void freeMe() {

    }

}
