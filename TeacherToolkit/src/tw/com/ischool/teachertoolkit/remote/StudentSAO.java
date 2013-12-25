package tw.com.ischool.teachertoolkit.remote;

import ischool.dsa.client.ContractConnection;
import ischool.dsa.utility.DSRequest;
import ischool.dsa.utility.DSResponse;
import ischool.dsa.utility.XmlHelper;
import ischool.dsa.utility.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import tw.com.ischool.oauth2signin.APInfo;
import tw.com.ischool.oauth2signin.AccessToken;
import tw.com.ischool.oauth2signin.util.DSAUtil;
import tw.com.ischool.oauth2signin.util.DSConnectionPooling;
import tw.com.ischool.oauth2signin.util.DSConnectionPooling.GetConnectionListener;
import tw.com.ischool.teachertoolkit.remote.ClassSAO.onGetClassListListener;
import tw.com.ischool.teachertoolkit.vo.ClassInfo;
import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import android.os.AsyncTask;
import android.util.Log;

/*
 * 專門從伺服器讀取學生資料
 */
public class StudentSAO {

	private onFindStudentListener mListener; // 查詢學生資料的 listener
	private onLoadStudentListener mLoadStudentListener; // 載入所有學生資料的 listener
	// 取得所有學生資料用的
	private List<ClassInfo> mClasses; // 所有班級的清單
	private int mCurrentClassIndex; // 目前讀到哪一個班級索引值
	private List<StudentInfo> mStudents; // 所有學生的清單
	
	private ContractConnection mCn ;	//DSA 連線
	private APInfo mAP;		//AP Info

	public void loadAllStudents(APInfo ap, onLoadStudentListener listener) {
		mLoadStudentListener = listener;
		mAP = ap;
		// 1. 取得班級清單
		ClassSAO classDAO = new ClassSAO();
		classDAO.getClassList(ap, new onGetClassListListener() {

			@Override
			public void onSuccess(List<ClassInfo> classes) {
				if (mLoadStudentListener != null)
					mLoadStudentListener.onClassLoaded(classes);

				mClasses = classes;
				mCurrentClassIndex = 0;
				mStudents = new ArrayList<StudentInfo>();
				getStudents();
			}

			@Override
			public void onFail(Exception ex) {
				Log.d("DEBUG", "取得班級清單錯誤、、、" + ex.getLocalizedMessage());
				mLoadStudentListener.onFail(null, ex);
			}
		});
	}

	/* 以循序(遞迴)方式取得每一個班的學生名單 */
	private void getStudents() {
		if (mCurrentClassIndex >= mClasses.size()) { // 如果執行完所有班級了
			Log.d("DEBUG",
					"共取得 " + mClasses.size() + "個班，以及 " + mStudents.size()
							+ " 位學生");
			mLoadStudentListener.onStudentsLoadCompleted(mStudents);

			return; // 中斷函數
		}

		ClassInfo cls = mClasses.get(mCurrentClassIndex); // 取得目前獨到的班級物件
		StudentSAO studSAO = new StudentSAO();
		studSAO.getStudentsByClassID(cls.getClassID(), mAP,
				new onFindStudentListener() {

					@Override
					public void onSuccess(List<StudentInfo> studs) {

						mLoadStudentListener.onClassStudentsLoaded(
								mCurrentClassIndex, mClasses, studs);

						for (StudentInfo stud : studs)
							mStudents.add(stud);

						mCurrentClassIndex += 1;
						getStudents();
					}

					@Override
					public void onFail(Exception ex) {
						mCurrentClassIndex += 1;
						getStudents();
					}
				});

	}

	/* 根據班級 ID 取得全班學生資料 */
	private void getStudentsByClassID(String classID,
			APInfo ap,
			onFindStudentListener listener) {
		mListener = listener;

		final DSRequest req = new DSRequest();
		Element reqelm = XmlHelper.parseXml("<Request><ClassID>" + classID
				+ "</ClassID><RecordCount>100</RecordCount></Request>");
		req.setContent(reqelm);

		final ContractConnection conn = DSAUtil.getDefaultConnection();
		DSConnectionPooling.get().getConnection(
				ap.getApName(), 
				ServiceConstant.targetContract, 
				AccessToken.getCurrentAccessToken(),
				new GetConnectionListener() {

					@Override
					public void onSuccess(ContractConnection cn) {
						mCn = cn;
						(new DSAsyncTask()).execute(req);
					}

					@Override
					public void onFail(Exception ex) {
						Log.d("DEBUG", "取得 DSA 連線" + mAP.getApName() + " 失敗： ........ !");
					}
					
				});
		
		
	}

	private void parseResult(DSResponse resp) {
		List<StudentInfo> reqs = new ArrayList<StudentInfo>();

		if (resp != null) {
			Element elmRoot = resp.getBody();
			// Log.d("DEBUG", XmlHelper.convertToString(elmRoot));

			List<Element> elms = XmlUtil.selectElements(elmRoot, "Student");
			for (Element elm : elms) {
				reqs.add(new StudentInfo(elm));
			}
		}

		if (mListener != null)
			mListener.onSuccess(reqs);
	}

	public interface onFindStudentListener {
		void onSuccess(List<StudentInfo> studs);

		void onFail(Exception ex);
	}

	/*
	 * 定義讀取伺服器學生資料的 Listener 介面
	 */
	public interface onLoadStudentListener {

		// 當取得班級清單之後觸發
		void onClassLoaded(List<ClassInfo> classes);

		// 當每一個班級的學生取得完成後，都會觸發。
		void onClassStudentsLoaded(int classIndex, List<ClassInfo> classes,
				List<StudentInfo> students);

		// 當所有班級的學生都載入完成後，就會觸發。
		void onStudentsLoadCompleted(List<StudentInfo> students);

		// 當某一個班級取得學生資料發生錯誤，就會觸發。
		void onFail(ClassInfo theClass, Exception ex);
	}

	private class DSAsyncTask extends AsyncTask<DSRequest, Void, DSResponse> {

		@Override
		protected DSResponse doInBackground(DSRequest... reqs) {
			ContractConnection conn = mCn;

			DSRequest req = reqs[0];
			Log.d("DEBUG", "Start Async Task ........ !");
			DSResponse rsp = null;
			try {
				Log.d("DEBUG", "Start get response ........ !");
				rsp = conn.sendRequest(
						ServiceConstant.Service_Student_FindStudents, req);
			} catch (Exception ex) {
				Log.d("DEBUG", "Get Exception ......... !");
				ex.printStackTrace();
				if (mListener != null)
					mListener.onFail(ex);
			}
			return rsp;
		}

		protected void onPostExecute(DSResponse result) {
			parseResult(result);
		};
	};

}
