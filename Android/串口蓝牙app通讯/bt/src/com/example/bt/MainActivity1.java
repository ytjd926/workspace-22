package com.example.bt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity1 extends Activity {
	private String tag = "MainActivity1";
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private static String macAddress = "20:14:05:06:13:95"; // 目标蓝牙mac地址

	private BluetoothAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		initBluetooth();
	}

	private void initBluetooth() {
		// TODO Auto-generated method stub
		adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			// 设备不支持蓝牙
			Log.i(tag, "设备不支持蓝牙，程序退出");
			this.finish();
		}
		// 打开蓝牙
		if (!adapter.isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 设置蓝牙可见性，最多300秒
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			MainActivity1.this.startActivity(intent);
		}

		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		// for(int i=0; i<devices.size(); i++)
		// {
		// BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
		// System.out.println(device.getName()+"  -biao-   "+device.getAddress());
		// }

		Iterator it = devices.iterator(); // 获得一个迭代子
		BluetoothDevice findDevice = null;
		while (it.hasNext()) {
			BluetoothDevice device = (BluetoothDevice) it.next(); // 得到下一个元素
			if (device.getAddress().equals(macAddress)) {
				findDevice = device;
				break;
			}
		}
		if (findDevice != null) {
			int connectState = findDevice.getBondState();
			switch (connectState) {
			// 未配对
			case BluetoothDevice.BOND_NONE:
				// 配对
				try {
					Method createBondMethod = BluetoothDevice.class
							.getMethod("createBond");
					createBondMethod.invoke(findDevice);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// 已配对
			case BluetoothDevice.BOND_BONDED:
				try {
					// 连接
					connect(findDevice);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}

	}

	// 蓝牙连接
	private void connect(BluetoothDevice device) throws IOException {
		// TODO Auto-generated method stub
		ConnectThread connect=new ConnectThread(device, adapter);
		connect.start();
		
	}

	public class ConnectThread extends Thread {
		BluetoothAdapter mAdapter;
		BluetoothSocket mySocket;
		ReceiveDatas connectBluetooth;
		OutputStream mmOutStream;

		// 此处变量略

		public ConnectThread(BluetoothDevice device, BluetoothAdapter mAdapterr) {
			this.mAdapter = mAdapterr;
			int sdk = Build.VERSION.SDK_INT;
			if (sdk >= 10) {
				try {
					mySocket = device
							.createInsecureRfcommSocketToServiceRecord(MY_UUID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					mySocket = device
							.createRfcommSocketToServiceRecord(MY_UUID);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		public void run() {
			//
			mAdapter.cancelDiscovery();
			try {
				mySocket.connect();
				// 启动接收远程设备发送过来的数据
				connectBluetooth = new ReceiveDatas(mySocket);
				connectBluetooth.start();
				// 输出流
				mmOutStream = mySocket.getOutputStream();
				
				sendMessage("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block

				try {
					mySocket.close();
				} catch (IOException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}

			}

		}

		// 写数据
		/* Call this from the main Activity to send data to the remote device */
		public void sendMessage(String msg) {
			byte[] buffer = new byte[16];
			try {
				if (mmOutStream == null) {
					Log.i("info", "输出流为空");
					return;
				}
				// 写数据
				buffer = msg.getBytes();
				mmOutStream.write(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (mmOutStream != null) {
						mmOutStream.flush();

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public class ReceiveDatas extends Thread {
		// 变量 略过
		BluetoothSocket mmSocket;
		InputStream mmInStream;

		// 构造方法
		public ReceiveDatas(BluetoothSocket socket) {

			this.mmSocket = socket;
			InputStream tempIn = null;

			// 获取输入流
			try {
				tempIn = socket.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			mmInStream = tempIn;
		}

		@Override
		public void run() {
			int bytes=-1;// 返回读取到的数据
			// 监听输入流
			int count = 0;
			   try {
				count = mmInStream.available();



            byte[] buffer = new byte[1024];// 缓冲数据流

			while ((bytes=mmInStream.read(buffer))!=-1) {
				try {
//					bytes = mmInStream.read(buffer);
//                    StringBuffer outBuffer = new StringBuffer();
                    byte[] buffer1 = new byte[bytes];
                    for (int i = 0; i < bytes; i++) {
//                        buffer1.append(buffer[i]);
                        buffer1[i] = buffer[i];
                    }
                    String receiveData = new String(buffer1, "UTF-8");
                    Log.e("TAG", "         receiveData="
                            + receiveData);
					// 此处处理数据……
					System.out.println("bytes=" + bytes);

				} catch (IOException e) {
					try {
						if (mmInStream != null) {
							mmInStream.close();
						}
						Log.i("info", "异常");
//						break;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				try {
//					Thread.sleep(50);// 延迟
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
               } catch (IOException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
		}

	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onStop() {
		this.finish();
		super.onStop();
	}

}
