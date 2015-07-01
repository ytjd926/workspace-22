package us.zm.sdkexample;

import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitializeListener;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

public class ExampleApplication extends Application implements Constants, ZoomSDKInitializeListener {
	
	private final static String TAG = ExampleApplication.class.getSimpleName();
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ZoomSDK sdk = ZoomSDK.getInstance();
		sdk.initialize(this, APP_KEY, APP_SECRET, WEB_DOMAIN, this);
		sdk.setDropBoxAppKeyPair(this, DROPBOX_APP_KEY, DROPBOX_APP_SECRET);
		sdk.setOneDriveClientId(this, ONEDRIVE_CLIENT_ID);
	}

	@Override
	public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
		Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);
		
		if(errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
			Toast.makeText(this, "Failed to initialize SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
		}
	}
	
}
