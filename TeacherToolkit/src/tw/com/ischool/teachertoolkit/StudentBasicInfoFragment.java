package tw.com.ischool.teachertoolkit;

import java.util.ArrayList;
import java.util.List;

import tw.com.ischool.teachertoolkit.util.StringUtil;
import tw.com.ischool.teachertoolkit.vo.StudentInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentBasicInfoFragment extends StudentDetailBaseFragment {
	
	private ListView listdetail;
	private List<DataItem> mItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.d("DEBUG", "StudentBasicInfoFragment.onCreateView");
		
		View vw = inflater.inflate(R.layout.fragment_student_basic_info, container, false);
		
		listdetail = (ListView) vw.findViewById(R.id.detail_list);
		listdetail.setDivider(null); // 去除行與行之間的黑線
		listdetail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				DetailAdapter adapter = (DetailAdapter)listdetail.getAdapter();
				DataItem di = adapter.getItem(position);
				takeAction(di);
				
			}
		});
		
		if (mStud != null) {
			setTextViewContent(R.id.student_txtName, mStud.getName(), vw);
			setTextViewContent(R.id.student_txtClass, mStud.getClassName(), vw);
			setTextViewContent(R.id.student_txtSeat, mStud.getSeatNo(), vw);
			setTextViewContent(R.id.student_txtGender, mStud.getGender(), vw);
			setTextViewContent(R.id.student_txtID, mStud.getStudentNumber(), vw);
			
			//Photo
			ImageView img = (ImageView) vw.findViewById(R.id.student_picture);
			Bitmap bitmap = mStud.getFreshmanPhoto();
			if (bitmap == null) {
				int resource_id = (mStud.getGender() == "男") ? R.drawable.boy
						: R.drawable.girl;
				bitmap = BitmapFactory.decodeResource(getResources(),
						resource_id);
			}
			img.setImageBitmap(bitmap);
			
			//Other Information
			fillListData(mStud);
			
		}
		
		
		return vw;
	}
	
	private void takeAction(DataItem di) {
		if (di.getItemType() == ItemType.Phone) {
			String telno = "tel:" + di.getContent();
			Intent intentDial = new Intent(Intent.ACTION_CALL);
			intentDial.setData(Uri.parse(telno));
			startActivity(intentDial);
		} else if (di.getItemType() == ItemType.Address) {
//			Intent googlemap = new Intent(getActivity(),
//					GoogleMapActivity.class);
//			googlemap.putExtra(GoogleMapActivity.PARAM_STUDENTNAME,
//					mStud.getName());
//			googlemap
//					.putExtra(GoogleMapActivity.PARAM_ADDRESS, di.getContent());
//
//			startActivity(googlemap);

		}else if(di.getItemType() == ItemType.Nothing){
			Toast.makeText(getActivity(), "Nothing", Toast.LENGTH_LONG).show();
		}
	}
	
	private void fillListData(StudentInfo targetStudent) {
		
		Log.d("DEBUG", "StudentBasicInfoFragment.fillListData");
		
		List<DataItem> items = new ArrayList<DataItem>();
		/*------------------------------聯絡人電話Item----------------------------------------*/
		items.add(new DataItem(true, "聯絡方式", ""));
		// 如果他不是空值或是空字串，則執行填入內容，如果他是空值或是空字串，則跳開不填入資料。
		if (!StringUtil.isNullOrWhitespace(targetStudent.getPerminentPhone()))
			items.add(new DataItem(false, targetStudent.getPerminentPhone(),
					"戶籍電話", ItemType.Phone));
		if (!StringUtil.isNullOrWhitespace(targetStudent.getContactPhone()))
			items.add(new DataItem(false, targetStudent.getContactPhone(),
					"通訊電話", ItemType.Phone));
		if (!targetStudent.getOtherPhones().isEmpty())
			items.add(new DataItem(false, targetStudent.getOtherPhones()
					.getFullPhone(), "其他電話", ItemType.Phone));
		if (!targetStudent.getSMSPhone().isEmpty())
			items.add(new DataItem(false, targetStudent.getSMSPhone(), "手機電話",
					ItemType.Phone));

		/*------------------------------聯絡人地址Item----------------------------------------*/
		if (!targetStudent.getPerminentAddress().equals(""))
			items.add(new DataItem(false, targetStudent.getPerminentAddress()
					.getFullAddress(), "戶籍地址", ItemType.Address));
		if (!targetStudent.getMailingAddress().equals(""))
			items.add(new DataItem(false, targetStudent.getMailingAddress()
					.getFullAddress(), "通訊地址", ItemType.Address));
		/*------------------------------父母親 & 監護人Item------------------------------------*/
		items.add(new DataItem(true, "父母親 & 監護人", ""));
		items.add(new DataItem(false, targetStudent.getFatherName(), "父"));
		items.add(new DataItem(false, targetStudent.getMotherName(), "母"));

		final DetailAdapter adapter = new DetailAdapter(getActivity(), items);
		listdetail.setAdapter(adapter);
	}
	
	private void setTextViewContent(int resourceID, String content, View vw) {
		TextView txt = (TextView)vw.findViewById(resourceID);
		if (txt != null)
			txt.setText(content);
	}
	
	private enum ItemType {
		Phone, Address, Nothing
	}

	/*
	 * Detail內的Item內容 _is_sec確認是否是Handle或著不是 title內容 content標題
	 */
	private class DataItem {

		private final boolean _is_sec;
		private String title;
		private String content;
		private ItemType mItemType;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public DataItem(boolean isSec, String t, String c) {
			this(isSec, t, c, ItemType.Nothing);
		}

		public DataItem(boolean isSec, String c, String t, ItemType itemType) {
			_is_sec = isSec;
			title = t;
			content = c;
			mItemType = itemType;
		}

		public boolean isSection() {
			return _is_sec;
		}

		public ItemType getItemType() {
			return mItemType;
		}

		public void setItemType(ItemType itemType) {
			this.mItemType = itemType;
		}

	}

	private class DetailAdapter extends ArrayAdapter<DataItem> {
		private LayoutInflater mInflator;
		private List<DataItem> mDetailItem;

		public DetailAdapter(Context context, List<DataItem> detailitem) {
			super(context, 0, detailitem);
			this.mDetailItem = detailitem;
			mInflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			final DataItem di = mDetailItem.get(position);
			// 這裡可以用 ListView 中的 addHeaderView 的方式來處理
			if (di.isSection()) {
				v = mInflator
						.inflate(R.layout.item_contact_detail_header, null);
				TextView txtName = (TextView) v.findViewById(R.id.separator);
				txtName.setText(di.getContent());

			} else {
				v = mInflator.inflate(R.layout.item_contact_detail, null);
				TextView txtContext = (TextView) v
						.findViewById(R.id.detail_txtContext);
				TextView txtTitle = (TextView) v
						.findViewById(R.id.detail_txtTitle);

				// View sep = v.findViewById(R.id.separator2);
				// ImageView imageView = (ImageView) v
				// .findViewById(R.id.imagePhoneMap);

				txtContext.setText(di.getContent());
				txtTitle.setText(di.getTitle());

				final ItemType mItemType = di.getItemType();
				Log.d("DEBUG", mItemType + "");

				/*
				 * if (mItemType != null) { sep.setVisibility(View.VISIBLE);
				 * imageView.setVisibility(View.VISIBLE); if (di.getItemType()
				 * == ItemType.Phone) {
				 * imageView.setImageResource(R.drawable.ic_call);
				 * imageView.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { String telno = "tel:"
				 * + di.getContent(); Intent intentDial = new Intent(
				 * Intent.ACTION_CALL); intentDial.setData(Uri.parse(telno));
				 * startActivity(intentDial); } });
				 * 
				 * } else if (di.getItemType() == ItemType.Address) {
				 * imageView.setImageResource(R.drawable.ic_map);
				 * imageView.setOnClickListener(new OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { Intent googlemap =
				 * new Intent( StudentDetailActivity.this,
				 * GoogleMapActivity.class);
				 * 
				 * googlemap.putExtra( GoogleMapActivity.PARAM_ADDRESS,
				 * di.getContent());
				 * 
				 * startActivity(googlemap); } });
				 * 
				 * } } else { sep.setVisibility(View.GONE);
				 * imageView.setVisibility(View.GONE); }
				 */
			}
			return v;
		}
	}
}
