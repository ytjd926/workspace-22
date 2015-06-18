package com.bright.examples.demos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bright.examples.model.BeaconModel;
import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconManager;
import com.brtbeacon.sdk.BRTRegion;
import com.brtbeacon.sdk.RangingListener;
import com.brtbeacon.sdk.ServiceReadyCallback;
import com.brtbeacon.sdk.Utils;
import com.brtbeacon.sdk.service.RangingResult;

import com.brtbeacon.sdk.BRTBeacon;

public class BRTBeaconManagerListBeaconsActivity extends Activity {

	private static final String TAG = BRTBeaconManagerListBeaconsActivity.class.getSimpleName();

	public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
	public static final String EXTRAS_BEACON = "extrasBeacon";

	private static final int REQUEST_ENABLE_BT = 1234;
	/**
	 * 用于标识扫描指定uuid的设备 uuid为null 表示扫描所有
	 */
	private static final BRTRegion ALL_BRIGHT_BEACONS_REGION = new BRTRegion("rid", null, null, null, null);

	private static final String BRIGHT_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final BRTRegion BRIGHT_BEACONS_REGION = new BRTRegion("rid", BRIGHT_PROXIMITY_UUID, null, null, null);
	private BRTBeaconManager beaconManager;
	private BRTLeDeviceListAdapter adapter;
	
	private ArrayList<BeaconModel> modelList;
	
    /* native method */
    public static native String sayHello(); 
    public static native double[] calculatePosition(double[] dataIn);

	/*
	 * this is used to load the 'libtriangulation' library on application
	 */
	static {
		System.loadLibrary("BRTBeaconManagerListBeaconsActivity");
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		adapter = new BRTLeDeviceListAdapter(this);
		ListView list = (ListView) findViewById(R.id.device_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(createOnItemClickListener());
		// 创建BRTBeaconManager对象
		beaconManager = new BRTBeaconManager(this);
		
		initBeacon();
		
		final TextView showPoint=(TextView) findViewById(R.id.showPoint);
		
		
		
		
		// 回调扫描结果
		beaconManager.setRangingListener(new RangingListener() {

			@Override
			public void onBeaconsDiscovered(final RangingResult rangingResult) {

				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						

						getActionBar().setSubtitle("附近Beacon个数: " + rangingResult.beacons.size());
						int size=rangingResult.beacons.size();
						
						List<BRTBeacon> beacons = rangingResult.beacons;
						ArrayList<BeaconModel> arr=new ArrayList<BeaconModel>();
						if(size>=3){
//							 double[] dataIn = new double[]{1.0, 1.0, 1, 3.0, 1.0, 1, 2, 4, 3.0};
						
							for (BRTBeacon beacon : beacons) {
								for (BeaconModel model : modelList) {
									if(model.getName().equals(beacon.getName())){
										model.setDistance(Utils.computeAccuracy(beacon));
										arr.add(model);
									}
								}
								
								if(arr.size()==3){
									break;
								}
							}
						}
						if(arr.size()==3){
							 double[] dataIn = new double[]{arr.get(0).getX(), arr.get(0).getY(), arr.get(0).getDistance(),
									 arr.get(1).getX(), arr.get(1).getY(), arr.get(1).getDistance(),
									 arr.get(2).getX(), arr.get(2).getY(), arr.get(2).getDistance()};
						     double[] dataOut = calculatePosition(dataIn);        
						     showPoint.setText(String.format("%.2f  ，  %.2f", dataOut[0],dataOut[1]));
						    		 
//						    		 dataOut[0]+","+dataOut[1]);
						}

						adapter.replaceWith(rangingResult.beacons);
					}
				});
			}

		});
	}

	private void initBeacon() {
		// TODO Auto-generated method stub
		
		modelList=new ArrayList<BeaconModel>();
		
		BeaconModel model0=new BeaconModel("V151906",0,0);
		BeaconModel model1=new BeaconModel("V151902",2,2);
		BeaconModel model2=new BeaconModel("V151893",0,2);
		BeaconModel model3=new BeaconModel("V151905",2,0);
		modelList.add(model0);
		modelList.add(model1);
		modelList.add(model2);
		modelList.add(model3);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.scan_menu, menu);
		MenuItem refreshItem = menu.findItem(R.id.refresh);
		refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			beaconManager.disconnect();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// 关闭扫描服务
		beaconManager.disconnect();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// 检查是否支持蓝牙低功耗
		if (!beaconManager.hasBluetoothle()) {
			Toast.makeText(this, "该设备没有BLE,不支持本软件.", Toast.LENGTH_LONG).show();
			return;
		}

		// 如果未打开蓝牙，则请求打开蓝牙。
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}

	@Override
	protected void onStop() {
		try {
			// 停止扫描
			beaconManager.stopRanging(ALL_BRIGHT_BEACONS_REGION);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// 关闭扫描服务
		beaconManager.disconnect();
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "设备蓝牙未打开", Toast.LENGTH_LONG).show();
				getActionBar().setSubtitle("设备蓝牙未打开");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void connectToService() {
		getActionBar().setSubtitle("扫描中...");
		adapter.replaceWith(Collections.<BRTBeacon> emptyList());
		// 扫描之前先建立扫描服务
		beaconManager.connect(new ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					// 开始扫描
					beaconManager.startRanging(ALL_BRIGHT_BEACONS_REGION);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private AdapterView.OnItemClickListener createOnItemClickListener() {
		return new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY) != null) {

					try {
						Class<?> clazz = Class.forName(getIntent().getStringExtra(EXTRAS_TARGET_ACTIVITY));
						Intent intent = new Intent(BRTBeaconManagerListBeaconsActivity.this, clazz);
						intent.putExtra(EXTRAS_BEACON, adapter.getItem(position));
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						Log.e(TAG, "Finding class by name failed", e);
					}
				}
			}
		};
	}

}
