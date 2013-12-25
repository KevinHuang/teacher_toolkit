package tw.com.ischool.oauth2signin;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.com.ischool.oauth2signin.HttpUtil.HttpListener;
import android.util.Log;

/*
 * 代表使用者資訊的 Signeton 物件
 */
public class User {
	public static final String OAUTH_USER_INFO_URL = Constant.SingIn.OAUTH_USERINFO_URL;

	private static User _user;
	
	private String mAccessToken="" ;
	private String mFirstName="";
	private String mLastName="";
	private String mUserID="";
	private String mUUID="";
	private String mSysID="";
	
	private List<APInfo> mApplications;
	

	public List<APInfo> getApplications() {
		return mApplications;
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public void setAccessToken(String accessToken) {
		mAccessToken = accessToken;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	public String getUserID() {
		return mUserID;
	}

	public void setUserID(String userID) {
		mUserID = userID;
	}

	public String getUUID() {
		return mUUID;
	}

	public void setUUID(String uUID) {
		mUUID = uUID;
	}

	public String getSysID() {
		return mSysID;
	}

	public void setSysID(String sysID) {
		mSysID = sysID;
	}

	private User() {
		mApplications = new ArrayList<APInfo>();
	}
	
	public static User get() {
		if (_user == null)
			_user = new User();
		return _user;
	}
	
	/*
	 *   取得目前帳號所能引用的 DSA Applications
	 *   */
	private void getMyApplications(String accessToken, GetApplicationInfoListener listener) {
		final GetApplicationInfoListener theListener = listener;
		HttpUtil hu = new HttpUtil();
		String targetUrl = String.format(Constant.SingIn.OAUTH_DOMAIN_LIST_URL, accessToken);
		hu.get(targetUrl, new HttpListener() {
			
			@Override
			public void onSuccess(String result) {
				Log.d("getApplicationInfo", result);
				try {
					mApplications.clear();
					JSONArray ary = new JSONArray(result);
					for(int i=0; i<ary.length(); i++) {
						JSONObject jsObj = ary.getJSONObject(i);
						APInfo ap = new APInfo(jsObj);
						mApplications.add(ap);
					}
					if (theListener != null)
						theListener.onSuccess();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					onFail(e);
				}
			}
			
			@Override
			public void onFail(Exception ex) {
				ex.printStackTrace();
				Log.e("getUserInfo", ex.getLocalizedMessage());
				if (theListener != null)
					theListener.onException(ex);
			}
		});
	}
	
	public void getUserInfo(String accessToken, GetUserInfoListener listener) {
		mAccessToken = accessToken;
		final GetUserInfoListener theListener = listener;
		HttpUtil hu = new HttpUtil();
		String targetUrl = String.format(OAUTH_USER_INFO_URL, accessToken);
		hu.get(targetUrl, new HttpListener() {
			
			@Override
			public void onSuccess(String result) {
				Log.d("getUserInfo", result);
				try {
					JSONObject jsObj = new JSONObject(result);
					mUserID = jsObj.getString("userID");
					mFirstName = jsObj.getString("firstName");
					mLastName = jsObj.getString("lastName");
					mUUID = jsObj.getString("uuid");
					
					//再取得這個帳號的 Applications 
					getMyApplications(mAccessToken, new GetApplicationInfoListener() {
						
						@Override
						public void onSuccess() {
							if (theListener != null)
								theListener.onSuccess();
						}
						
						@Override
						public void onException(Exception ex) {
							ex.printStackTrace();
							onFail(ex);
						}
					});
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					onFail(e);
				}
			}
			
			@Override
			public void onFail(Exception ex) {
				ex.printStackTrace();
				Log.e("getUserInfo", ex.getLocalizedMessage());
				if (theListener != null)
					theListener.onException(ex);
			}
		});
	}
	
	public interface GetUserInfoListener {
		void onSuccess() ;
		void onException(Exception ex);
	}
	
	public interface GetApplicationInfoListener {
		void onSuccess() ;
		void onException(Exception ex);
	}
}
