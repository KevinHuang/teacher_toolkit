package tw.com.ischool.oauth2signin;

import java.net.HttpURLConnection;
import java.net.URL;

import tw.com.ischool.oauth2signin.util.Util;
import android.os.AsyncTask;
import android.util.Log;

public class HttpUtil {

	private HttpListener mListener;
	private Exception mEx;

	public void get(String url, HttpListener listener) {
		mListener = listener;
		(new HttpGetTask()).execute(url);
	}

	/**
	 * Define Listener Interface
	 * @author huangkevin
	 *
	 */
	public interface HttpListener {
		void onSuccess(String result);

		void onFail(Exception ex);
	}

	private class HttpGetTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mEx = null;
		}

		@Override
		protected String doInBackground(String... urls) {
			String result = "";
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(urls[0]);

				urlConnection = (HttpURLConnection) url.openConnection();
				result = Util.convertStreamToString(urlConnection
						.getInputStream());
			} catch (Exception ex) {
				Log.d("HTTPGetTask", ex.getLocalizedMessage());
				mEx = ex;
			} finally {
				if (urlConnection != null)
					urlConnection.disconnect();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mListener != null) {
				if (mEx != null) {
					mListener.onFail(mEx);
				}
				else {
					mListener.onSuccess(result);
				}
					
			}
				
		}
	}

}
