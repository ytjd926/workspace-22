package com.example.bt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.achartengine.chartdemo.demo.chart.AverageCubicTemperatureChart;
import org.achartengine.chartdemo.demo.chart.AverageTemperatureChart;
import org.achartengine.chartdemo.demo.chart.BudgetDoughnutChart;
import org.achartengine.chartdemo.demo.chart.BudgetPieChart;
import org.achartengine.chartdemo.demo.chart.CombinedTemperatureChart;
import org.achartengine.chartdemo.demo.chart.IDemoChart;
import org.achartengine.chartdemo.demo.chart.MultipleTemperatureChart;
import org.achartengine.chartdemo.demo.chart.ProjectStatusBubbleChart;
import org.achartengine.chartdemo.demo.chart.ProjectStatusChart;
import org.achartengine.chartdemo.demo.chart.SalesBarChart;
import org.achartengine.chartdemo.demo.chart.SalesComparisonChart;
import org.achartengine.chartdemo.demo.chart.SalesGrowthChart;
import org.achartengine.chartdemo.demo.chart.SalesStackedBarChart;
import org.achartengine.chartdemo.demo.chart.ScatterChart;
import org.achartengine.chartdemo.demo.chart.SensorValuesChart;
import org.achartengine.chartdemo.demo.chart.TemperatureChart;
import org.achartengine.chartdemo.demo.chart.TrigonometricFunctionsChart;
import org.achartengine.chartdemo.demo.chart.WeightDialChart;

import com.ian.missionhills.localstorage.dao.YuLiangDAO;
import com.ian.missionhills.localstorage.model.DateUtil;
import com.ian.missionhills.localstorage.model.YuLiang;

