package tw.com.ischool.teachertoolkit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TABLE_STUDENT = "student"; // Table student
	public static final String TABLE_APPLICATION = "application"; // Table application
	public static final String TABLE_CLASS = "class";	//table class

	private final static String DBNAME = "teacher_toolkit.db"; // 資料庫名稱
	private final static int DATABASE_VERSION = 1; // 版本
	/* Student Detail */
	public static final String COLUMN_STUDENT_ID = "_id";
	public static final String COLUMN_STUDENT_STUDENTID = "StudentId";
	public static final String COLUMN_STUDENT_CLASS_ID = "ref_class_id";
	public static final String COLUMN_STUDENT_AP_ID = "ref_ap_id";
	public static final String COLUMN_STUDENT_STUDENTNAME = "StudentName";
	public static final String COLUMN_STUDENT_STUDENTNUMBER = "StudentNumber";
	public static final String COLUMN_STUDENT_SEATNO = "Seatno";
	public static final String COLUMN_STUDENT_GENDER = "Gender";
	public static final String COLUMN_STUDENT_CLASSNAME = "ClassName";
	public final static String COLUMN_STUDENT_FRESHMANPHOTO = "FreshmanPhoto";
	public final static String COLUMN_STUDENT_GRADUATEPHOTO = "GraduatePhoto";
	public final static String COLUMN_STUDENT_FATHERNAME = "FatherName";
	public final static String COLUMN_STUDENT_MOTHERNAME = "MotherName";
	public final static String COLUMN_STUDENT_CUSTODIANNAME = "CustodianName";
	public final static String COLUMN_STUDENT_PERMANENTPHONE = "PermanentPhone";
	public final static String COLUMN_STUDENT_CONTACTPHONE = "ContactPhone";
	public final static String COLUMN_STUDENT_OTHERPHONES = "OtherPhones";
	public final static String COLUMN_STUDENT_SMSPHONE = "SMSPhone";
	public final static String COLUMN_STUDENT_PERMINENTADDRESS = "PermanentAddress";
	public final static String COLUMN_STUDENT_MAILINGADDRESS = "MailingAddress";
	public final static String COLUMN_STUDENT_OTHERADDRESSES = "OtherAddresses";
	
	/* Application Detail */
	public static final String COLUMN_AP_ID = "_id";
	public static final String COLUMN_AP_NAME = "ap_name";
	public static final String COLUMN_AP_FULLAME = "full_name";
	public static final String COLUMN_AP_ORIGIN = "origin";
	
	/* Class Detail */
	public static final String COLUMN_CLASS_ID = "_id";
	public static final String COLUMN_CLASS_REF_APID = "ref_ap_id";
	public static final String COLUMN_CLASS_NAME = "class_name";
	public static final String COLUMN_CLASS_GRADE = "grade";
	

	public DBHelper(Context context) {
		super(context, DBNAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String INIT_TABLE_STUD = " CREATE TABLE " + TABLE_STUDENT + "("
				+ COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_STUDENT_STUDENTID + " INTEGER, " 
				+ COLUMN_STUDENT_STUDENTNAME + " CHAR, " 
				+ COLUMN_STUDENT_STUDENTNUMBER + " CHAR, " 
				+ COLUMN_STUDENT_SEATNO + " CHAR, " 
				+ COLUMN_STUDENT_CLASS_ID + " CHAR, " 
				+ COLUMN_STUDENT_AP_ID + " CHAR, " 
				+ COLUMN_STUDENT_GENDER + " CHAR, " 
				+ COLUMN_STUDENT_CLASSNAME + " CHAR, " 
				+ COLUMN_STUDENT_FRESHMANPHOTO + " CHAR, "
				+ COLUMN_STUDENT_GRADUATEPHOTO + " CHAR, " 
				+ COLUMN_STUDENT_FATHERNAME + " CHAR, " 
				+ COLUMN_STUDENT_MOTHERNAME + " CHAR, "
				+ COLUMN_STUDENT_CUSTODIANNAME + " CHAR, " 
				+ COLUMN_STUDENT_PERMANENTPHONE + " CHAR, " 
				+ COLUMN_STUDENT_CONTACTPHONE + " CHAR, "
				+ COLUMN_STUDENT_OTHERPHONES + " CHAR, " 
				+ COLUMN_STUDENT_SMSPHONE + " CHAR, "
				+ COLUMN_STUDENT_PERMINENTADDRESS + " CHAR, " 
				+ COLUMN_STUDENT_MAILINGADDRESS + " CHAR, " 
				+ COLUMN_STUDENT_OTHERADDRESSES + " CHAR) ";
		db.execSQL(INIT_TABLE_STUD);

		final String INIT_TABLE_AP = " CREATE TABLE " + TABLE_APPLICATION + "("
				+ COLUMN_AP_ID + " INTEGER PRIMARY KEY , "
				+ COLUMN_AP_NAME + " INTERGER, " 
				+ COLUMN_AP_FULLAME + " CHAR, " 
				+ COLUMN_AP_ORIGIN + " CHAR ) ";
		db.execSQL(INIT_TABLE_AP);
		
		final String INIT_TABLE_CLASS = " CREATE TABLE " + TABLE_CLASS + "("
				+ COLUMN_CLASS_ID + " INTEGER PRIMARY KEY , "
				+ COLUMN_CLASS_REF_APID + " INTERGER, " 
				+ COLUMN_CLASS_NAME + " CHAR, " 
				+ COLUMN_CLASS_GRADE + " INTEGER ) ";
		db.execSQL(INIT_TABLE_CLASS);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		for(int i= oldVersion; i< newVersion; i++) {
			updateSQL(i+1, db);
		}
	}

	private void updateSQL(int newVersion, SQLiteDatabase db) {
//		String strSQL = "";
		if (newVersion == 2) { // version 1 to version 2
			
		}

		if (newVersion == 3) { // version 2 to version 3

		}

		if (newVersion == 4) { // version 3 to version 4

		}

		if (newVersion == 5) { // version 4 to version 5
			
		}
	}

}
