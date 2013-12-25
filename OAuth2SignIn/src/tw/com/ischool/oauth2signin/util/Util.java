package tw.com.ischool.oauth2signin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

public class Util {

	public static String convertStreamToString(InputStream stream) {
		StringBuilder sb = new StringBuilder();

		InputStreamReader isReader = new InputStreamReader(stream);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(isReader);
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	/*  檢查網路狀態  */
	public static boolean isNetworkOnline(Context context) {
		//取得 ConnectivityManager 物件
	    ConnectivityManager connMgr = 
	              (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    // 取得作用中的網路資訊物件
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());

	}  
	
	/* 將 Base64 字串轉換成 Bitmap */
	public static Bitmap convertBase64ToBitmap(String strBase64) {
		byte[] imageAsBytes = Base64.decode(strBase64.getBytes(), Base64.DEFAULT);
		Bitmap result = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length); 
		return result;
	}
}
