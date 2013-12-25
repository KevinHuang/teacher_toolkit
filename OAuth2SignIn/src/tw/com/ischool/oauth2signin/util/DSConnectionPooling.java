package tw.com.ischool.oauth2signin.util;

import ischool.dsa.client.ContractConnection;
import ischool.dsa.client.target.dsns.DSNSTargetURLProvider;
import ischool.dsa.client.token.GeneralToken;
import ischool.dsa.utility.XmlHelper;

import java.util.HashMap;

import org.w3c.dom.Element;

import tw.com.ischool.oauth2signin.AccessToken;
import tw.com.ischool.oauth2signin.AccessToken.RefreshAccessTokenListener;
import android.os.AsyncTask;

public class DSConnectionPooling {

	private static DSConnectionPooling _singleton;

	private DSConnectionPooling() {

	}

	public static DSConnectionPooling get() {
		if (_singleton == null) {
			_singleton = new DSConnectionPooling();
		}

		return _singleton;
	}

	private HashMap<String, ContractConnection> mPooling = new HashMap<String, ContractConnection>();
	private String mDSNS = "";
	private String mContract = "";
	private AccessToken mToken;

	private GetConnectionListener mListener;

	public void getConnection(String dsns, String contract,
			AccessToken accessToken, GetConnectionListener listener) {
		// public ContractConnection getConnection(String dsns, String contract,
		// AccessToken accessToken) {

		final String key = dsns + "_" + contract;
		mDSNS = dsns;
		mContract = contract;
		mToken = accessToken;
		mListener = listener;

		if (mPooling.containsKey(key)) {
			ContractConnection cn = mPooling.get(key);
			// return cn;
			listener.onSuccess(cn);
		} else {
			if (accessToken.isExpired()) {
				AccessToken.refreshAccessToken(accessToken.getRefreshToken(),
						new RefreshAccessTokenListener() {
							@Override
							public void onSuccess(AccessToken token) {
								try {
									ContractConnection cn = DSAUtil.connectTo(
											mDSNS, mContract,
											mToken.getAccessToken());
									mPooling.put(key, cn);
									mListener.onSuccess(cn);
								} catch (Exception ex) {
									mListener.onFail(ex);
								}
							}

							@Override
							public void onException(Exception ex) {
								mListener.onFail(ex);
							}
						});
			} else {
				try {
					ContractConnection cn = DSAUtil.connectTo(mDSNS, mContract,
							mToken.getAccessToken());
					mPooling.put(key, cn);
					mListener.onSuccess(cn);
				} catch (Exception ex) {
					mListener.onFail(ex);
				}
			}
		}
	}

	public interface GetConnectionListener {
		void onSuccess(ContractConnection cn);

		void onFail(Exception ex);
	}
	//
	//
	// private class ConnectionCreater {
	// private String mDSNS ="";
	// private String mContract ="";
	// private String mToken="";
	//
	// private GetConnectionListener mListener ;
	//
	// public ConnectionCreater(String dsns, String contract, String token) {
	// mDSNS = dsns;
	// mContract = contract;
	// mToken = token;
	// }
	//
	// public void create(GetConnectionListener listener) {
	// mListener = listener ;
	// Void[] params = new Void[] {};
	// (new CreateConnectionTask()).execute(params);
	// }
	//
	// private class CreateConnectionTask extends AsyncTask<Void, Integer,
	// ResultConnection> {
	//
	// @Override
	// protected ResultConnection doInBackground(Void... params) {
	// Element token = XmlHelper
	// .parseXml("<SecurityToken Type='PassportAccessToken'><AccessToken>"
	// + mToken + "</AccessToken></SecurityToken>");
	//
	// ResultConnection rc = new ResultConnection();
	// try {
	// ContractConnection cn = new ContractConnection(
	// new DSNSTargetURLProvider(mDSNS), mContract);
	// cn.connect(new GeneralToken(token), true);
	// rc.conn = cn;
	// }
	// catch (Exception ex) {
	// rc.ex = ex;
	// }
	// return rc;
	// }
	//
	// @Override
	// protected void onPostExecute(ResultConnection rc) {
	// String key = mDSNS + "_" + mContract ;
	// if (rc.ex == null) {
	// mListener.onSuccess(rc.conn);
	// mPooling.put(key, rc.conn);
	// }
	// else {
	// mListener.onFail(rc.ex);
	// }
	// }
	//
	// }// end of class : CreateConnectionTask
	//
	// private class ResultConnection {
	// public ContractConnection conn;
	// public Exception ex;
	// }
	//
	// }// end of class : ConnectionCreater
}// end of class : DSConnectionPooling

