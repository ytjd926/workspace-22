/**
 * Date:2013-9-25
 * Author:Ryze
 * todo:webservice���ýӿ�
 */
package com.example.bt;

public class LoadMoreLoader extends BaseInfoLoader {
	public LoadMoreLoader() {
		
	}

	public void doRequest(String service, String function,
			LoaderListener listener, String... bodyArgs) throws Exception {
		this.setHeadJson(service, function);
		this.setParams(bodyArgs);
		this.addLoaderListener(listener);
		this.request();
	}

	
	public void doRequestJSON(String service, String function,
			LoaderListener listener, String... bodyArgs) throws Exception {
		this.setHeadJson(service, function);
		this.setParamsJSON(bodyArgs);
		this.addLoaderListener(listener);
		this.request();
	}
	
	public void doRequestChild(String service, String function,
			LoaderListener listener, String... bodyArgs) throws Exception {
		this.setHeadChildJson(service, function);
		this.setParams(bodyArgs);
		this.addLoaderListener(listener);
		this.request();
	}

	
	/**
	 * 队列机制  接口
	 * @param service
	 * @param function
	 * @param listener
	 * @param params
	 * @throws Exception
	 */
	public void doRequest(String service, String function,
			LoaderListener listener, Object params) throws Exception {
		this.setHeadJson(service, function);
		this.setParam(params);
		this.addLoaderListener(listener);
		this.request();
	}

}
