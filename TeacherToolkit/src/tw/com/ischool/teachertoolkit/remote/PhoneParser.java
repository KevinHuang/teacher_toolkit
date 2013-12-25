package tw.com.ischool.teachertoolkit.remote;

import ischool.dsa.utility.XmlHelper;
import ischool.dsa.utility.XmlUtil;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PhoneParser {
	private String mTargetPhoneString = "";
	private String mPhonelist = "";
	private String mPhoneNumber = "";

	public PhoneParser(String phoneXmlString) {
		mTargetPhoneString = phoneXmlString.replaceAll("&lt", "<").replace(
				"&lt", ">");
	}

	public String getFullPhone() {
		String result = "";
		Element reqelm = XmlHelper.parseXml(mTargetPhoneString);
		if (reqelm != null) {
			NodeList phone = reqelm.getElementsByTagName("Phone");
			if (phone.getLength() > 0) {
				Element elmPhone = (Element) phone.item(0);
				mPhonelist = XmlUtil.getElementText(elmPhone, "Phonelist");
				mPhoneNumber = XmlUtil.getElementText(elmPhone, "PhoneNumber");
				result = mPhonelist + mPhoneNumber;
			}
		}
		return result;
	}

	public String getTargetPhoneString() {
		return mTargetPhoneString;
	}

	public void setTargetPhoneString(String targetPhoneString) {
		mTargetPhoneString = targetPhoneString;
	}

	public String getPhonelist() {
		return mPhonelist;
	}

	public void setPhonelist(String phonelist) {
		mPhonelist = phonelist;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}
	
	public boolean isEmpty(){
		String str = getFullPhone();
		return str.isEmpty();
	}

}
