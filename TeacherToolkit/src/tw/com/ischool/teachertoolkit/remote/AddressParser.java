package tw.com.ischool.teachertoolkit.remote;

import ischool.dsa.utility.XmlHelper;
import ischool.dsa.utility.XmlUtil;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* 		<PermanentAddress>&lt;AddressList&gt;&lt;Address&gt;&lt;ZipCode&gt;302&lt;/ZipCode&gt;&lt;County&gt;新竹縣&lt;/County&gt;&lt;Town&gt;竹北市&lt;/Town&gt;&lt;District&gt;自強四路39號&lt;/District&gt;&lt;Area/&gt;&lt;DetailAddress/&gt;&lt;/Address&gt;&lt;/AddressList&gt;</PermanentAddress> */
public class AddressParser {

	private String mTargetAddressString ="";
	private String mZipCode="";
	private String mCounty ="";
	private String mTown="";
	private String mArea="";
	private String mDistrict ="";
	private String mLatitude="";
	private String mLongitude ="";
	private String mDetailAddress="";
	
	public AddressParser(String addressXmlString) {
		mTargetAddressString= addressXmlString.replaceAll("&lt;", "<").replace("&lt;", ">");
		
	}
	
	public String getFullAddress() {
		String result = "";
		
		if (!mTargetAddressString.startsWith("<"))
			return mTargetAddressString;
		
		Element reqelm = XmlHelper.parseXml(mTargetAddressString);
		if (reqelm != null) {
			NodeList addresses = reqelm.getElementsByTagName("Address");
			if (addresses.getLength() > 0) {
				Element elmAddress = (Element)addresses.item(0);
				mZipCode = XmlUtil.getElementText(elmAddress, "ZipCode");
				mCounty = XmlUtil.getElementText(elmAddress, "County");
				mTown = XmlUtil.getElementText(elmAddress, "Town");
				mArea = XmlUtil.getElementText(elmAddress, "Area");
				mDistrict = XmlUtil.getElementText(elmAddress, "District");
				mLatitude = XmlUtil.getElementText(elmAddress, "Latitude");
				mLongitude = XmlUtil.getElementText(elmAddress, "Longitude");
				mDetailAddress = XmlUtil.getElementText(elmAddress, "DetailAddress");
				
				result = mZipCode + mCounty + mTown + mArea + mDistrict + mDetailAddress ;
			}
		}
		
		
		return result ;
	}

	public String getTargetAddressString() {
		return mTargetAddressString;
	}

	public void setTargetAddressString(String targetAddressString) {
		mTargetAddressString = targetAddressString;
	}

	public String getZipCode() {
		return mZipCode;
	}

	public void setZipCode(String zipCode) {
		mZipCode = zipCode;
	}

	public String getCounty() {
		return mCounty;
	}

	public void setCounty(String county) {
		mCounty = county;
	}

	public String getTown() {
		return mTown;
	}

	public void setTown(String town) {
		mTown = town;
	}

	public String getDistrict() {
		return mDistrict;
	}

	public void setDistrict(String district) {
		mDistrict = district;
	}

	public String getLatitude() {
		return mLatitude;
	}

	public void setLatitude(String latitude) {
		mLatitude = latitude;
	}

	public String getLongitude() {
		return mLongitude;
	}

	public void setLongitude(String longitude) {
		mLongitude = longitude;
	}

	public String getDetailAddress() {
		return mDetailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		mDetailAddress = detailAddress;
	}

		

}
