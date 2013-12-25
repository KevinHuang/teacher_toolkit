package tw.com.ischool.teachertoolkit.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/*
 * 此物件暫時儲存學生資料，好在不同 Fragment 之間存取。
 */
public class StudentsTempData {

	/* 靜態欄位 */
	private static List<StudentInfo> sTempStudents = new ArrayList<StudentInfo>();
	private static HashMap<String, StudentInfo> sTempID_Studs = new HashMap<String, StudentInfo>();
	private static HashMap<String, List<StudentInfo>> sTemp_Class_Students = new HashMap<String, List<StudentInfo>>();
	
	
	public static void setTempStudents(List<StudentInfo> studs) {
		sTempStudents = studs;
		sTempID_Studs.clear();
		sTemp_Class_Students.clear();
		
		for(StudentInfo stud : studs) {
			sTempID_Studs.put(stud.getStudentID(), stud);
			
			String class_key = stud.getAPID() + "_" + stud.getClassID();
			if (!sTemp_Class_Students.containsKey(class_key))
				sTemp_Class_Students.put(class_key, new ArrayList<StudentInfo>());
			
			sTemp_Class_Students.get(class_key).add(stud);
		}
	}

	public static List<StudentInfo> getTempStudents() {
		return sTempStudents;
	}
	
	public static StudentInfo getStudentByID(String apID, String studID) {
		StudentInfo stud = null ;
		if (sTempID_Studs.containsKey(studID))
			stud = sTempID_Studs.get(studID);
		return stud;
	}
	
	public static List<StudentInfo> getStudentsByClass(ClassInfo cls) {
		List<StudentInfo> result = new ArrayList<StudentInfo>();
		String class_key = cls.getAPID() + "_" + cls.getClassID();
		
		if (sTemp_Class_Students.containsKey(class_key))
			result = sTemp_Class_Students.get(class_key);
		
		Collections.sort(result, new Comparator<StudentInfo>() {

			@Override
			public int compare(StudentInfo lhs, StudentInfo rhs) {
				return (lhs.getClassName() + lhs.getSeatNo()).compareTo(rhs.getClassName() + rhs.getSeatNo());
			}
			
		});
		
		return result ;
	}
	
	public class MyIntComparable implements Comparator<Integer>{
		 
	    @Override
	    public int compare(Integer o1, Integer o2) {
	        return (o1>o2 ? -1 : (o1==o2 ? 0 : 1));
	    }
	} 
	
	public static List<StudentInfo> queryStudentsByKeyword(String keyword) {
		List<StudentInfo> result = new ArrayList<StudentInfo>();
		
		for(StudentInfo stud : sTempStudents) {
			if (stud.getName().indexOf(keyword) > 0) {
				result.add(stud);
			}
		}
		
		return result ;
	}
}
