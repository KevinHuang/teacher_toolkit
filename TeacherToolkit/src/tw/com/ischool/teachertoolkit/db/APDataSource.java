package tw.com.ischool.teachertoolkit.db;

import java.util.ArrayList;
import java.util.List;

import tw.com.ischool.oauth2signin.APInfo;
import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class APDataSource {
	private DBHelper dbhelper = null;
	private SQLiteDatabase mDB;

	/* Constructor */
	public APDataSource(Context context) {
		dbhelper = new DBHelper(context);
	}

	public void openDatabase() {
		mDB = dbhelper.getWritableDatabase();
	}

	public void closeDatabase() {
		dbhelper.close();
	}

	public List<APInfo> getAPList() {
		List<APInfo> aps = new ArrayList<APInfo>();
		String[] columns = { 
				DBHelper.COLUMN_AP_ID, 
				DBHelper.COLUMN_AP_NAME,
				DBHelper.COLUMN_AP_FULLAME, 
				DBHelper.COLUMN_AP_ORIGIN };

		// 讀取資料
		Cursor cursor = mDB.query(DBHelper.TABLE_APPLICATION, columns, null, null,
				null, null, DBHelper.COLUMN_AP_FULLAME);

		// 移到第一筆記錄
		cursor.moveToFirst();

		// 當不是最後一筆記錄之後 (類似 EOF)
		while (!cursor.isAfterLast()) {
			APInfo ap = convertCursorToAP(cursor);
			aps.add(ap);
			cursor.moveToNext();
		}

		return aps;
	}

	private APInfo convertCursorToAP(Cursor cursor) {
		int ap_id = cursor.getInt(0);
		String ap_name = cursor.getString(1);
		String ap_full_name = cursor.getString(2);
		String origin = cursor.getString(3);
		return new APInfo(ap_id, ap_name, ap_full_name, origin);
	}

	public long insertAP(APInfo ap) {
		/* 寫入 DB */
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_AP_ID, ap.getID());
		cv.put(DBHelper.COLUMN_AP_NAME, ap.getApName());
		cv.put(DBHelper.COLUMN_AP_FULLAME, ap.getFullName());
		cv.put(DBHelper.COLUMN_AP_ORIGIN, ap.getOrigin());

		long newId = mDB.insert(DBHelper.TABLE_APPLICATION, null, cv);
		return newId;
	}
	
	public int updateAP(APInfo ap) {
		
		/* 更新 DB */
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COLUMN_AP_NAME, ap.getApName());
		cv.put(DBHelper.COLUMN_AP_FULLAME, ap.getFullName());
		cv.put(DBHelper.COLUMN_AP_ORIGIN, ap.getOrigin());
		
		int rowNum = mDB.update(DBHelper.TABLE_APPLICATION, cv ,  DBHelper.COLUMN_AP_ID + "=" + ap.getID(), null);
		return rowNum;
	}

	public void deleteAP(APInfo ap) {
		mDB.delete(DBHelper.TABLE_APPLICATION, DBHelper.COLUMN_AP_ID + "=" + ap.getID(), null);
	}
	
	
}
