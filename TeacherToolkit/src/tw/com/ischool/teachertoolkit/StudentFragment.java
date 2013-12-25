package tw.com.ischool.teachertoolkit;

import java.util.ArrayList;
import java.util.List;

import tw.com.ischool.oauth2signin.APInfo;
import tw.com.ischool.teachertoolkit.vo.APTempData;
import tw.com.ischool.teachertoolkit.vo.ClassInfo;
import tw.com.ischool.teachertoolkit.vo.ClassTempData;
import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import tw.com.ischool.teachertoolkit.vo.StudentsTempData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StudentFragment extends Fragment {

	public static StudentFragment newInstance() {
		return new StudentFragment();
	}

	private ListView mListView;
	private SelectStudentListener mListener;
	private List<StudentInfo> mStudents;
	private String mTitle="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View vw = inflater.inflate(R.layout.fragment_student, container, false);

		mListView = (ListView) vw.findViewById(R.id.listView1);

		refreshUI();

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View vw, int position,
					long id) {

				StudentInfo stud = mStudents.get(
						position);
				if (mListener != null)
					mListener.onSelect(stud);
			}
		});
		return vw;
	}

	private class MyArrayAdapter extends ArrayAdapter<StudentInfo> {
		private Context mContext;
		private List<StudentInfo> mStudents;

		public MyArrayAdapter(Context context, List<StudentInfo> students) {
			super(context, R.layout.fragment_student, students);
			mContext = context;
			mStudents = students;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			StudentInfo stud = mStudents.get(position);

			View vw = convertView;
			if (vw == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vw = inflater
						.inflate(R.layout.list_item_student, parent, false);
			}

			ImageView img = (ImageView) vw.findViewById(R.id.imgStudPhoto);

			Bitmap bitmap = stud.getFreshmanPhoto();
			if (bitmap == null) {
				int resource_id = (stud.getGender() == "男") ? R.drawable.boy
						: R.drawable.girl;
				bitmap = BitmapFactory.decodeResource(getResources(),
						resource_id);
			}
			img.setImageBitmap(bitmap);

			TextView txtStudName = (TextView) vw.findViewById(R.id.txtStudName);
			txtStudName.setText(stud.getName());

			TextView txtClassName = (TextView) vw
					.findViewById(R.id.txtClassName);
			txtClassName.setText(stud.getClassName());

			TextView txtSeatNo = (TextView) vw.findViewById(R.id.txtSeatNo);
			txtSeatNo.setText(stud.getSeatNo());

			TextView txtSchoolName = (TextView) vw
					.findViewById(R.id.txtSchoolName);
			APInfo ap = APTempData.getAPByID(stud.getAPID());
			txtSchoolName.setText(ap.getFullName());

			return vw;
		}
	}

	public void setOnSelectStudentListener(SelectStudentListener listener) {
		mListener = listener;
	}

	public interface SelectStudentListener {
		void onSelect(StudentInfo stud);
	}

	/* public method */

	public void setAPInfo(APInfo ap, String title) {
		if (ap == null)
			return;
		
		mTitle = title;
		
		List<StudentInfo> studs = new ArrayList<StudentInfo>();
		for (ClassInfo cls : ClassTempData.getClassListByAPID(ap.getID() + "")) {
			studs.addAll(StudentsTempData.getStudentsByClass(cls));
		}
		mStudents = studs;
		refreshUI();
	}

	public void setClassInfo(ClassInfo cls, String title) {
		if (cls == null)
			return ;
		
		mTitle = title;
		mStudents = StudentsTempData.getStudentsByClass(cls);
		
		refreshUI();
	}

	public void setQueryString(String queryString) {
		mTitle = "搜尋：" + queryString ;
		mStudents = StudentsTempData.queryStudentsByKeyword(queryString);
		
		refreshUI();
	}

	public void refreshUI(boolean isShowAllStudent) {
		mStudents = StudentsTempData.getTempStudents();
		refreshUI();
	}
	
	public void refreshUI() {
		if (mStudents == null) {	
			mStudents = StudentsTempData.getTempStudents();
		}
		
		if (!mTitle.equals(""))
		{
			getActivity().getActionBar().setTitle(mTitle);
		}
		
		MyArrayAdapter adapter = new MyArrayAdapter(getActivity(), mStudents);
		mListView.setAdapter(adapter);
	}
}
