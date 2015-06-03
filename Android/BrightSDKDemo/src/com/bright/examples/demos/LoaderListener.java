/**
 * Date:2013-9-25
 * Author:Ryze
 * todo: webservice����ӿ�
 */
package com.bright.examples.demos;

public interface LoaderListener {
    public void onSuccess(String response) ;  
    
    public void onStart() ;
    
    public void onFinish() ;
    
    public void onFailure(int status, String msg);

    public void onConnectFailure(int status,String msg);

}
