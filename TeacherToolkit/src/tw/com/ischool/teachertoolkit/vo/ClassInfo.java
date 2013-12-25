package tw.com.ischool.teachertoolkit.vo;

import ischool.dsa.utility.XmlUtil;

import org.w3c.dom.Element;

/*
 * 代表一筆班級物件
 */
public class ClassInfo {
	
	private final static String CLASSID = "ClassID";
	private final static String APID = "";
	private final static String CLASSNAME = "ClassName";
	private final static String GRADEYEAR = "GradeYear";

	private String mClassID;	//班級編號
	private String mClassName;	//班級名稱
	private String mGradeYear;	//年級
	private String mAPID;	//所屬的 DSA Application ID
	
	public ClassInfo(String clsID, String apID, String clsName, String gradeYear) {
		mClassID = clsID;
		mClassName = clsName;
		mGradeYear = gradeYear;
		mAPID = apID;
	}
	
	public ClassInfo(Element elmClass) {
		if (elmClass == null)
			return  ;
		
		mClassID = XmlUtil.getElementText(elmClass ,CLASSID);
		mClassName = XmlUtil.getElementText(elmClass, CLASSNAME);
		mGradeYear = XmlUtil.getElementText(elmClass ,GRADEYEAR);
	}

	
	
	public String getAPID() {
		return mAPID;
	}
	public void setAPID(String apID) {
		mAPID = apID;
	}
	public String getClassID() {
		return mClassID;
	}

	public void setClassID(String classID) {
		mClassID = classID;
	}

	public String getClassName() {
		return mClassName;
	}

	public void setClassName(String className) {
		mClassName = className;
	}

	public String getGradeYear() {
		return mGradeYear;
	}

	public void setGradeYear(String gradeYear) {
		mGradeYear = gradeYear;
	}
	
	
}
