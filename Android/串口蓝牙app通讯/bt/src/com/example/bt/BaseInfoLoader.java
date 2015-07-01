/**
 * Date:2013-9-25
 * Author:Ryze
 * todo: webservice调用的实现类   
 * （待完善）
 */
package com.example.bt;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public abstract class BaseInfoLoader implements InfoLoader {

	private String tag = "com.example.bt.BaseInfoLoader";
	private LoaderListener listener;
	private String METHOD_NAME;
	private String SOAP_ACTION;
	private String NAMESPACE;

	/**
	 * webservice接口相关
	 */

	@Override
	public void addLoaderListener(LoaderListener listener) {
		this.listener = listener;
	}

	@Override
	public void setHeadJson(String service, String function) {
		METHOD_NAME = function;
		NAMESPACE = RequestProperties.getURL()+service;
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
	}
	
	public void setHeadChildJson(String service, String function) {
		METHOD_NAME = function;
		NAMESPACE = RequestProperties.getChildURL()+service;
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
	}

	@Override
	public void request() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				doRequest();
			}
		}).start();
	}
	/**
	 * 同步请求
	 */
	private void doRequest() {
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()        
//        .detectDiskReads()        
//        .detectDiskWrites()        
//        .detectNetwork()   // or .detectAll() for all detectable problems        
//        .penaltyLog()        
//        .build());        
//                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()        
//        .detectLeakedSqlLiteObjects()     
//        .penaltyLog()        
//        .penaltyDeath()        
//        .build());  
                
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(getParams());
		DebugUtil.showLog(params.toString());
		BaseInfoLoader.this.onStart();
//		Log.e("BaseInfoLoader", "SOAP_ACTION="+SOAP_ACTION);
		//数字10000是请求超时的时间
//		HttpTransportSE androidHttpTransport = new HttpTransportSE(NAMESPACE,10000);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(NAMESPACE);
		SoapPrimitive resultsRequestSOAP = null;
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
			// 这里可加一些通用的数据解析等操作
			BaseInfoLoader.this.onSuccess(resultsRequestSOAP.toString());
		}catch (ConnectException e){
            e.printStackTrace();
            BaseInfoLoader.this.onConnectFailure(NetConnectionConst.ERROR_NETCONNECTION,e.getMessage());
        }catch(SocketTimeoutException e){
            e.printStackTrace();
            BaseInfoLoader.this.onConnectFailure(NetConnectionConst.ERROR_NETCONNECTION,e.getMessage());
        }catch (Exception e) {
			// TODO 调用webservice接口异常
			e.printStackTrace();
			BaseInfoLoader.this.onFailure(NetConnectionConst.ONFAIL,
					"请求超时");
		} finally {
            try {
                androidHttpTransport.getServiceConnection().disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
            	System.gc();
                envelope=null;
                androidHttpTransport.reset();
                androidHttpTransport = null;
                resultsRequestSOAP=null;
                BaseInfoLoader.this.onFinish();
               
            }
		}
	}

	private Object params;

	@Override
	public void setParams(String... arg) {
		params = new SoapObject(NAMESPACE, METHOD_NAME);
		if (arg == null) {
			return;
		}
		if (arg.length % 2 != 0) {
			throw new IllegalArgumentException();
		}

		for (int i = 0, lenth = arg.length / 2; i < lenth; ++i) {
			((SoapObject) params).addProperty(arg[2 * i], arg[2 * i + 1]);
		}
	}

	@Override
	public void setParam(Object params) {
//	Object j=new String("33333333");
//	
//		try {
			this.params =params;
//			this.params =new D(params).clone();
//			String temp=this.params.toString();
//			System.out.println("----------");
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public Object getParams() {
		return params == null ? new Object() : params;
	}

	@Override
	public void setParamsJSON(String... arg) throws JSONException {
		if (arg == null) {
			return;
		}

		if (arg.length % 2 != 0)
			throw new IllegalArgumentException();
		params = new JSONObject();
		for (int i = 0, lenth = arg.length / 2; i < lenth; ++i) {
			((JSONObject) params).put(arg[2 * i], arg[2 * i + 1]);
		}
	}

	protected void onStart() {
		if (listener != null)
			listener.onStart();
	}

	protected void onFinish() {
		if (listener != null)
			listener.onFinish();

	}

	protected void onFailure(int status, String msg) {
		if (listener != null)
			listener.onFailure(status, msg);
	}

	protected void onSuccess(String responseString) {
		if (listener != null)
			listener.onSuccess(responseString);
	}

    protected  void onConnectFailure(int status,String msg){
        if(listener!=null)
            listener.onConnectFailure(status,msg);
    }
	
	class D implements Cloneable{//实现Cloneable接口
		D(Object params){
		}
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// 实现clone方法
			return super.clone();
		}
	}

}
