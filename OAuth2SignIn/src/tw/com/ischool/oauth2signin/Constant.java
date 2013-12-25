package tw.com.ischool.oauth2signin;

public class Constant {
	public class SingIn {
		public static final String OAUTH_URL = "http://android-ischool-oauth.appspot.com";
		public static final String OAUTH_REFRESHTOKEN_URL = "http://android-ischool-oauth.appspot.com/refreshToken?refresh_token=%s";
		public static final String OAUTH_LOGOUT_URL = "https://auth.ischool.com.tw/logout.php";
		public static final String OAUTH_USERINFO_URL = "https://auth.ischool.com.tw/services/me2.php?token_type=bearer&access_token=%s";
		public static final String OAUTH_DOMAIN_LIST_URL = "https://auth.ischool.com.tw/services/referenceApplication.get.php?token_type=bearer&access_token=%s";
		public static final String WEB2_URL = "https://web2.ischool.com.tw/";
		public static final String STRANGE_URL = "https://auth.ischool.com.tw/auth/1";
		
	}
}
