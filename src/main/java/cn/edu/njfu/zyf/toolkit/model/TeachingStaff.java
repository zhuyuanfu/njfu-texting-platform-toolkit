package cn.edu.njfu.zyf.toolkit.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * 用于定期向教职工发送生日祝福短信。
 * @author zyf
 *
 */
public class TeachingStaff {
	private String unchangeableCode;
	private String name;
	private LocalDate birthday;
	private String identityNumber;
	private String mobilePhoneNumber;
	
	public String getUnchangeableCode() {
		return unchangeableCode;
	}
	public void setUnchangeableCode(String unchangeableCode) {
		this.unchangeableCode = unchangeableCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public String getBirthdayString() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return birthday.format(dtf);
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	
	@Override
	public String toString() {
		return "{" + name + ", " + unchangeableCode + ", " + this.getBirthdayString() + "}\n";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof TeachingStaff) {
			TeachingStaff otherTeachingStaff = (TeachingStaff) other;
			if(this.identityNumber != null && otherTeachingStaff.getIdentityNumber() != null) {
				return this.getIdentityNumber().equalsIgnoreCase(otherTeachingStaff.getIdentityNumber());
			} else {
				return false;
			}
		} else {
			return false;
		}
		
	}
}
