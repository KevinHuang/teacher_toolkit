package tw.com.ischool.oauth2signin.util;

import ischool.dsa.client.ContractConnection;
import ischool.dsa.client.target.dsns.DSNSTargetURLProvider;
import ischool.dsa.client.token.GeneralToken;
import ischool.dsa.utility.XmlHelper;

import org.w3c.dom.Element;

import android.os.StrictMode;

public class DSAUtil {

	private static ContractConnection _defaultConnection;

	public static ContractConnection getDefaultConnection() {
		return _defaultConnection;
	}

	public static void setDefaultConnection(ContractConnection conn) {
		_defaultConnection = conn;
	}

	public static ContractConnection connectTo(String dsns, String contract,
			String accessToken) {
		
		// Allow Access Internet in Main Thread !
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Element token = XmlHelper
				.parseXml("<SecurityToken Type='PassportAccessToken'><AccessToken>"
						+ accessToken + "</AccessToken></SecurityToken>");

		// dsns name 
		ContractConnection cc = new ContractConnection(
				new DSNSTargetURLProvider(dsns), contract);
		cc.connect(new GeneralToken(token), true);

		return cc;
	}
}
