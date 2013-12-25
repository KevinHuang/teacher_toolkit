package tw.com.ischool.teachertoolkit.vo;

import ischool.dsa.utility.XmlUtil;

import org.w3c.dom.Element;

/*
 *  <Course>
		<CourseID>6857</CourseID>
		<CourseName>全一忠 自主學習</CourseName>
		<Period>1</Period>
	</Course>
 */
public class CourseInfo {

	private final static String COURSEID = "CourseID";
	private final static String COURSENAME = "CourseName";
	private final static String PERIOD = "CourseName";

	private String mCourseID;	//班級編號
	private String mCourseName;	//班級名稱
	private String mPeriod;	//年級
	
	public CourseInfo(Element elmCourse) {
		mCourseName = XmlUtil.getElementText(elmCourse ,COURSEID);
		mCourseName = XmlUtil.getElementText(elmCourse, COURSENAME);
		mPeriod = XmlUtil.getElementText(elmCourse ,PERIOD);
	}

	public String getCourseID() {
		return mCourseID;
	}

	public void setCourseID(String courseID) {
		mCourseID = courseID;
	}

	public String getCourseName() {
		return mCourseName;
	}

	public void setCourseName(String courseName) {
		mCourseName = courseName;
	}

	public String getPeriod() {
		return mPeriod;
	}

	public void setPeriod(String period) {
		mPeriod = period;
	}
	
}
