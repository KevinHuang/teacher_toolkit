package tw.com.ischool.teachertoolkit.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tw.com.ischool.oauth2signin.APInfo;

public class APTempData {
	/* 靜態欄位 */
	private static List<APInfo> sTempAPList = new ArrayList<APInfo>();
	private static HashMap<String, APInfo> sDicAPList = new HashMap<String, APInfo>();
	
	
	public static void setTempAPList(List<APInfo> apList) {
		sTempAPList = apList;
		sDicAPList.clear();
		
		for(APInfo ap : apList) {
			String ap_id = ap.getID() + "";
			sDicAPList.put(ap_id, ap);
		}
	}

	public static List<APInfo> getTempAPList() {
		return sTempAPList;
	}
	
	public static APInfo getAPByID(String ap_id) {
		APInfo ap = null ;
		String key = ap_id ;
		if (sDicAPList.containsKey(key))
			ap = sDicAPList.get(key);
		return ap;
	}
}
