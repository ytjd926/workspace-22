package com.example.bt;

import java.util.Properties;

/**
 * Description:
 * @date 2014-2-10
 * @author Ryze
 * @version 1.0
 */
public class RequestProperties {
	public static Properties p = null;
	public static String IP="";
	public static String PORT="";
	public static String CHILDIP="";
	public static String CHILDPORT="";
    public static String MHM_EWOTA_IP="";
    public static String MALL_EWOTA_IP="";
    public static String MHM_EWOTA_PORT="";
    public static String MALL_EWOTA_PORT="";


	static {
		init();
	}
	public static void init() {
		try {
			p = new java.util.Properties();
			java.io.InputStream in=BaseAPP.getInstance().getResources().openRawResource(R.raw.request);
			if (in == null) {
				return;
			}
			p.load(in);
			in.close();
			IP = p.getProperty("IP","192.168.0.134");//默认取
			PORT = p.getProperty("PORT","8080");

			
			} catch (Throwable e) {
				e.printStackTrace();
			}
	}
	public static String getURL(){
		if(IP.equals("")){
			RequestProperties.init();
		}
		return "http://"+IP+":"+PORT;
	}
	
	public static String getChildURL(){
		if(CHILDIP.equals("")){
			RequestProperties.init();
		}
		return "http://"+CHILDIP+":"+CHILDPORT;
	}

    public static String getEWOTAURL(){
        if(MHM_EWOTA_IP.equals("")){
            RequestProperties.init();
        }
        return "http://"+MHM_EWOTA_IP+":"+MHM_EWOTA_PORT;
    }

    public static String getMALLEWOTAURL(){
        if(MALL_EWOTA_IP.equals("")){
            RequestProperties.init();
        }
        return "http://"+MALL_EWOTA_IP+":"+MALL_EWOTA_PORT;
    }
}
