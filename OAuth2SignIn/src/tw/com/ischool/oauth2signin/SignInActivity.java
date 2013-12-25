package tw.com.ischool.oauth2signin;

import ischool.signin.WebAppInterface;
import ischool.signin.WebAppInterface.OnTokenRetrievedListener;

import java.util.Date;

import tw.com.ischool.oauth2signin.AccessToken.RefreshAccessTokenListener;
import tw.com.ischool.oauth2signin.util.Util;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/*
 * 此 Activity 負責以 OAuth2 與 ischool 帳號進行登入與登出的動作。
 * 其他程式使用 startActivityForResult 以開啟此 Acitivity，如以下程式碼執行登入動作：
 * 
 * 	Intent i = new Intent(MainActivity.this, SignActivity.class);
 *  startActvityForResult(i, SignInActivity.ACTION_REQUESTCODE);
 * 
 * 
 * 要接收執行結果請覆寫 onActivityResult 事件，並從 resultCode 判斷是否成功，如：
 * @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 // 判斷是否是根據 SignIn 動作的回傳
 if (requestCode == SignInActivity.ACTION_REQUESTCODE) {
 // 如果執行成功
 if (resultCode == RESULT_OK) {
 User user = User.get();
 String firstName = user.getFirstName();
 }
 else {
 String errMsg = data.getExtraString(SignActvity.SIGNIN_ERROR_MESSAGE)

 }	
 }
 }
 *
 *
 * 如果要登出系統，則在 intent 中加入：
 * 
 * 	i.putExtra(SignInActivity.ACTION_TYPE, SignInActivity.ACTION_TYPE_SIGNOUT);
 * 
 * 則 Activity 啟動後會停在登入畫面。
 */

public class SignInActivity extends Activity {

	/* 如果要執行登出動作，則在 intent 中加入以下參數。 */
	public final static String ACTION_TYPE = "SignActivity.IsSignOut"; //
	public final static int ACTION_TYPE_SIGNIN = 0;
	public final static int ACTION_TYPE_SIGNOUT = -1;

	/* 讓呼叫的 Activity 可以取得登入錯誤訊息的 Key */
	public final static String SIGNIN_ERROR_MESSAGE = "SignInActivity.SignInErrorMessage";

	/* 啟動此 Activity 時，應該傳入此 Request Code，以方便在接收結果時判斷 */
	public final static int ACTION_REQUESTCODE = 65535;

	/* Tag for Debugging */
	public final static String DEBUG_TAG = "IschoolOAuth2Fragment.Debug_Tag";

	private final static String PREF_NAME = "RefreshTokenPreference";
	private final static String PREF_REFRESHTOKEN = "RefreshToken";

