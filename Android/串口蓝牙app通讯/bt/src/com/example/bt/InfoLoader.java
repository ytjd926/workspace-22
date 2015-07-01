/**
 * Date:2013-9-25
 * Author:Ryze
 * todo: 监听接口
 */
package com.example.bt;

import org.json.JSONException;

public interface InfoLoader {

	/**
	 * 添加监听
	 * 
	 * @param listener
	 */
	public void addLoaderListener(LoaderListener listener);

	/**
	 * 发出请求
	 */
	public void request();

	/**
	 * 设置参数
	 * 
	 * @param arg
	 * @throws JSONException
	 */
	void setParams(String... arg);
	
	
	public Object getParams();

	void setHeadJson(String service, String function);

	void setParamsJSON(String[] arg) throws JSONException;

	void setParam(Object params);

}
