package tw.com.ischool.teachertoolkit;

import java.util.ArrayList;
import java.util.List;

import tw.com.ischool.oauth2signin.APInfo;
import tw.com.ischool.oauth2signin.AccessToken;
import tw.com.ischool.oauth2signin.SignInActivity;
import tw.com.ischool.oauth2signin.User;
import tw.com.ischool.oauth2signin.util.Util;
import tw.com.ischool.teachertoolkit.StudentListFragment.SelectStudentListener;
import tw.com.ischool.teachertoolkit.db.APDataSource;
import tw.com.ischool.teachertoolkit.db.ClassDataSource;
import tw.com.ischool.teachertoolkit.db.StudentDataSource;
import tw.com.ischool.teachertoolkit.remote.StudentSAO;
import tw.com.ischool.teachertoolkit.remote.StudentSAO.onLoadStudentListener;
import tw.com.ischool.teachertoolkit.vo.APTempData;
import tw.com.ischool.teachertoolkit.vo.ClassInfo;
import tw.com.ischool.teachertoolkit.vo.ClassTempData;
import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import tw.com.ischool.teachertoolkit.vo.StudentsTempData;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private final static String fragmemtTag = "StudentFragmentTag";

	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private List<APInfo> mAPs = new ArrayList<APInfo>(); // 目前使用者的 AP 清單
	private int mCurrentAPIndex = 0; // 同步到第幾個 AP

	private APDataSource mdsAP;
	private ClassDataSource mdsClass;
	private StudentDataSource mdsStudent;
	
	private StudentListFragment mStudFrag;
	
	private ProgressDialog barProgressDialog;	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// prepare 資料，初始化 SQL Tables，並讀出 Temp Data */
		initSQLiteDataSource();
		if (savedInstanceState == null)  //第一次開啟 Activity。
			reloadDBData();
		
		initListViewDrawer();
		initFragment();
		
		
		/*
		 * 1. 判斷是否 DB 中有資料？ 1.1 如果有資料，則顯示學生清單 1.2 如果沒有資料，判斷是否有網路，好進行同步資料 1.2.1
		 * 如果有網路，則進行同步資料作業 1.2.2 如果沒有網路，則提示使用者開啟網路好進行同步作業。
		 */
		List<APInfo> aps = mdsAP.getAPList();
		if (aps.size() > 0) // DB 中有資料
		{
			
		}
		else {	//如果沒有資料，判斷是否有網路
			/*  如果沒有資料，則提示使用者開啟網路，好進行同步資料作業 */
			if (!Util.isNetworkOnline(getApplicationContext())) {
				Toast.makeText(getBaseContext(), "請開啟網路，好進行同步資料作業", Toast.LENGTH_LONG).show();
			}
			else {	//有網路，則進行同步作業
				doSyncData();
			}
		}
		
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_refreshData:
			syncAllData();
			return true;
		case R.id.action_Singout :
			signOut();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		mdsAP.closeDatabase();
		mdsClass.closeDatabase();
		mdsStudent.closeDatabase();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mdsAP.openDatabase();
		mdsClass.openDatabase();
		mdsStudent.openDatabase();
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SignInActivity.ACTION_REQUESTCODE) {
			if (resultCode == RESULT_OK) {
				User user = User.get();
				mAPs = user.getApplications();

				/* 檢查 local db 中是否有紀錄，如果沒有，就同步資料 */
				showLog("檢查 local db 中是否有紀錄，如果沒有，就同步資料");
				APDataSource dsAP = new APDataSource(MainActivity.this);
				dsAP.openDatabase();
				List<APInfo> aps = dsAP.getAPList();
				dsAP.closeDatabase();
				if (aps.size() < 1) {
					syncAllData();
				}
			} else {
				String errMsg = (data == null) ? "使用者取消登入動作" : data.getExtras()
						.getString(SignInActivity.SIGNIN_ERROR_MESSAGE);
				showLog(errMsg);
			}
		}
	}

	/*
	 * 同步資料，首先要檢查是否登入系統
	 * 1. 若已經登入，則開啟 ProgressBarActivity 進行同步資料
	 * 2. 若尚未登入，則開啟 SignInActivity 進行登入動作。
	 * 		2.1 若登入成功，則開啟 ProgressBarActivity 進行同步資料
	 * 		2.2 若登入失敗，則出現警示使用者 (5 秒後關閉)，並關閉 SignInActivity，回到主畫面
	 */
	private void doSyncData() {
		/* 判斷是否登入 */
		showLog("MainActivity.doSyncData() ..... ");

		if (AccessToken.getCurrentAccessToken() == null) {
			showLog("No AccessToken found, invoke MainActivity.signIn() ..... ");
			signIn();
		} else {
			User user = User.get();
			showLog("Hello, " + user.getFirstName() + "..... ");
			syncAllData();	
		}
	}

	public void signIn() {
		showLog("MainActivity.signIn() ..... startActivityForResult( ....); ");
		Intent i = new Intent(this, SignInActivity.class);
		startActivityForResult(i, SignInActivity.ACTION_REQUESTCODE);
	}

	public void signOut() {
		Intent i = new Intent(this, SignInActivity.class);
		i.putExtra(SignInActivity.ACTION_TYPE,
				SignInActivity.ACTION_TYPE_SIGNOUT);
		startActivityForResult(i, SignInActivity.ACTION_REQUESTCODE);
	}
	


	private void initSQLiteDataSource() {
		mdsAP = new APDataSource(MainActivity.this);
		mdsAP.openDatabase();

		mdsClass = new ClassDataSource(MainActivity.this);
		mdsClass.openDatabase();

		mdsStudent = new StudentDataSource(MainActivity.this);
		mdsStudent.openDatabase();
		
	}
	
	private void reloadDBData() {
		APTempData.setTempAPList(mdsAP.getAPList());
		ClassTempData.setTempClassList(mdsClass.getClassList());
		StudentsTempData.setTempStudents(mdsStudent.getAllStudents());
	}
	
	private void initFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		mStudFrag = (StudentListFragment)fragmentManager.findFragmentByTag(fragmemtTag);
		FragmentTransaction trans = fragmentManager.beginTransaction();
		if (mStudFrag == null) {
			mStudFrag = StudentListFragment.newInstance();
			mStudFrag.setRetainInstance(true);
			mStudFrag.setOnSelectStudentListener(new SelectStudentListener() {
				
				@Override
				public void onSelect(StudentInfo stud) {
					Intent i = new Intent(MainActivity.this, StudentDetailActivity.class);
					i.putExtra(StudentDetailActivity.PARAM_APID, stud.getAPID());
					i.putExtra(StudentDetailActivity.PARAM_STUDENT_ID, stud.getStudentID());
					startActivity(i);
					
				}
			});
			trans.add(R.id.content_frame, mStudFrag, fragmemtTag).commit();
		}
		else {
			trans.attach(mStudFrag).commit();
		}
	}

	private void syncAllData() {
		showLog("準備開始同步資料 ...");
		mCurrentAPIndex = 0;
		
		onResume();	//makesure all datasource is opened.

		// save applications
		for (APInfo ap : User.get().getApplications()) {
			APInfo apInDB = APTempData.getAPByID(ap.getID() + "");

			if (apInDB == null) {
				mdsAP.insertAP(ap);
				showLog("新增 AP：" + ap.getFullName() + " ...");
			} else {
				mdsAP.updateAP(ap);
				showLog("修改 AP：" + ap.getFullName() + " ...");
			}
		}

		// 同步所有資料
		loadAllData();
	}

	private void loadAllData() {
		if (barProgressDialog == null)
			barProgressDialog = new ProgressDialog(MainActivity.this);
        barProgressDialog.setTitle("同步學生資料 ...");
        barProgressDialog.setMessage("Download in progress ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(10);
        barProgressDialog.show();

		
		if (mCurrentAPIndex >= mAPs.size()) {
			//同步資料完畢：
			//1. 重新載入 Cache 資料
			reloadDBData();
			//2. 更新 Drawer
			initListViewDrawer();
			//3. 更新學生 Fragment的清單，會顯示所有學生
			if (mStudFrag != null)
				mStudFrag.refreshUI(true);
			setTitle(mDrawerTitle);
			//4. 消除 ProgressBar
			barProgressDialog.dismiss();
			
			return;
		}

		final APInfo ap = mAPs.get(mCurrentAPIndex);
		showLog("開始同步 AP：" + ap.getFullName() + " 的資料 ...");
		StudentSAO studSAO = new StudentSAO();
		studSAO.loadAllStudents(ap, new onLoadStudentListener() {

			@Override
			public void onStudentsLoadCompleted(List<StudentInfo> students) {
				showLog("已經完成下載 AP：" + ap.getFullName() + " 的學生資料資料，共 "
						+ students.size() + " 人");
				mCurrentAPIndex += 1;
				loadAllData(); // 遞迴
			}

			@Override
			public void onFail(ClassInfo theClass, Exception ex) {
				showLog("下載 AP：" + ap.getFullName() + " 的學生資料資料失敗，原因為 "
						+ ex.getLocalizedMessage(), true);
				mCurrentAPIndex += 1;
				loadAllData(); // 遞迴
			}

			@Override
			public void onClassStudentsLoaded(int classIndex,
					List<ClassInfo> classes, List<StudentInfo> students) {
				ClassInfo cls = classes.get(classIndex);
				showLog("已經完成下載 班級：" + cls.getClassName() + " 的學生資料資料，共 "
						+ students.size() + " 人");

				// Save Students to SQLite
				for (StudentInfo stud : students) {
					stud.setClassID(cls.getClassID());
					stud.setAPID(ap.getID() + "");
					
					StudentInfo studInDB = StudentsTempData.getStudentByID(
							ap.getID() + "", stud.getStudentID());
					if (studInDB == null) {
						showLog("新增 學生：" + stud.getStudentID() + stud.getName()
								+ " ...");
						mdsStudent.insertStudent(stud);
					} else {
						showLog("修改 學生：" + stud.getStudentID() + stud.getName()
								+ " ...");
						stud.setSysID(studInDB.getSysID());
						mdsStudent.updateStudent(stud);
					}
				}
				barProgressDialog.setMessage( cls.getClassName() );
				barProgressDialog.setProgress(classIndex+1);
			}

			@Override
			public void onClassLoaded(List<ClassInfo> classes) {
				showLog("已經完成下載 AP：" + ap.getFullName() + " 的班級資料資料，共 "
						+ classes.size() + " 班");

				// Save to SQLite
				for (ClassInfo ci : classes) {
					ci.setAPID(ap.getID() + "");
					ClassInfo ciInDB = ClassTempData.getClassByID(ci.getAPID(),
							ci.getClassID());
					if (ciInDB == null) {
						showLog("新增 班級：" + ci.getClassName() + " ...");
						mdsClass.insertClass(ci);
					} else {
						showLog("修改 班級：" + ci.getClassName() + " ...");
						mdsClass.updateClass(ci);
					}
				}
				barProgressDialog.setMessage("準備中 ....");
		        barProgressDialog.setMax(classes.size());
		        barProgressDialog.setProgress(1);
		        
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		//configure SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	/*
	 * Debug 顯示訊息用的
	 */
	private void showLog(String msg) {
		showLog(msg, false);
	}

	private void showLog(String msg, boolean isError) {
		if (isError)
			Log.e(SignInActivity.DEBUG_TAG, msg);
		else
			Log.d(SignInActivity.DEBUG_TAG, msg);
	}


	/* 初始化 Drawer 的資料 */
	private void initListViewDrawer() {
		
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		//準備項目資料
		final List<ItemData> data = new ArrayList<ItemData>();
		ItemData apItem = null;
		for(APInfo ap : APTempData.getTempAPList()) {
			apItem = new ItemData(true,ap, null,0, "");
			data.add(apItem);
			int studCount = 0;
			
			for(ClassInfo cls : ClassTempData.getClassListByAPID(ap.getID()+"")) {
				int clsStudCount = StudentsTempData.getStudentsByClass(cls).size();
				ItemData clsItem = new ItemData(false, null, cls, 0, clsStudCount +"");
				data.add(clsItem);
				studCount += clsStudCount ;
			}
			apItem.setExtraMsg(studCount +"");
		}
		
		DrawerListAdapter adapter = new DrawerListAdapter(MainActivity.this,
				data);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				ItemData item = data.get(position);
				
				if (item.isSection()) {
					mStudFrag.setAPInfo(item.getAP(), item.getTitle());
				}
				else {
					mStudFrag.setClassInfo(item.getCls(), item.getTitle());
				}

				// Highlight the selected item, update the title, and close the
				// drawer
				mDrawerList.setItemChecked(position, true);
				setTitle(item.getTitle());
				mDrawerLayout.closeDrawer(mDrawerList);

			}
		});

	}

	/*
	 * Menu 的資料項目
	 */
	private class ItemData {

		private boolean mSection;
		private String mTitle;
		private int mImageResourceID;
		private APInfo mAP;
		private ClassInfo mClass;
		private String mExtraMsg;
		

		public ItemData(boolean isSection, APInfo ap, ClassInfo cls, int imgResourceID, String extraMsg) {
			mSection = isSection;
			mImageResourceID = imgResourceID;
			mAP = ap;
			mClass = cls;
			mExtraMsg = extraMsg ;
		}

		public boolean isSection() {
			return mSection;
		}

		public void setSection(boolean section) {
			mSection = section;
		}

		public String getTitle() {
			String result = "";
			if (this.isSection()) {
				if (mAP != null)
					result = mAP.getFullName() ;
			}
			else {
				if (mClass != null)
					result = mClass.getClassName();
			}
			
			return (result += (" ( " + mExtraMsg + " )") );
		}

		public int getImageResourceID() {
			return mImageResourceID;
		}

		public void setImageResourceID(int imageResourceID) {
			mImageResourceID = imageResourceID;
		}

		public APInfo getAP() {
			return mAP;
		}

		public void setAP(APInfo aP) {
			mAP = aP;
		}

		public ClassInfo getCls() {
			return mClass;
		}

		public void setCls(ClassInfo class1) {
			mClass = class1;
		}

		public String getExtraMsg() {
			return mExtraMsg;
		}

		public void setExtraMsg(String extraMsg) {
			mExtraMsg = extraMsg;
		}
		

	}

	private class DrawerListAdapter extends ArrayAdapter<ItemData> {

		private List<ItemData> mItems;
		private Context mContext;

		public DrawerListAdapter(Context context, List<ItemData> data) {
			super(context, android.R.layout.simple_list_item_1, data);
			mContext = context;
			mItems = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vw = null;
			ItemData di = mItems.get(position);
			if (di.isSection()) {
				vw = inflater.inflate(R.layout.list_item_header, null);
				TextView txt = (TextView) vw
						.findViewById(R.id.txtListHeaderCaption);
				txt.setText(di.getTitle());
			} else {
				vw = inflater.inflate(R.layout.list_item_body, null);
				TextView txt = (TextView) vw
						.findViewById(R.id.txtListBodyCaption);
				txt.setText(di.getTitle());
			}

			return vw;
		}

	}

}
