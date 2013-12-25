package tw.com.ischool.teachertoolkit.db;

import java.util.ArrayList;
import java.util.List;

import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StudentDataSource {
	private DBHelper dbhelper = null;
	private SQLiteDatabase mDB;

	/* Constructor */
	public StudentDataSource(Context context) {
		dbhelper = new DBHelper(context);
	}

	public void openDatabase() {
		mDB = dbhelper.getWritableDatabase();
	}

	public void closeDatabase() {
		dbhelper.close();
	}

	public List<StudentInfo> getAllStudents() {
		List<StudentInfo> studs = new ArrayList<StudentInfo>();
		String[] columns = { 
				DBHelper.COLUMN_STUDENT_ID, 
				DBHelper.COLUMN_STUDENT_STUDENTID,
				DBHelper.COLUMN_STUDENT_STUDENTNAME, 
				DBHelper.COLUMN_STUDENT_STUDENTNUMBER,
				DBHelper.COLUMN_STUDENT_SEATNO, 
				DBHelper.COLUMN_STUDENT_GENDER,
				DBHelper.COLUMN_STUDENT_CLASSNAME, 
				DBHelper.COLUMN_STUDENT_FRESHMANPHOTO,
				DBHelper.COLUMN_STUDENT_GRADUATEPHOTO, 
				DBHelper.COLUMN_STUDENT_FATHERNAME,
				DBHelper.COLUMN_STUDENT_MOTHERNAME, 
				DBHelper.COLUMN_STUDENT_CUSTODIANNAME,
				DBHelper.COLUMN_STUDENT_PERMANENTPHONE, 
				DBHelper.COLUMN_STUDENT_CONTACTPHONE,
				DBHelper.COLUMN_STUDENT_OTHERPHONES, 
				DBHelper.COLUMN_STUDENT_SMSPHONE,
				DBHelper.COLUMN_STUDENT_PERMINENTADDRESS, 
				DBHelper.COLUMN_STUDENT_MAILINGADDRESS, 
				DBHelper.COLUMN_STUDENT_OTHERADDRESSES,
				DBHelper.COLUMN_STUDENT_AP_ID, 
				DBHelper.COLUMN_STUDENT_CLASS_ID,
				};

		// 讀取資料
		Cursor cursor = mDB.query(DBHelper.TABLE_STUDENT, columns, null, null,
				null, null, DBHelper.COLUMN_STUDENT_STUDENTNAME);

		// 移到第一筆記錄
		cursor.moveToFirst();

		// 當不是最後一筆記錄之後 (類似 EOF)
		while (!cursor.isAfterLast()) {
			StudentInfo todoItem = convertCursorToToDo(cursor);
			studs.add(todoItem);
			cursor.moveToNext();
		}

		return studs;
	}

	private StudentInfo convertCursorToToDo(Cursor cursor) {

		StudentInfo stud = new StudentInfo();

		stud.setSysID(cursor.getLong(0));
		stud.setStudentID(cursor.getLong(1) + "");
		stud.setName(cursor.getString(2));
		stud.setStudentNumber(cursor.getString(3));
		stud.setSeatNo(cursor.getString(4));
		stud.setGender(cursor.getString(5));
		stud.setClassName(cursor.getString(6));
		stud.setFreshmanPhotoBase64String(cursor.getString(7));
		stud.setGraduatePhotoBase64String(cursor.getString(8));
		stud.setFatherName(cursor.getString(9));
		stud.setMotherName(cursor.getString(10));
		stud.setCustodianName(cursor.getString(11));
		stud.setPerminentPhone(cursor.getString(12));
		stud.setContactPhone(cursor.getString(13));
		stud.setOtherPhones(cursor.getString(14));
		stud.setSMSPhone(cursor.getString(15));
		stud.setPerminentAddress(cursor.getString(16));
		stud.setMailingAddress(cursor.getString(17));
		stud.setOtherAddresses(cursor.getString(18));
		stud.setAPID(cursor.getString(19));
		stud.setClassID(cursor.getString(20));
		return stud;
	}

	public long insertStudent(StudentInfo stud) {
		/* 寫入 DB */
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_STUDENT_STUDENTID, stud.getStudentID());
		cv.put(DBHelper.COLUMN_STUDENT_STUDENTNAME, stud.getName());
		cv.put(DBHelper.COLUMN_STUDENT_STUDENTNUMBER, stud.getStudentNumber());
		cv.put(DBHelper.COLUMN_STUDENT_SEATNO, stud.getSeatNo());
		cv.put(DBHelper.COLUMN_STUDENT_GENDER, stud.getGender());
		cv.put(DBHelper.COLUMN_STUDENT_CLASSNAME, stud.getClassName());
		cv.put(DBHelper.COLUMN_STUDENT_AP_ID, stud.getAPID());
		cv.put(DBHelper.COLUMN_STUDENT_CLASS_ID, stud.getClassID());
		cv.put(DBHelper.COLUMN_STUDENT_FRESHMANPHOTO, stud.getFreshmanPhotoBase64String());
		cv.put(DBHelper.COLUMN_STUDENT_GRADUATEPHOTO, stud.getGraduatePhotoBase64String());
		cv.put(DBHelper.COLUMN_STUDENT_FATHERNAME, stud.getFatherName());
		cv.put(DBHelper.COLUMN_STUDENT_MOTHERNAME, stud.getMotherName());
		cv.put(DBHelper.COLUMN_STUDENT_CUSTODIANNAME, stud.getCustodianName());
		cv.put(DBHelper.COLUMN_STUDENT_PERMANENTPHONE, stud.getPerminentPhone());
		cv.put(DBHelper.COLUMN_STUDENT_CONTACTPHONE, stud.getContactPhone());
		cv.put(DBHelper.COLUMN_STUDENT_OTHERPHONES, stud.getOtherPhones().getFullPhone()); //不是字串
		cv.put(DBHelper.COLUMN_STUDENT_SMSPHONE, stud.getSMSPhone());
		cv.put(DBHelper.COLUMN_STUDENT_PERMINENTADDRESS, stud.getPerminentAddress().getFullAddress()); //不是字串
		cv.put(DBHelper.COLUMN_STUDENT_MAILINGADDRESS, stud.getMailingAddress().getFullAddress());
		cv.put(DBHelper.COLUMN_STUDENT_OTHERADDRESSES, stud.getOtherAddresses().getFullAddress());
		

		long newId = mDB.insert(DBHelper.TABLE_STUDENT, null, cv);
		return newId;
	}
	
	public int updateStudent(StudentInfo stud) {
		/* 寫入 DB */
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_STUDENT_STUDENTID, stud.getStudentID());
		cv.put(DBHelper.COLUMN_STUDENT_STUDENTNAME, stud.getName());
		cv.put(DBHelper.COLUMN_STUDENT_STUDENTNUMBER, stud.getStudentNumber());
		cv.put(DBHelper.COLUMN_STUDENT_SEATNO, stud.getSeatNo());
		cv.put(DBHelper.COLUMN_STUDENT_GENDER, stud.getGender());
		cv.put(DBHelper.COLUMN_STUDENT_CLASSNAME, stud.getClassName());
		cv.put(DBHelper.COLUMN_STUDENT_AP_ID, stud.getAPID());
		cv.put(DBHelper.COLUMN_STUDENT_CLASS_ID, stud.getClassID());
		cv.put(DBHelper.COLUMN_STUDENT_FRESHMANPHOTO, stud.getFreshmanPhotoBase64String());
		cv.put(DBHelper.COLUMN_STUDENT_GRADUATEPHOTO, stud.getGraduatePhotoBase64String());
		cv.put(DBHelper.COLUMN_STUDENT_FATHERNAME, stud.getFatherName());
		cv.put(DBHelper.COLUMN_STUDENT_MOTHERNAME, stud.getMotherName());
		cv.put(DBHelper.COLUMN_STUDENT_CUSTODIANNAME, stud.getCustodianName());
		cv.put(DBHelper.COLUMN_STUDENT_PERMANENTPHONE, stud.getPerminentPhone());
		cv.put(DBHelper.COLUMN_STUDENT_CONTACTPHONE, stud.getContactPhone());
		cv.put(DBHelper.COLUMN_STUDENT_OTHERPHONES, stud.getOtherPhones().getFullPhone()); //不是字串
		cv.put(DBHelper.COLUMN_STUDENT_SMSPHONE, stud.getSMSPhone());
		cv.put(DBHelper.COLUMN_STUDENT_PERMINENTADDRESS, stud.getPerminentAddress().getFullAddress()); //不是字串
		cv.put(DBHelper.COLUMN_STUDENT_MAILINGADDRESS, stud.getMailingAddress().getFullAddress());
		cv.put(DBHelper.COLUMN_STUDENT_OTHERADDRESSES, stud.getOtherAddresses().getFullAddress());
		

		String whereClause =  DBHelper.COLUMN_STUDENT_ID + "=" + stud.getSysID() ;
		int recCount = mDB.update(DBHelper.TABLE_STUDENT, cv, whereClause, null);
		//Log.d("DEBUG", "修改了" + recCount + "位學生");
		
		return recCount;
	}


	public void deleteStudentBySystemID(String[] ids) {
		mDB.delete(DBHelper.TABLE_STUDENT, DBHelper.COLUMN_STUDENT_ID + "=?", ids);
	}
	
	public void deleteStudentByStudentID(String[] ids) {
		mDB.delete(DBHelper.TABLE_STUDENT, DBHelper.COLUMN_STUDENT_STUDENTID + "=?", ids);
	}
	
	
	
}
