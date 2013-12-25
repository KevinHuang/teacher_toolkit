package tw.com.ischool.oauth2signin;

import org.json.JSONException;
import org.json.JSONObject;

public class APInfo {

	private int mID;
	private String mApName;
	private String mFullName;
	private String mOrigin ;	//account_app, domain_app
	
	public APInfo(JSONObject obj) throws JSONException  {
		
		mID = obj.getInt("id");
		mApName = obj.getString("ap_name");
		mFullName = obj.getString("name");
		mOrigin = obj.getString("origin");
	}
	
	public APInfo(int id, String ap_name, String full_name, String origin) {
		mID = id;
		mApName = ap_name;
		mFullName = full_name;
		mOrigin = origin ;
	}
	
	public int getID() {
		return mID;
	}
	public void setID(int iD) {
		mID = iD;
	}
	public String getApName() {
		return mApName;
	}
	public void setApName(String apName) {
		mApName = apName;
	}
	public String getFullName() {
		return mFullName;
	}
	public void setFullName(String fullName) {
		mFullName = fullName;
	}
	public String getOrigin() {
		return mOrigin;
	}
	public void setOrigin(String origin) {
		mOrigin = origin;
	}
	
	
}