	private WebView mWebLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		showLog("SignInActvity.onCreate() ....");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);

		initWebView();

		boolean hasNetworkConnection = Util.isNetworkOnline(this);
		Intent i = getIntent();
		if (wantToExecuteSignOut(i)) {
			signOut(hasNetworkConnection);
			if (!hasNetworkConnection) {
				returnResult(false, "已清除行動裝置上的身分資料，但沒有連線的網路，進入登入畫面。");
			}
		} else {
			// 判斷網路是否連通，若否，則Dialog 後關閉。
			showLog("判斷網路是否連通 ...");
			if (!hasNetworkConnection) {
				returnResult(false, "沒有連線的網路，無法登入系統。");
			} else
				signIn();
		}

		getActionBar().hide();
	}

	/*
	 * 對 WebView 初始化。
	 */
	private void initWebView() {

		showLog("SignInActvity.initWebView() ....");

		// 1. 取得對 WebView 的參照
		mWebLogin = (WebView) findViewById(R.id.webLogin);

		// 2. WebView 啟用 Javascript
		WebSettings setting = mWebLogin.getSettings();
		setting.setJavaScriptEnabled(true);

		// 3. 接受從 Javascript 傳過來的 Access Token
		WebAppInterface webInterface = new WebAppInterface();
		webInterface.setOnTokenRetrivedListener(new OnTokenRetrievedListener() {
			@Override
			public void onTokenRetrieved(String tokenJSONString) {
				AccessToken.setCurrentAccessTokenString(tokenJSONString);
				AccessToken token = AccessToken.getCurrentAccessToken(); // Singleton,
																			// 所以到處可以取得
																			// AccessToken

				if (token.hasError()) {
					returnResult(false, token.getErrorMessage(), false);
				} else {
					// 把 Refresh Token 紀錄在 Shared Preference 中
					setRefreshTokenToPreference(token.getCurrentAccessToken()
							.getRefreshToken());
					// 取得使用者資料
					getUserInfo(token.getAccessToken());
				}
			}
		});
		mWebLogin.addJavascriptInterface(webInterface, "Android");

		// 4.
		mWebLogin.setWebViewClient(new WebViewClient() {
			// 自行處理 redirect 的狀況
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showLog("載入 url : " + url);
				view.loadUrl(url);
				return true;
			}

			// 當載入完成時，
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				showLog("url 已經載入完成 : " + url);

				if (url.equals(Constant.SingIn.OAUTH_LOGOUT_URL)) {
					toSignInPage();
				}
				//
				// // 第一次登入時會進入 web2 登入後的畫面，這時候需要在重新 reload 一次。
				if (url.indexOf(Constant.SingIn.STRANGE_URL) > -1) {
					toSignInPage();
				}
			}
		});
	}

	/*
	 * 把畫面導到登入頁面
	 */
	private void toSignInPage() {
		mWebLogin.loadUrl(Constant.SingIn.OAUTH_URL);
	}

	/*
	 * 把畫面導到登出頁面
	 */
	private void toSignOutPage() {
		mWebLogin.loadUrl(Constant.SingIn.OAUTH_LOGOUT_URL);
	}

	// 執行登出動作
	private void signOut(boolean hasNetworkConnection) {
		showLog("SignInActvity.signOut() ....");

		// 1. 清掉記憶體中的 AccessToken
		showLog("erase Access Token ....");
		AccessToken.setCurrentAccessToken(null); // Clear Current Access Token
		// 2. 清掉 preference 中的 Refresh Token
		showLog("remove Refredh Token from Preference ....");
		removeRefreshTokenFromPreference();

		// 3. 導到登出頁面
		if (hasNetworkConnection) {
			showLog(" to Signout Page ....");
			this.toSignOutPage();
		}
	}

	/*
	 * 執行登入動作。此動作會先檢查記憶體中是否有 Access Token？ 1.1 如果有，代表這次已經登入，接下來就檢查此 Token 是否已過期？
	 * 1.1.1 如果沒有過期，則直接回傳 成功。 1.1.2 如果已經過期，則透過 refresh token 重新取得 Access
	 * Token，並回傳成功或不成功。
	 * 
	 * 1.2 如果沒有，就表示還沒有登入，就要檢查 preference 中是否有 refresh token？ 1.2.1 如果有 Refresh
	 * Token，就表示以前曾經登入過，所以就拿 Refresh Token 換一個新的 Access Token，並回傳成功與否。 1.2.2
	 * 如果沒有 Refresh Token，就表示從來不曾成功登入系統，所以顯示登入畫面。
	 */
	private void signIn() {
		Log.d(DEBUG_TAG, "SignInActivity.SignIn() ...");
		AccessToken token = AccessToken.getCurrentAccessToken();
		// 先判斷記憶體中是否有 Access Token，代表是否已經登入
		showLog("找尋記憶體中是否有 Access Token");
		if (token != null) {
			// 判斷 token 是否過期
			Log.e(DEBUG_TAG, "Token存在：" + token.getAccessToken());
			Log.e(DEBUG_TAG, "判斷 Token 是否過期");
			if (!token.isExpired()) {
				Log.e(DEBUG_TAG, "Token 未過期 ..."
						+ token.getExpiredDate().toString());
				// a. 如果尚未過期，就直接回傳
				setResult(RESULT_OK);
			} else { // 如果已過期，就換一個新的 AccessToken
				Log.e(DEBUG_TAG, "SignIn() ...");
				renewToken(AccessToken.getCurrentAccessToken()
						.getRefreshToken());
			}
		} else { // 如果記憶體中沒有 Access Token
			// a. 找出 Preference 中儲存的 Refresh Token，並更新
			showLog("記憶體中找不到 Access Token, 尋找 Preference 中是否有 Refresh Token ? ....");
			SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
			String refresh_token = settings.getString(PREF_REFRESHTOKEN, "");
			if (!refresh_token.equals("")) {
				showLog("找到 refresk token :" + refresh_token + ", 換一個新的。");
				renewToken(refresh_token);
			} else {
				// 如果連 Preference 中也沒有 Refresh Token ，表示從未登入過，則導到登入頁面
				showLog("preference 中找不到 refresk token，導到登入頁面。");
				this.toSignInPage();
			}
		}
	}

	/*
	 * 以舊的 Refresh Token 更換一組新的 Access Token / Refresh Token, 如果成功取得
	 * AccessToken之後，會先更新 shared preference中的 refresh Token， 然後取得使用者資料。
	 */
	private void renewToken(String refreshToken) {
		showLog("SignInActivity.renewToken(...);");
		// 更換一個新的 access token
		AccessToken.refreshAccessToken(refreshToken,
				new RefreshAccessTokenListener() {

					@Override
					public void onSuccess(AccessToken token) {
						showLog(" 以 refresh token 成功取得 AccessToken : " + token);
						// 把 Refresh Token 紀錄在 Shared Preference 中
						setRefreshTokenToPreference(token
								.getCurrentAccessToken().getRefreshToken());
						// b. 取得使用者資料
						getUserInfo(token.getAccessToken());
					}

					@Override
					public void onException(Exception ex) {
						showLog("以 refresh token 取得 AccessToken 失敗 : "
								+ ex.getLocalizedMessage());
						returnResult(false, ex.getLocalizedMessage()); // 回傳失敗
					}
				});
	}

	/*
	 * 到 auth.ischool.com.tw 取得使用者的資訊
	 */
	private void getUserInfo(String token) {
		showLog("SignInActivity.getUserInfo( tokenv) ...");
		final User user = User.get(); // Singleton
		user.getUserInfo(token, new User.GetUserInfoListener() {
			@Override
			public void onSuccess() {
				Log.d("DEBUG",
						" Get User Info Success : User Name : "
								+ user.getFirstName() + user.getLastName());
				returnResult(true, ""); // 回傳成功
			}

			@Override
			public void onException(Exception ex) {
				showLog("取得使用者資訊失敗：" + ex.getLocalizedMessage());
				returnResult(false, ex.getLocalizedMessage()); // 回傳失敗
			}
		});
	}

	/*
	 * 把結果回傳給原本呼叫的 Activity (caller)。 預設會關閉目前的 Activity
	 */
	private void returnResult(boolean isSuccessful, String errMsg) {
		returnResult(isSuccessful, errMsg, true);
	}

	/*
	 * 把結果回傳給原本呼叫的 Activity (caller)。
	 */
	private void returnResult(boolean isSuccessful, String errMsg,
			boolean wantToCloseActivity) {
		showLog("把結果回傳給前一個 Activity : SignInActivity.returnResult ("
				+ isSuccessful + "," + errMsg + "," + wantToCloseActivity + ")");
		if (isSuccessful) {
			setResult(RESULT_OK);
		} else {
			Intent i = new Intent();
			i.putExtra(SIGNIN_ERROR_MESSAGE, errMsg);
			setResult(RESULT_CANCELED, i);
		}

		// 決定是否關閉目前的 Activity
		if (wantToCloseActivity) {
			finish();
		}
	}

	// 把 refresh Token 記錄到 Preference 中。
	private void setRefreshTokenToPreference(String refreshToken) {
		SharedPreferences settings = this.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREF_REFRESHTOKEN, refreshToken);
		// Commit the edits!
		editor.commit();
	}

	// 把 refresh token 從 Preference 中移除。
	private void removeRefreshTokenFromPreference() {
		SharedPreferences settings = this.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(PREF_REFRESHTOKEN);
		editor.commit();
	}

	/* 判斷是否要執行登出動作，否則就是登入動作 */
	private boolean wantToExecuteSignOut(Intent i) {
		boolean result = false;
		if (i.hasExtra(ACTION_TYPE)) {
			result = (i.getIntExtra(ACTION_TYPE, ACTION_TYPE_SIGNIN) == ACTION_TYPE_SIGNOUT);
		}
		return result;
	}

	/*
	 * Debug 用
	 */
	private void showLog(String msg) {
		Log.d(SignInActivity.DEBUG_TAG, msg);
	}

}
