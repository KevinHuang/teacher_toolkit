package tw.com.ischool.teachertoolkit;

import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import android.support.v4.app.Fragment;

public class StudentDetailBaseFragment extends Fragment {

	protected StudentInfo mStud;

	public StudentInfo getStud() {
		return mStud;
	}

	public void setStud(StudentInfo stud) {
		mStud = stud;
	}
	
	
}
