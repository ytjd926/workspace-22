
package com.jovision.demo;

import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jovision.Consts;
import com.jovision.Jni;
import com.jovision.tools.JVNetConst;

import org.json.JSONException;
import org.json.JSONObject;

public class RemotePlayBackActivity extends BaseActivity {

    private static final String TAG = "RemotePlayBackActivity";

    protected int surfaceWidth = -1;
    protected int surfaceHeight = -1;
    public DisplayMetrics disMetrics;

    private SurfaceView playSurface;// 视频播放view
    private SurfaceHolder holder;
    private TextView linkState;// 连接状态
    private SeekBar progressBar;
    private Button pauseBtn;

    private int indexOfChannel;
    private String acBuffStr;
    private int currentProgress;// 当前进度
    private int seekProgress;// 手动进度
    private int totalProgress;// 总进度
    private Boolean isPaused = false;// 是否已暂停

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {
        switch (what) {

            case Consts.CALL_CONNECT_CHANGE: {
                Log.i(TAG, "CALL_CONNECT_CHANGE:what=" + what + ",arg1=" + arg1
                        + ",arg2=" + arg2 + ",obj=" + obj);
                switch (arg2) {
                // 1 -- 连接成功
                    case JVNetConst.CONNECT_OK: {
                        break;
                    }
                    // 2 -- 断开连接成功
                    case JVNetConst.DISCONNECT_OK: {
                        showTextToast(R.string.closed);

                        break;
                    }
                    // 4 -- 连接失败
                    case JVNetConst.CONNECT_FAILED: {
                        showTextToast(R.string.closed);

                        break;
                    }
                    // 6 -- 连接异常断开
                    case JVNetConst.ABNORMAL_DISCONNECT: {
                        showTextToast(R.string.closed);

                        break;
                    }
                    // 7 -- 服务停止连接，连接断开
                    case JVNetConst.SERVICE_STOP: {
                        showTextToast(R.string.closed);

                        break;
                    }
                    case Consts.BAD_NOT_CONNECT: {
                        showTextToast(R.string.closed);
                        break;
                    }
                    // 3 -- 不必要重复连接
                    case JVNetConst.NO_RECONNECT: {
                        break;
                    }
                    // 5 -- 没有连接
                    case JVNetConst.NO_CONNECT: {
                        break;
                    }
                    // 8 -- 断开连接失败
                    case JVNetConst.DISCONNECT_FAILED: {
                        break;
                    }

                    // 9 -- 其他错误
                    case JVNetConst.OHTER_ERROR: {
                        break;
                    }
                }
            }

            case Consts.CALL_PLAY_DATA: {// 远程回放数据
                linkState.setVisibility(View.GONE);
                switch (arg2) {
                    case JVNetConst.JVN_DATA_O: {
                        if (0 == totalProgress) {
                            try {
                                JSONObject jobj;
                                jobj = new JSONObject(obj.toString());
                                Log.i(TAG, "OFrame = " + obj.toString());
                                if (null != jobj) {
                                    totalProgress = jobj.optInt("total");
                                    progressBar.setMax(totalProgress);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case JVNetConst.JVN_DATA_I:
                    case JVNetConst.JVN_DATA_B:
                    case JVNetConst.JVN_DATA_P: {
                        currentProgress++;
                        progressBar.setProgress(currentProgress);
                        break;
                    }
                }

                break;
            }
            case Consts.CALL_PLAY_DOOMED: {// 远程回放结束
                if (Consts.PLAYBACK_DONE == arg2) {
                    this.finish();
                }
            }
            case Consts.WHAT_DUMMY: {
                playSurface.getHolder().setFixedSize(surfaceWidth, surfaceHeight);
                break;
            }
            default:
                break;
        }

    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {
        handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
    }

    @Override
    protected void initSettings() {
        Intent intent = getIntent();
        if (null != intent) {
            indexOfChannel = intent.getIntExtra("IndexOfChannel", 0);
            acBuffStr = intent.getStringExtra("acBuffStr");
        }
        currentProgress = 0;
    }

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_playback);
        playSurface = (SurfaceView) findViewById(R.id.playsurface);
        linkState = (TextView) findViewById(R.id.linkstate);
        linkState.setTextColor(Color.GREEN);
        linkState.setVisibility(View.VISIBLE);
        linkState.setText(R.string.connectting);

        progressBar = (SeekBar) findViewById(R.id.playback_seekback);
        progressBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        pauseBtn = (Button) findViewById(R.id.playbackpause);
        pauseBtn.setOnClickListener(myOnClickListener);
        disMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disMetrics);
        if (surfaceWidth < 0 || surfaceHeight < 0) {
            surfaceWidth = disMetrics.widthPixels;
            surfaceHeight = (int) (0.75 * disMetrics.widthPixels);
        }

        holder = playSurface.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Jni.pause(indexOfChannel);
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                boolean resumeRes = Jni.resume(indexOfChannel,
                        holder.getSurface());// boolean
                Log.v(TAG, "JNI-Remote-Resume=" + resumeRes);
                boolean enable = Jni.enablePlayback(indexOfChannel, true);
                if (enable) {
                    Jni.sendBytes(indexOfChannel,
                            (byte) JVNetConst.JVN_REQ_PLAY,
                            (byte[]) acBuffStr.getBytes(),
                            acBuffStr.getBytes().length);
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
            }
        });

    }

    private OnClickListener myOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.playbackpause: {// 暂停继续按钮
                    if (isPaused) {
                        isPaused = false;
                        pauseBtn.setBackgroundResource(R.drawable.video_stop_icon);
                        // 继续播放视频
                        Jni.sendBytes(indexOfChannel, JVNetConst.JVN_CMD_PLAYGOON,
                                new byte[0], 0);
                    } else {
                        isPaused = true;
                        // 暂停视频
                        Jni.sendBytes(indexOfChannel, JVNetConst.JVN_CMD_PLAYPAUSE,
                                new byte[0], 0);
                        pauseBtn.setBackgroundResource(R.drawable.video_play_icon);
                    }
                    break;
                }
            }

        }

    };

    /**
     * 远程回放进度条拖动事件
     */
    OnSeekBarChangeListener mOnSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            seekProgress = arg1;
        }

        @Override
        public void onStartTrackingTouch(SeekBar arg0) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar arg0) {
            currentProgress = seekProgress;
            Jni.sendInteger(indexOfChannel, JVNetConst.JVN_CMD_PLAYSEEK,
                    seekProgress);
        }
    };

    @Override
    public void onBackPressed() {
        boolean stopRes = Jni.sendBytes(indexOfChannel,
                JVNetConst.JVN_CMD_PLAYSTOP, new byte[0], 0);
        Log.v(TAG, "JNI-Remote-stopRes=" + stopRes);

        RemotePlayBackActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void saveSettings() {

    }

    @Override
    protected void freeMe() {
        Jni.enablePlayback(indexOfChannel, false);
    }

}
