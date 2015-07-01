package us.zm.sdkexample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingEvent;
import us.zoom.sdk.MeetingOptions;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.ZoomSDK;
import us.zm.sdkexample.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class MainActivity extends Activity implements Constants,
		MeetingServiceListener {
	static Logs log = new Logs("us.zm.sdkexample.MainActivity",
			Logs.DebugType.D);

	private EditText mEdtMeetingNo;

	private final static int STYPE = MeetingService.USER_TYPE_ZOOM;
	// private final static String DISPLAY_NAME = "";

	public static String user_id = "";

	public static String token = "";

	public static String user_name = "";

	public static String meet_num = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		new Thread(new Runnable() {

			@Override
			public void run() {
				httpWithPost();
			}
		}).start();

		mEdtMeetingNo = (EditText) findViewById(R.id.edtMeetingNo);
		ZoomSDK zoomSDK = ZoomSDK.getInstance();

		if (zoomSDK.isInitialized()) {
			MeetingService meetingService = zoomSDK.getMeetingService();
			meetingService.addListener(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mEdtMeetingNo.setText(meet_num);
	}

	@Override
	protected void onDestroy() {
		ZoomSDK zoomSDK = ZoomSDK.getInstance();

		if (zoomSDK.isInitialized()) {
			MeetingService meetingService = zoomSDK.getMeetingService();
			meetingService.removeListener(this);
		}

		super.onDestroy();
	}

	public void onClickBtnJoinMeeting(View view) {
		mEdtMeetingNo.setText(meet_num);
		String meetingNo = mEdtMeetingNo.getText().toString().trim();

		if (meetingNo.length() == 0) {
			Toast.makeText(
					this,
					"You need to enter a meeting number which you want to join.",
					Toast.LENGTH_LONG).show();
			return;
		}

		ZoomSDK zoomSDK = ZoomSDK.getInstance();

		if (!zoomSDK.isInitialized()) {
			Toast.makeText(this, "SDK has not been initialized successfully",
					Toast.LENGTH_LONG).show();
			return;
		}

		MeetingService meetingService = zoomSDK.getMeetingService();

		MeetingOptions opts = new MeetingOptions();
		opts.no_driving_mode = true;
		// opts.no_invite = true;
		// opts.no_meeting_end_message = true;
		// opts.no_titlebar = true;
		// opts.no_bottom_toolbar = true;

		int ret = meetingService.joinMeeting(this, meet_num, user_name, opts);

		log.D("onClickBtnJoinMeeting, ret=" + ret + " meet_num==" + meet_num);
	}

	public void onClickBtnStartMeeting(View view) {
		mEdtMeetingNo.setText(meet_num);
		String meetingNo = mEdtMeetingNo.getText().toString().trim();

		if (meetingNo.length() == 0) {
			Toast.makeText(this,
					"You need to enter a scheduled meeting number.",
					Toast.LENGTH_LONG).show();
			return;
		}

		ZoomSDK zoomSDK = ZoomSDK.getInstance();

		if (!zoomSDK.isInitialized()) {
			Toast.makeText(this, "SDK has not been initialized successfully",
					Toast.LENGTH_LONG).show();
			return;
		}

		MeetingService meetingService = zoomSDK.getMeetingService();

		MeetingOptions opts = new MeetingOptions();
		opts.no_driving_mode = true;

		int ret = meetingService.startMeeting(this, user_id, token, STYPE,
				meet_num, user_name, opts);

		log.D("onClickBtnStartMeeting, ret=" + ret + " meet_num==" + meet_num);
	}

	public void onClickBtnStartInstantMeeting(View view) {

		ZoomSDK zoomSDK = ZoomSDK.getInstance();

		if (!zoomSDK.isInitialized()) {
			Toast.makeText(this, "SDK has not been initialized successfully",
					Toast.LENGTH_LONG).show();
			return;
		}

		MeetingService meetingService = zoomSDK.getMeetingService();

		MeetingOptions opts = new MeetingOptions();
		opts.no_driving_mode = true;

		int ret = meetingService.startInstantMeeting(this, user_id, token,
				STYPE, user_name, opts);

		log.D("onClickBtnStartInstantMeeting, ret=" + ret);
	}

	@Override
	public void onMeetingEvent(int meetingEvent, int errorCode,
			int internalErrorCode) {

		if (meetingEvent == MeetingEvent.MEETING_CONNECT_FAILED
				&& errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
			Toast.makeText(this, "Version of SDK is too low!",
					Toast.LENGTH_LONG).show();
		}
	}

	public static void httpWithPost() {
		/* 建立HTTP Post连线 */
		String url = "https://api.zhumu.me/v1/user/get";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("api_key", API_KEY));
		params.add(new BasicNameValuePair("api_secret", API_SECRET));
		params.add(new BasicNameValuePair("logintype", "3"));
		params.add(new BasicNameValuePair("loginname", "SDKTest@zhumu.me"));
		try {

			// 发出HTTP request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得HTTP response
			 HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
//			HttpResponse httpResponse = getNewHttpClient().execute(httpRequest);

			log.D("httpWithPost()  error==="
					+ httpResponse.getStatusLine().getStatusCode());
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取出回应字串
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				log.D("httpWithPost()  strResult=" + strResult);

				// {"code":100,"zcode":16457481,"id":"wqljsh1JSGGL1ji_40UarQ","username":"???","mobile":null,"usertype":0,"det":null,
				// "createtime":"\/Date(1430378900000)\/","createby":"fulq@yonyou.com","pmi":"15449593466","role":0,
				// "email":"fulq@yonyou.com","isowner":0,"token":"qmzg0uXIeSS69lCTo8UTQdjVh8EtVvi_z-DVGXEPFHM.BgIgMVZRc3VBSW9nTFVPVjRpZnY4aC9id2xrMTVBNm45OWNANTZhODFiNjE5NWFlOWQ5YTc2NTYzOTdmNGYxZjk3YWUxNjcwMmUwODk1MDEyOWNlNjNiNDViMmM3N2FkZDJlZgA"}
				JSONObject jsonObject = new JSONObject(strResult.toString());
				String code = jsonObject.getString("code");
				String zcode = jsonObject.getString("zcode");
				String id = jsonObject.getString("id");// User_id

				String username = jsonObject.getString("username");
				String mobile = jsonObject.getString("mobile");
				String usertype = jsonObject.getString("usertype");
				String det = jsonObject.getString("det");
				String createtime = jsonObject.getString("createtime");
				String createby = jsonObject.getString("createby");

				String pmi = jsonObject.getString("pmi");
				String role = jsonObject.getString("role");
				String email = jsonObject.getString("email");
				String isowner = jsonObject.getString("isowner");
				String token = jsonObject.getString("token");

				MainActivity.user_id = id;
				MainActivity.token = token;
				MainActivity.user_name = username;
				meet_num = pmi;
				log.D("httpWithPost():--id=" + id + "token=" + token);
			} else {
				log.D("httpWithPost()  error =");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
}
