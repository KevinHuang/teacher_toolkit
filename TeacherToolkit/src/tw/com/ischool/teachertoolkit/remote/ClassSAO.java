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
import tw.com.ischool.oauth2signin.util.DSConnectionPooling;
import tw.com.ischool.oauth2signin.util.DSConnectionPooling.GetConnectionListener;
import tw.com.ischool.teachertoolkit.vo.ClassInfo;
import android.os.AsyncTask;
import android.util.Log;

/* 負責取得班級資料的 Data Access Object */
public class ClassSAO {

	private onGetClassListListener mListener;
	private APInfo mAP;
	
	private ContractConnection mCn;

	public void getClassList(APInfo ap, onGetClassListListener listener) {
		mListener = listener;
		mAP = ap;
		//取得 DSA 連線
		DSConnectionPooling.get().getConnection(
				ap.getApName(), 
				ServiceConstant.targetContract, 
				AccessToken.getCurrentAccessToken(),
				new GetConnectionListener() {

					@Override
					public void onSuccess(ContractConnection cn) {
						mCn = cn;
						ClassAsyncTask asyTask = new ClassAsyncTask();
						asyTask.execute(new DSRequest());
					}

					@Override
					public void onFail(Exception ex) {
						Log.d("DEBUG", "取得 DSA 連線" + mAP.getApName() + " 失敗： ........ !");
						mListener.onFail(ex);
					}
					
				});	
		
		

	}

	/* 定義當取得班級清單後，要被通知的 listener , 所需要實作的interface */
	public interface onGetClassListListener {
		void onSuccess(List<ClassInfo> classes);

		void onFail(Exception ex);
	}

	/* 定義取得班級清單的 AsyncTask 物件 */
	private class ClassAsyncTask extends AsyncTask<DSRequest, Void, DSResponse> {

		@Override
		protected DSResponse doInBackground(DSRequest... reqs) {
			ContractConnection conn = mCn;

			DSRequest req = reqs[0];
			Log.d("DEBUG", "Start Async Task ........ !");
			DSResponse rsp = null;
			try {
				Log.d("DEBUG", "Start get response ........ !");
				rsp = conn.sendRequest(
						ServiceConstant.Service_Student_GetClassList, req);
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

	/* 把取得的班級 XML 字串解析成 ClassInfo 的集合，並回傳給 listener */
	private void parseResult(DSResponse resp) {
		// 1.定義班級的集合物件
		List<ClassInfo> reqs = new ArrayList<ClassInfo>();

		if (resp != null) {

			Element elmRoot = resp.getBody();
			Log.d("DEBUG", XmlHelper.convertToString(elmRoot));

			// 2.解析 班級的 XML 資料為 ClassInfo 物件,並加入 班級的集合物件
			List<Element> elms = XmlUtil.selectElements(elmRoot, "Class");
			for (Element elm : elms) {
				reqs.add(new ClassInfo(elm));
			}
		}

		/* 3.把班級清單回傳給listener */
		if (mListener != null)
			mListener.onSuccess(reqs);

	}

}
