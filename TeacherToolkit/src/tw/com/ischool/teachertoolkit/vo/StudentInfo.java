package tw.com.ischool.teachertoolkit.vo;

import ischool.dsa.utility.XmlUtil;

import org.w3c.dom.Element;

import tw.com.ischool.oauth2signin.util.Util;
import tw.com.ischool.teachertoolkit.remote.AddressParser;
import tw.com.ischool.teachertoolkit.remote.PhoneParser;
import android.graphics.Bitmap;

/*
 * <Student>
		<StudentID>150674</StudentID>
		<StudentName>王妙文</StudentName>
		<StudentNumber>010136</StudentNumber>
		<SeatNo>45</SeatNo>
		<StudentStatus>1</StudentStatus>
		<FreshmanPhoto/>
		<Gender>女</Gender>
		<ClassID>5593</ClassID>
		<GradeYear>1</GradeYear>
		<ClassName>全一仁</ClassName>
		<GraduatePhoto/>
		<FatherName>王世謙</FatherName>
		<MotherName>柳雅庭</MotherName>
		<CustodianName>王世謙</CustodianName>
		<PermanentPhone>03-5152357</PermanentPhone>
		<ContactPhone>03-5547285</ContactPhone>
		<OtherPhones>&lt;PhoneList&gt;&lt;PhoneNumber/&gt;&lt;PhoneNumber/&gt;&lt;PhoneNumber/&gt;&lt;/PhoneList&gt;</OtherPhones>
		<SMSPhone>0926788889</SMSPhone>
		<PermanentAddress>&lt;AddressList&gt;&lt;Address&gt;&lt;ZipCode&gt;302&lt;/ZipCode&gt;&lt;County&gt;新竹縣&lt;/County&gt;&lt;Town&gt;竹北市&lt;/Town&gt;&lt;District&gt;自強四路39號&lt;/District&gt;&lt;Area/&gt;&lt;DetailAddress/&gt;&lt;/Address&gt;&lt;/AddressList&gt;</PermanentAddress>
		<MailingAddress>&lt;AddressList&gt;&lt;Address&gt;&lt;ZipCode&gt;302&lt;/ZipCode&gt;&lt;County&gt;新竹縣&lt;/County&gt;&lt;Town&gt;竹北市&lt;/Town&gt;&lt;District/&gt;&lt;Area/&gt;&lt;DetailAddress&gt;自強四路39號&lt;/DetailAddress&gt;&lt;/Address&gt;&lt;/AddressList&gt;</MailingAddress>
		<OtherAddresses/>
	</Student>
 * 
 */

public class StudentInfo {
	
	private final static String STUDENTID = "StudentID";
	private final static String STUDENTNUMBER = "StudentNumber";
	private final static String STUDENT_NAME = "StudentName";
	private final static String CLASS_ID = "ClassID";
	private final static String AP_ID = "APID";
	private final static String CLASS_NAME = "ClassName";
	private final static String GENDER = "Gender";
	private final static String SEAT_NO = "SeatNo";
	private final static String FRESHMAN_PHOTO = "FreshmanPhoto";
	private final static String GRADUATE_PHOTO = "GraduatePhoto";
	private final static String FATHER_NAME = "FatherName";
	private final static String MOTHER_NAME = "MotherName";
	private final static String CUSTODIAN_NAME = "CustodianName";
	private final static String PERMANENT_PHONE = "PermanentPhone";
	private final static String CONTACT_PHONE = "ContactPhone";
	private final static String OTHER_PHONES = "OtherPhones";
	private final static String SMS_PHONE = "SMSPhone";
	private final static String PERMINENT_ADDRESS = "PermanentAddress";
	private final static String MAILING_ADDRESS="MailingAddress";
	private final static String OTHER_ADDRESSES ="OtherAddresses";

	private long mSysID;	//the id in SQLite
	private String mStudentID;
	private String mStudentNumber;
	private String mName;
	private String mGender ;
	private String mClassID;
	private String mAPID;
	private String mClassName;
	private String mSeatNo;
	private String mFreshmanPhotoBase64String="";
	private String mGraduatePhotoBase64String="";
	private String mFatherName;
	private String mMotherName;
	private String mCustodianName;
	private String mPerminentPhone;
	private String mContactPhone;
	private String mSMSPhone;
	private String mOtherPhones;
	private String mPerminentAddress;
	private String mMailingAddress;
	private String mOtherAddresses;
	
	public StudentInfo() {
		
	}
	
