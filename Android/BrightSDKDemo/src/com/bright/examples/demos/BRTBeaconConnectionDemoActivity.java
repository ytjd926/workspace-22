package com.bright.examples.demos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.brtbeacon.sdk.BRTBeaconConfig;
import com.brtbeacon.sdk.BRTBeaconPower;
import com.brtbeacon.sdk.BRTThrowable;
import com.brtbeacon.sdk.connection.BRTBeaconConnection;
import com.brtbeacon.sdk.connection.BeaconCharacteristics;
import com.brtbeacon.sdk.connection.ConnectionCallback;
import com.brtbeacon.sdk.connection.WriteCallback;

/**
 * 演示连接Beacon，读写相关特征
 */
public class BRTBeaconConnectionDemoActivity extends Activity {

	private BRTBeacon beacon;
	private BRTBeaconConnection connection;
	private TextView statusView;
	private TextView beaconDetailsView;
	private EditText intervalEditView;
	private EditText minorEditView;
	private EditText majorEditView;
	private EditText measuredPowerEditView;
	private EditText nameEditView;
	private EditText uuidEditView;
	private TextView txTextView;
	private View afterConnectedView;
	private RadioGroup radioGroup;
	private Switch modeSwitch;// 开发模式
	private ProgressDialog dialogAlert;
	private BRTBeaconPower txpower;
	private RadioButton radioButtondbm23;
	private RadioButton radioButtondbm6;
	private RadioButton radioButtondbm0;
	private RadioButton radioButtondbm4;
	private int radiobm23;
	private int radiodbm6;
	private int radiodbm0;
	private int radiodbm4;
	private int pMode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.characteristics_demo);
		beacon = getIntent().getParcelableExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_BEACON);
		init();
		connection = new BRTBeaconConnection(this, beacon, createConnectionCallback());
	}

	private void init() {
		dialogAlert = new ProgressDialog(this);
		statusView = (TextView) findViewById(R.id.status);
		beaconDetailsView = (TextView) findViewById(R.id.beacon_details);
		afterConnectedView = findViewById(R.id.after_connected);
		minorEditView = (EditText) findViewById(R.id.minor);
		majorEditView = (EditText) findViewById(R.id.major);
		measuredPowerEditView = (EditText) findViewById(R.id.power);
		nameEditView = (EditText) findViewById(R.id.name);
		intervalEditView = (EditText) findViewById(R.id.interval);
		uuidEditView = (EditText) findViewById(R.id.uuid);
		txTextView = (TextView) findViewById(R.id.dbm);

		findViewById(R.id.btn_update).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setCharacteristics();
			}
		});
		findViewById(R.id.btn_temperature).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (connection.isConnected()) {
					connection.getTemperature();
				}
			}
		});
		radioButtondbm23 = (RadioButton) findViewById(R.id.update_dbm23);
		radioButtondbm6 = (RadioButton) findViewById(R.id.update_dbm6);
		radioButtondbm0 = (RadioButton) findViewById(R.id.update_dbm0);
		radioButtondbm4 = (RadioButton) findViewById(R.id.update_dbm4);
		radiobm23 = radioButtondbm23.getId();
		radiodbm6 = radioButtondbm6.getId();
		radiodbm0 = radioButtondbm0.getId();
		radiodbm4 = radioButtondbm4.getId();
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == radiobm23) {
					txpower = BRTBeaconPower.BRTBeaconPowerLevelMinus23;
				} else if (checkedId == radiodbm6) {
					txpower = BRTBeaconPower.BRTBeaconPowerLevelMinus6;
				} else if (checkedId == radiodbm0) {
					txpower = BRTBeaconPower.BRTBeaconPowerLevelDefault;
				} else if (checkedId == radiodbm4) {
					txpower = BRTBeaconPower.BRTBeaconPowerLevelPlus4;
				}
			}
		});
		modeSwitch = (Switch) findViewById(R.id.mode);
		modeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					pMode = 1;
				} else {
					pMode = 0;
				}
			}
		});

	}

	/**
	 * 配置Beacon
	 */
	private void setCharacteristics() {
		showDialog("配置...");
		BRTBeaconConfig configBeacon = new BRTBeaconConfig();
		configBeacon.setName(nameEditView.getText().toString());
		configBeacon.setUuid(uuidEditView.getText().toString());
		configBeacon.setMajor(Integer.parseInt(majorEditView.getText().toString()));
		configBeacon.setMinor(Integer.parseInt(minorEditView.getText().toString()));
		configBeacon.setAdIntervalMillis(Integer.parseInt(intervalEditView.getText().toString()));
		configBeacon.setTxPower(BRTBeaconConnectionDemoActivity.this.txpower);
		configBeacon.setMeasuredPower(Integer.parseInt(measuredPowerEditView.getText().toString()));
		configBeacon.setdevolMode(pMode);
		connection.setBeaconCharacteristic(configBeacon, new WriteCallback() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						cancelDialog();
						showToast("操作成功");
					}
				});

			}

			@Override
			public void onError(BRTThrowable throwable) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						cancelDialog();
						showToast("操作失败");
					}
				});

			}
		});
	}

	private String matchtxvalue(BRTBeaconPower tx) {
		String txvalue = "BRTBeaconPowerLevelDefault";
		switch (tx) {
			case BRTBeaconPowerLevelMinus23:
				txvalue = "BRTBeaconPowerLevelMinus23";
				break;
			case BRTBeaconPowerLevelMinus6:
				txvalue = "BRTBeaconPowerLevelMinus6";
				break;
			case BRTBeaconPowerLevelDefault:
				txvalue = "BRTBeaconPowerLevelDefault";
				break;
			case BRTBeaconPowerLevelPlus4:
				txvalue = "BRTBeaconPowerLevelPlus4";
				break;
		}
		return txvalue;
	}

	private void cancelDialog() {
		if (dialogAlert != null && dialogAlert.isShowing()) {
			dialogAlert.cancel();
		}
	}

	private void showDialog(String message) {
		dialogAlert.setMessage(message);
		if (dialogAlert != null && !dialogAlert.isShowing()) {
			dialogAlert.show();
		}
	}

	private com.brtbeacon.sdk.connection.ConnectionCallback createConnectionCallback() {
		return new ConnectionCallback() {

			@Override
			public void onAuthenticated(final BeaconCharacteristics beaconChars) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						statusView.setText("状态: 连接成功");
						StringBuilder sb = new StringBuilder().append("电量: ").append(beaconChars.getBattery())
								.append("%\n").append("温度: ").append(beaconChars.getTemperature()).append("℃")
								.append("\n固件版本: ").append(beaconChars.getVersion()).append("\n");
						beaconDetailsView.setText(sb.toString());
						cancelDialog();
						majorEditView.setText(String.valueOf(beaconChars.getMajor()));
						minorEditView.setText(String.valueOf(beaconChars.getMinor()));
						uuidEditView.setText(String.valueOf(beaconChars.getUuid()));
						intervalEditView.setText(String.valueOf(beaconChars.getAdvertisingIntervalMillis()));
						measuredPowerEditView.setText(String.valueOf(beaconChars.getMeasuredPower()));
						txTextView.setText(matchtxvalue(beaconChars.getTxPower()));
						BRTBeaconConnectionDemoActivity.this.txpower = beaconChars.getTxPower();
						nameEditView.setText(new String(beaconChars.getName()));
						afterConnectedView.setVisibility(View.VISIBLE);
						pMode = beaconChars.getDevolMode();
						if (beaconChars.getDevolMode() == 1) {
							modeSwitch.setChecked(true);
						} else {
							modeSwitch.setChecked(false);
						}
						switch (beaconChars.getTxPower()) {
							case BRTBeaconPowerLevelMinus23:
								radioButtondbm23.setChecked(true);
								break;
							case BRTBeaconPowerLevelMinus6:
								radioButtondbm6.setChecked(true);
								break;
							case BRTBeaconPowerLevelDefault:
								radioButtondbm0.setChecked(true);
								break;
							case BRTBeaconPowerLevelPlus4:
								radioButtondbm4.setChecked(true);
								break;
							default:
								break;
						}
					}
				});
			}

			@Override
			public void onAuthenticationError(final BRTThrowable throwable) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						cancelDialog();
						statusView.setText("状态:"+throwable.getError());
					}
				});
			}

			@Override
			public void onDisconnected() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						cancelDialog();
						statusView.setText("状态: 连接断开");
					}
				});
			}

		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!connection.isConnected()) {
			showDialog("连接中...");
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					connection.connect();
				}
			}, 1000);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			connection.close();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		connection.close();
	}

	@Override
	protected void onStop() {
		connection.close();
		super.onStop();
	}

	private void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}
}