import android.R.integer;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {

	private BluetoothAdapter mBluetoothAdapter = null;      
    
	private BluetoothSocket btSocket = null;      
	      
	private OutputStream outStream = null;      
	      
	private InputStream inStream = null;      
	      
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  //��������������ͨ�õ�UUID����Ҫ���      
	      
	private static String address = "20:14:05:06:13:95"; // <==Ҫ���ӵ������豸MAC��ַ      
	TextView textView1;
	EditText editView1;
	TextView textView4;
	TextView textview6;
	private YuLiangDAO dao;
	private YuLiang yuLiang ;
	
	 private IDemoChart[] mCharts = new IDemoChart[] {  new CombinedTemperatureChart()};
    @Override
    protected void onCreate(Bundle savedInstanceState) {	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1  =(TextView)findViewById(R.id.textView1);//
        editView1=(EditText)findViewById(R.id.editView1);//
        textView4=(TextView)findViewById(R.id.textView4);//总雨量
        textview6=(TextView)findViewById(R.id.textView6);//分时雨量
        Button btn = (Button) this.findViewById(R.id.button1);//获取数据
        Button btn2 = (Button) this.findViewById(R.id.button2);//显示柱状图
        Button btn3 = (Button) this.findViewById(R.id.button3);//显示柱状图
        btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), Main.class);
				startActivity(intent);
			}
		});
        btn3.setOnClickListener(this);
        btn.setOnClickListener(this);
        dao=YuLiangDAO.getInstance();
        yuLiang=new YuLiang();
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			/*1:��ȡ����BlueToothAdapter*/      
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      
			if(mBluetoothAdapter == null)       
			{      
			    Toast.makeText(this, "Bluetooth is not available.", Toast.LENGTH_LONG).show();      
			    finish();      
			    return;      
			}      
			if(!mBluetoothAdapter.isEnabled())       
			{      
			    Toast.makeText(this, "Please enable your Bluetooth and re-run this program.", Toast.LENGTH_LONG).show();      
			    finish();      
			    return;      
			}       
			       
			/*2:��ȡԶ��BlueToothDevice*/       
			    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);      
			if(mBluetoothAdapter == null)       
			{      
			    Toast.makeText(this, "Can't get remote device.", Toast.LENGTH_LONG).show();      
			    finish();      
			    return;      
			}      
			       
			/*3:���Socket*/            
			    try {      
			    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);      
			} catch (IOException e) {      
			      
			    Log.e("TAG", "ON RESUME: Socket creation failed.", e);      
			      
			}      
			      
			/*4:ȡ��discovered��ʡ��Դ*/      
			mBluetoothAdapter.cancelDiscovery();              
			      
			      
			/*5:����*/      
			      
			try {      
			      
			    btSocket.connect();      
			      
			    Log.e("TAG", "ON RESUME: BT connection established, data transfer link open.");      
			      
			} catch (IOException e) {      
				Log.e("TAG", e.getMessage());    
			    try {      
			        btSocket.close();      
			      
			    } catch (IOException e2) {      
			      
			        Log .e("TAG","ON RESUME: Unable to close socket during connection failure", e2);      
			    }      
			}    

			/*��ʱ����ͨ���ˣ��������⺯����*/      
			try {   
			outStream = btSocket.getOutputStream();   
			  
			inStream = btSocket.getInputStream(); 
			  
			} catch (IOException e) {   
			    Log.e("TAG", "ON RESUME: Output stream creation failed.", e);   
			}   
			   
			   
			//String message = "1";   
			   
			byte[] msgBuffer = hexStringToBytes("010300000002C40B");
			   
			try {   
			    outStream.write(msgBuffer);   
			   
			} catch (IOException e) {   
			    Log.e("TAG", "ON RESUME: Exception during write.", e);   
			}   
			byte[] buffer = new byte[100];
			try {
				int count = inStream.read(buffer);
				Log.i("TAG","" + count);   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			String var="01030400040000BBF2";
			String var=buffer.toString();
			String  jsz1=var.substring(6, 10);
			String  jsz2=var.substring(10, 14);
			long  result=Long.parseLong(jsz2+jsz1, 16);//16����ת��Ϊ10����
			textView1.setText(result+"");
			long i=1;
			if(!editView1.getText().equals(""))
			{
				i=Long.parseLong(editView1.getText()+"");//ϵ��ת��
			}
			if(!"".equals(result) && result!=0)
			{
				textView4.setText((i*result)+"");
				yuLiang.setZyl(Long.parseLong(textView4.getText()+""));
				Date pushTime = new Date();
				yuLiang.setTime(DateUtil.getDateTimeStr(pushTime,"yyyy-MM-dd HH:mm"));
				yuLiang.setDate(DateUtil.getDateTimeStr(pushTime,"yyyy-MM-dd"));
				List<YuLiang> list=dao.queryByParamAscOrder("time",true,"date",DateUtil.getDateTimeStr(pushTime,"yyyy-MM-dd"));
//				list=dao.queryAll();
				//计算分时雨量的问题
				if(list.size()>0)
				{
					yuLiang.setFsyl(Long.parseLong(textView4.getText()+"")-list.get(0).getFsyl());
					textview6.setText(Long.parseLong(textView4.getText()+"")-list.get(0).getFsyl()+"");
				}else {
					yuLiang.setFsyl(Long.parseLong(textView4.getText()+""));
					textview6.setText(textView4.getText()+"");
				}
				dao.save(yuLiang);
				Log.e("数据===", list.size()+"");
			}
			break;
		case R.id.button3:
			Intent intent=new Intent();
			intent= mCharts[0].execute(this);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	public byte[] hexStringToBytes(String hexString) {  
		if (hexString == null || hexString.equals("")) {  
		       return null;  
		    }  
		    hexString = hexString.toUpperCase();  
		    int length = hexString.length() / 2;  
		    char[] hexChars = hexString.toCharArray();  
		    byte[] d = new byte[length];  
		    for (int i = 0; i < length; i++) {  
		        int pos = i * 2;  
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		    }  
		    return d;  
	}  

	private byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