	public StudentInfo(Element student) {
		if (student == null)
			return  ;
		
		mStudentID = XmlUtil.getElementText(student ,STUDENTID);
		mStudentNumber = XmlUtil.getElementText(student, STUDENTNUMBER);
		mName = XmlUtil.getElementText(student ,STUDENT_NAME);
		mClassID =  XmlUtil.getElementText(student , CLASS_ID);
		mClassName = XmlUtil.getElementText(student ,CLASS_NAME);
		mSeatNo = XmlUtil.getElementText(student ,SEAT_NO);
		mGender = XmlUtil.getElementText(student ,GENDER);
		mFreshmanPhotoBase64String = XmlUtil.getElementText(student,  FRESHMAN_PHOTO);
		mGraduatePhotoBase64String = XmlUtil.getElementText(student,  GRADUATE_PHOTO);
		mFatherName = XmlUtil.getElementText(student,  FATHER_NAME);
		mMotherName = XmlUtil.getElementText(student,  MOTHER_NAME);
		mCustodianName = XmlUtil.getElementText(student,  CUSTODIAN_NAME);
		mPerminentPhone = XmlUtil.getElementText(student,  PERMANENT_PHONE);
		mContactPhone = XmlUtil.getElementText(student,  CONTACT_PHONE);
		mSMSPhone = XmlUtil.getElementText(student,  SMS_PHONE);
		mOtherPhones = XmlUtil.getElementText(student,  OTHER_PHONES);
		mPerminentAddress = XmlUtil.getElementText(student,  PERMINENT_ADDRESS);
		mMailingAddress = XmlUtil.getElementText(student,  MAILING_ADDRESS);
		mOtherAddresses = XmlUtil.getElementText(student,  OTHER_ADDRESSES);

		
	}
	
	
	public String getClassID() {
		return mClassID;
	}

	public void setClassID(String classID) {
		mClassID = classID;
	}

	public String getAPID() {
		return mAPID;
	}

	public void setAPID(String aPID) {
		mAPID = aPID;
	}

	public long getSysID() {
		return mSysID;
	}

	public void setSysID(long sysID) {
		mSysID = sysID;
	}

	public String getStudentNumber() {
		return mStudentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		mStudentNumber = studentNumber;
	}

	public String getFreshmanPhotoBase64String() {
		return mFreshmanPhotoBase64String;
	}
	public Bitmap getFreshmanPhoto() {
		return Util.convertBase64ToBitmap(mFreshmanPhotoBase64String);
	}
	
	public String getStudentID() {
		return mStudentID;
	}
	public void setStudentID(String studentID) {
		mStudentID = studentID;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public String getGender() {
		return mGender;
	}
	public void setGender(String gender) {
		mGender = gender;
	}
	public String getClassName() {
		return mClassName;
	}
	public void setClassName(String className) {
		mClassName = className;
	}
	public String getSeatNo() {
		return (mSeatNo.length() < 2) ?( "0" + mSeatNo) : mSeatNo ;
	}
	public void setSeatNo(String seatNo) {
		mSeatNo = seatNo;
	}
	public String getGraduatePhotoBase64String() {
		return mGraduatePhotoBase64String;
	}
	public void setGraduatePhotoBase64String(String graduatePhotoBase64String) {
		mGraduatePhotoBase64String = graduatePhotoBase64String;
	}
	public String getFatherName() {
		return mFatherName;
	}
	public void setFatherName(String fatherName) {
		mFatherName = fatherName;
	}
	public String getMotherName() {
		return mMotherName;
	}
	public void setMotherName(String motherName) {
		mMotherName = motherName;
	}
	public String getCustodianName() {
		return mCustodianName;
	}
	public void setCustodianName(String custodianName) {
		mCustodianName = custodianName;
	}
	public String getPerminentPhone() {
		return mPerminentPhone;
	}
	public void setPerminentPhone(String perminentPhone) {
		mPerminentPhone = perminentPhone;
	}
	public String getContactPhone() {
		return mContactPhone;
	}
	public void setContactPhone(String contactPhone) {
		mContactPhone = contactPhone;
	}
	public String getSMSPhone() {
		return mSMSPhone;
	}
	public void setSMSPhone(String sMSPhone) {
		mSMSPhone = sMSPhone;
	}
	public PhoneParser getOtherPhones() {
		return new PhoneParser(mOtherPhones);
	}
	public void setOtherPhones(String otherPhones) {
		mOtherPhones = otherPhones;
	}
	public AddressParser getPerminentAddress() {
		return new AddressParser(mPerminentAddress);
	}
	public void setPerminentAddress(String perminentAddress) {
		mPerminentAddress = perminentAddress;
	}
	public AddressParser getMailingAddress() {
		return new AddressParser(mMailingAddress);
	}
	public void setMailingAddress(String mailingAddress) {
		mMailingAddress = mailingAddress;
	}
	public AddressParser getOtherAddresses() {
		
		return new AddressParser(mOtherAddresses);
	}
	public void setOtherAddresses(String otherAddresses) {
		mOtherAddresses = otherAddresses;
	}
	public void setFreshmanPhotoBase64String(String freshmanPhotoBase64String) {
		mFreshmanPhotoBase64String = freshmanPhotoBase64String;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName() ;
	}
	
	
}
