
package com.jovision.demo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jovision.Consts;
import com.jovision.beans.RemoteVideo;
import com.jovision.tools.PlayUtil;
import com.jovision.tools.RemoteVideoAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class RemoteListActivity extends BaseActivity {

    private final String TAG = "RemoteList";

    private EditText selectDate;// 选择的日期
    private ImageView moreData;// 下三角
    private Button search;// 检索
    private RemoteVideoAdapter remoteVideoAdapter;
    private ListView remoteListView;

    private String date = "";
    private Calendar rightNow = Calendar.getInstance();
    private int year;
    private int month;
    private int day;

    private ArrayList<RemoteVideo> videoList;
    private int deviceType;// 设备类型
    private int indexOfChannel;// 通道index
    private int channelOfChannel;// 通道index
    private boolean isJFH;// 是否带帧头

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {
        dismissDialog();
        switch (what) {
            case Consts.CALL_CHECK_RESULT: {// 查询远程回放数据
                byte[] pBuffer = (byte[]) obj;
                videoList = PlayUtil.getRemoteList(pBuffer, deviceType,
                        channelOfChannel);
                if (null != videoList && 0 != videoList.size()) {
                    handler.sendMessage(handler
                            .obtainMessage(Consts.WHAT_REMOTE_DATA_SUCCESS));
                } else {
                    handler.sendMessage(handler
                            .obtainMessage(Consts.WHAT_REMOTE_NO_DATA_FAILED));
                }
                break;
            }
            case Consts.WHAT_REMOTE_DATA_SUCCESS: {
                remoteVideoAdapter.setData(videoList);
                remoteListView.setAdapter(remoteVideoAdapter);
                remoteVideoAdapter.notifyDataSetChanged();
                break;
            }
            case Consts.WHAT_REMOTE_DATA_FAILED: {
                remoteVideoAdapter.setData(videoList);
                remoteVideoAdapter.notifyDataSetChanged();
                showTextToast(R.string.str_video_load_failed);
                break;
            }
            case Consts.WHAT_REMOTE_NO_DATA_FAILED: {
                remoteVideoAdapter.setData(videoList);
                remoteVideoAdapter.notifyDataSetChanged();
                showTextToast(R.string.str_video_nodata_failed);
                break;
            }
        }

    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {
        handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));

    }

    @Override
    protected void initSettings() {

    }

    @Override
    protected void initUi() {
        setContentView(R.layout.activity_remotelist);

        selectDate = (EditText) findViewById(R.id.datetext);
        moreData = (ImageView) findViewById(R.id.dateselectbtn);
        search = (Button) findViewById(R.id.search);
        remoteVideoAdapter = new RemoteVideoAdapter(RemoteListActivity.this);
        remoteListView = (ListView) findViewById(R.id.videolist);
        remoteListView.setOnItemClickListener(onItemClickListener);
        selectDate.setInputType(InputType.TYPE_NULL);
        selectDate.setOnTouchListener(onTouchListener);
        moreData.setOnTouchListener(onTouchListener);
        search.setOnClickListener(onClickListener);

        year = rightNow.get(Calendar.YEAR);
        month = rightNow.get(Calendar.MONTH) + 1;
        day = rightNow.get(Calendar.DAY_OF_MONTH);
        selectDate.setText(year + "-" + month + "-" + day);

        date = String.format("%04d%02d%02d000000%04d%02d%02d000000", year,
                month, day, year, month, day);
        Intent intent = getIntent();
        if (null != intent) {
            deviceType = intent.getIntExtra("DeviceType", 0);
            indexOfChannel = intent.getIntExtra("IndexOfChannel", 0);
            channelOfChannel = intent.getIntExtra("ChannelOfChannel", 0);
            isJFH = intent.getBooleanExtra("isJFH", false);
        }

        searchRemoteData(2 * 1000);
    }

    /**
     * 远程回放列点击播放事件
     */
    OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            RemoteVideo videoBean = videoList.get(arg2);
            String acBuffStr = PlayUtil.getPlayFileString(videoBean, isJFH,
                    deviceType, year, month, day, arg2);
            Log.v(TAG, "acBuffStr:" + acBuffStr);
            if (null != acBuffStr && !"".equalsIgnoreCase(acBuffStr)) {
                Intent intent = new Intent();
                intent.setClass(RemoteListActivity.this,
                        RemotePlayBackActivity.class);
                intent.putExtra("IndexOfChannel", indexOfChannel);
                intent.putExtra("acBuffStr", acBuffStr);
                RemoteListActivity.this.startActivity(intent);
            }

        }
    };

    /**
     * 单击事件
     */
    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search:
                    searchRemoteData(100);
                    break;
            }
        }
    };

    /**
     * 检索远程回放数据
     */
    public void searchRemoteData(final int sleepCount) {
        createDialog("", false);
        String temStr = selectDate.getText().toString();
        String[] temArray = temStr.split("-");
        year = Integer.parseInt(temArray[0]);
        month = Integer.parseInt(temArray[1]);
        day = Integer.parseInt(temArray[2]);
        date = String.format("%04d%02d%02d000000%04d%02d%02d000000", year,
                month, day, year, month, day);

        Thread searchThread = new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(sleepCount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PlayUtil.checkRemoteData(indexOfChannel, date);
            }

        };
        searchThread.start();
    }

    /**
     * 日历轻触事件
     */
    OnTouchListener onTouchListener = new TextView.OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            if (MotionEvent.ACTION_DOWN == arg1.getAction()) {
                new DatePickerDialog(RemoteListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker arg0, int year,
                                    int month, int day) {
                                selectDate.setText(year + "-" + (++month) + "-"
                                        + day);
                                date = String.format(
                                        "%04d%02d%02d000000%04d%02d%02d000000",
                                        year, month, day, year, month, day);
                                searchRemoteData(100);
                            }
                        }, rightNow.get(Calendar.YEAR),
                        rightNow.get(Calendar.MONTH),
                        rightNow.get(Calendar.DAY_OF_MONTH)).show();
            }
            return true;
        }

    };

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
}
