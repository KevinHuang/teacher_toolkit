package tw.com.ischool.teachertoolkit.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassTempData {

	/* 靜態欄位 */
	private static List<ClassInfo> sTempClassList = new ArrayList<ClassInfo>();
	private static HashMap<String, ClassInfo> sDicClassList = new HashMap<String, ClassInfo>();
	private static HashMap<String, List<ClassInfo>> sTemp_AP_ClassList = new HashMap<String, List<ClassInfo>>();
	
	
	public static void setTempClassList(List<ClassInfo> classList) {
		sTempClassList = classList;
		sTemp_AP_ClassList.clear();
		sDicClassList.clear();
		for(ClassInfo cls : sTempClassList) {
			String ap_id = cls.getAPID();
			String key = ap_id + "_" + cls.getClassID() ;
			sDicClassList.put(key, cls);
			
			if (!sTemp_AP_ClassList.containsKey(ap_id))
				sTemp_AP_ClassList.put(cls.getAPID(), new ArrayList<ClassInfo>());
			sTemp_AP_ClassList.get(ap_id).add(cls);
		}
	}

	public static List<ClassInfo> getTempClassList() {
		return sTempClassList;
	}
	
	public static ClassInfo getClassByID(String ap_id, String cls_id) {
		ClassInfo cls = null ;
		String key = ap_id + "_" + cls_id ;
		if (sDicClassList.containsKey(key))
			cls = sDicClassList.get(key);
		return cls;
	}
	
	public static List<ClassInfo> getClassListByAPID(String apID) {
		List<ClassInfo> result = new ArrayList<ClassInfo>();
		if (sTemp_AP_ClassList.containsKey(apID))
			result = sTemp_AP_ClassList.get(apID);
		return result ;
	}
}
