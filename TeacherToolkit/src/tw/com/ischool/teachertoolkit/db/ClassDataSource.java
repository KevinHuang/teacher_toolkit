package tw.com.ischool.teachertoolkit.db;

import java.util.ArrayList;
import java.util.List;

import tw.com.ischool.oauth2signin.APInfo;
import tw.com.ischool.teachertoolkit.vo.ClassInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ClassDataSource {

	private DBHelper dbhelper = null;
	private SQLiteDatabase mDB;

	/* Constructor */
	public ClassDataSource(Context context) {
		dbhelper = new DBHelper(context);
	}

	public void openDatabase() {
		mDB = dbhelper.getWritableDatabase();
	}

	public void closeDatabase() {
		dbhelper.close();
	}

	public List<ClassInfo> getClassList() {
		List<ClassInfo> aps = new ArrayList<ClassInfo>();
		String[] columns = { 
				DBHelper.COLUMN_CLASS_ID, 
				DBHelper.COLUMN_CLASS_REF_APID,
				DBHelper.COLUMN_CLASS_NAME, 
				DBHelper.COLUMN_CLASS_GRADE };

		// 讀取資料
		Cursor cursor = mDB.query(DBHelper.TABLE_CLASS, columns, null, null,
				null, null, DBHelper.COLUMN_CLASS_NAME);

		// 移到第一筆記錄
		cursor.moveToFirst();

		// 當不是最後一筆記錄之後 (類似 EOF)
		while (!cursor.isAfterLast()) {
			ClassInfo cls = convertCursorToClass(cursor);
			aps.add(cls);
			cursor.moveToNext();
		}

		return aps;
	}

	private ClassInfo convertCursorToClass(Cursor cursor) {
		String cls_id = cursor.getInt(0) + "";
		String ap_id = cursor.getInt(1) + "";
		String cls_name = cursor.getString(2);
		String grade = cursor.getInt(3) + "";
		ClassInfo cls = new ClassInfo(cls_id,ap_id, cls_name, grade);
		
		return cls;
	}

	public long insertClass(ClassInfo cls) {
		/* 寫入 DB */
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_CLASS_ID , cls.getClassID());
		cv.put(DBHelper.COLUMN_CLASS_NAME, cls.getClassName());
		cv.put(DBHelper.COLUMN_CLASS_REF_APID, cls.getAPID());
		cv.put(DBHelper.COLUMN_CLASS_GRADE, cls.getGradeYear());

		long newId = mDB.insert(DBHelper.TABLE_CLASS, null, cv);
		return newId;
	}
	
	public int updateClass(ClassInfo cls) {
		
		/* 更新 DB */
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COLUMN_CLASS_NAME, cls.getClassName());
		cv.put(DBHelper.COLUMN_CLASS_REF_APID, cls.getAPID());
		cv.put(DBHelper.COLUMN_CLASS_GRADE, cls.getGradeYear());
		
		int rowNum = mDB.update(DBHelper.TABLE_CLASS, cv ,  DBHelper.COLUMN_CLASS_ID + "=" + cls.getClassID(), null);
		return rowNum;
	}

	public void deleteAP(ClassInfo cls) {
		String whereClaus = DBHelper.COLUMN_CLASS_ID + "=" + cls.getClassID() + " and " + DBHelper.COLUMN_CLASS_REF_APID + "=" + cls.getAPID();
		mDB.delete(DBHelper.TABLE_APPLICATION, whereClaus , null);
	}
	
	
	
}
