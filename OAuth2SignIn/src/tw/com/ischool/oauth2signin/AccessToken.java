package tw.com.ischool.oauth2signin;


import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import tw.com.ischool.oauth2signin.HttpUtil.HttpListener;
import tw.com.ischool.oauth2signin.User.GetUserInfoListener;

import android.util.Log;

public class AccessToken {
	private final static String DebugTag = "AccessToken.DebugTag";
	
	private final static String ACCESS_TOKEN = "access_token";
	private final static String REFRESH_TOKEN = "refresh_token";
	private final static String TOKEN_TYPE = "token_type";
	private final static String SCOPE = "scope";
	private final static String EXPIRES_IN = "expires_in";
	private final static String ERROR = "error";
	
	private String mAccessToken="";
	private String mRefreshToken="";
	private String mTokenType ="";
	private String mScope ="";
	private Date mExpiredDate ;
	private boolean mError = true;
	private String mErrorMessage ="";
	
	private static AccessToken sCurrentAccessToken;
	
	public static AccessToken getCurrentAccessToken() {
		return sCurrentAccessToken ;
	}
	
	public static void setCurrentAccessToken(AccessToken token) {
		sCurrentAccessToken = token;
	}
	
	public static void setCurrentAccessTokenString(String tokenString) {
		sCurrentAccessToken = new AccessToken(tokenString);
	}
	
	//以 RefreshToken 取得新的 Access Token
	public static void refreshAccessToken(String refreshToken ,  RefreshAccessTokenListener listener) {
		
		final RefreshAccessTokenListener theListener = listener;
		HttpUtil hu = new HttpUtil();
		String targetUrl = String.format(Constant.SingIn.OAUTH_REFRESHTOKEN_URL  , refreshToken);
		
		hu.get(targetUrl, new HttpListener() {
			
			@Override
			public void onSuccess(String result) {
				Log.d("refresh Access Token", result);
				try {
					AccessToken.setCurrentAccessTokenString(result);
					theListener.onSuccess(AccessToken.getCurrentAccessToken());
				} catch (Exception e) {
					e.printStackTrace();
					onFail(e);
				}
			}
			
			@Override
			public void onFail(Exception ex) {
				ex.printStackTrace();
				Log.e("refresh Access Token", ex.getLocalizedMessage());
				if (theListener != null)
					theListener.onException(ex);
			}
		});
		
	}
	
	public interface RefreshAccessTokenListener {
		void onSuccess(AccessToken token) ;
		void onException(Exception ex);
	}
	
	
	
	
	private AccessToken(String tokenJSONString) {
		try {
			JSONObject obj = new JSONObject(tokenJSONString);
			if (obj.has(ACCESS_TOKEN)) mAccessToken = obj.getString(ACCESS_TOKEN);
			if (obj.has(REFRESH_TOKEN)) mRefreshToken = obj.getString(REFRESH_TOKEN);
			if (obj.has(TOKEN_TYPE)) mTokenType = obj.getString(TOKEN_TYPE);
			if (obj.has(SCOPE)) mScope = obj.getString(SCOPE);
			
			if (obj.has(EXPIRES_IN))  {
				int seconds = obj.getInt(EXPIRES_IN);
				long ONE_MINUTE_IN_MILLIS= seconds *  1000;//millisecs
				long t=(new Date()).getTime();
				mExpiredDate =new Date(t +  ONE_MINUTE_IN_MILLIS);
			}
			
			if (obj.has(ERROR))  {
				mErrorMessage = obj.getString(ERROR);
			}
			else {
				mError = false;
				mErrorMessage = "";
			}
			
			Log.d(DebugTag, tokenJSONString);
			
			/* {
			 * "access_token":"354a89862a59e218281efe97656e374a",
			 * "expires_in":3600,
			 * "token_type":"bearer",
			 * "scope":null,
			 * "refresh_token":"e781187c4e5db6885e83d073565ea45a"
			 * } 
			 * 
			 * {
			 * 	"error":"invalid_grant code expired.(now=2013-11-07 22:34:32 expires=2013-11-07 21:40:46)"
			 * };  	
			 * */
		} catch (Exception ex) {

		}
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public String getRefreshToken() {
		return mRefreshToken;
	}

	public Date getExpiredDate() {
		return mExpiredDate;
	}

	public String getTokenType() {
		return mTokenType;
	}

	public String getScope() {
		return mScope;
	}
	public boolean hasError() {
		return mError;
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}
	
	public boolean isExpired() {
		return (this.getExpiredDate().getTime() <= (new Date()).getTime());
	}
}
