package tw.com.ischool.teachertoolkit.util;

import android.text.TextUtils;

public class StringUtil {
	
	public static Boolean isNullOrWhitespace(String string) {
		  if (string == null)
		   return true;

		  if (string.equalsIgnoreCase("null"))
		   return true;

		  String trimed = string.trim();
		  return TextUtils.isEmpty(trimed);
		 }
}
