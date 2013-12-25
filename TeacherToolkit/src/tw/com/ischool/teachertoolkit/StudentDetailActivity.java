package tw.com.ischool.teachertoolkit;

import tw.com.ischool.teachertoolkit.vo.APTempData;
import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import tw.com.ischool.teachertoolkit.vo.StudentsTempData;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class StudentDetailActivity extends FragmentActivity {

	/* Define String Constant for passing parameters to this activity. */
	public final static String PARAM_APID = "StudentDetailActivity.param_apid";
	public final static String PARAM_STUDENT_ID = "StudentDetailActivity.param_studentid";

	/* Define String Constant for Fragment Tag */
	private final static String STUD_BASIC_INFO_FRAGMENT = "StudentBasicInfoFragment";
	private final static String STUD_NOTE_FRAGMENT = "StudentNoteFragment";
	private final static String STUD_SCORE_FRAGMENT = "StudentScoreFrament";
	private final static String STUD_DISCIPLINE_FRAGMENT = "StudentDisciplineFragment";

	private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	private ViewPager mViewPager;

	private StudentBasicInfoFragment mFragStudBasicInfo;
	private StudentNoteFragment mFragStudNote;
	private StudentScoreFragment mFragStudScore;
	private StudentDisciplineFragment mFragStudDiscipline;

	private StudentInfo mStud;
	private String[] mTabTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_detail);

		// define tab titles
		mTabTitles = new String[] { "基本資料", "筆記", "成績", "獎懲", "出缺勤" };

		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between pages, select the
				// corresponding tab.
				getActionBar().setSelectedNavigationItem(position);
				Log.d("DEBUG", "page selected :position" + position);
			}
		});

		// get StudentInfo object
		Intent i = getIntent();
		String ap_id = i.getStringExtra(PARAM_APID);
		String student_id = i.getStringExtra(PARAM_STUDENT_ID);
		mStud = StudentsTempData.getStudentByID(ap_id, student_id);

		String title = mStud.getName() + "(" + mStud.getClassName() + " "
				+ mStud.getSeatNo() + "號, "
				+ APTempData.getAPByID(mStud.getAPID()).getFullName() + ")";
		getActionBar().setTitle(title);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// init tabs
		initTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	private void initTabs() {
		final ActionBar actionBar = getActionBar();

		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// show the given tab
				mViewPager.setCurrentItem(tab.getPosition());
			}

			public void onTabUnselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// hide the given tab
			}

			public void onTabReselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// probably ignore this event
			}

			@Override
			public void onTabReselected(Tab arg0,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(Tab arg0,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabUnselected(Tab arg0,
					android.app.FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}
		};

		// Add 3 tabs, specifying the tab's text and TabListener
		for (int i = 0; i < mTabTitles.length; i++) {
			actionBar.addTab(actionBar.newTab().setText(mTabTitles[i])
					.setTabListener(tabListener));
		}
	}

	// Since this is an object collection, use a FragmentStatePagerAdapter,
	// and NOT a FragmentPagerAdapter.
	public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			FragmentManager mgr = getSupportFragmentManager();
			FragmentTransaction tx = mgr.beginTransaction();

			StudentDetailBaseFragment fragment = null;
			switch (i) {
			case 0:
				fragment = (StudentDetailBaseFragment) mgr
						.findFragmentByTag(STUD_BASIC_INFO_FRAGMENT);
				if (fragment == null) {
					fragment = new StudentBasicInfoFragment();
				}
				break;
			case 1:
				fragment = (StudentDetailBaseFragment) mgr
						.findFragmentByTag(STUD_NOTE_FRAGMENT);
				if (fragment == null) {
					fragment = new StudentNoteFragment();
				}
				break;
			case 2:
				fragment = (StudentDetailBaseFragment) mgr
						.findFragmentByTag(STUD_NOTE_FRAGMENT);
				if (fragment == null) {
					fragment = new StudentNoteFragment();
				}
				break;
			case 3:
				fragment = (StudentDetailBaseFragment) mgr
						.findFragmentByTag(STUD_SCORE_FRAGMENT);
				if (fragment == null) {
					fragment = new StudentScoreFragment();
				}
				break;
			default:
				fragment = (StudentDetailBaseFragment) mgr
						.findFragmentByTag(STUD_DISCIPLINE_FRAGMENT);
				if (fragment == null) {
					fragment = new StudentDisciplineFragment();
				}
				break;
			}

			fragment.setStud(mStud);

			return fragment;
		}

		@Override
		public int getCount() {
			return mTabTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (position < mTabTitles.length) ? mTabTitles[position] : "";
		}
	}

}
