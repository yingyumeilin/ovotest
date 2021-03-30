package com.example.oto01.model;

public class ShopsInfo {

	public int id;
	public String shopsname;
	public String name;
	public String address;
	public String detail_address;
	public double longitude;
	public double latitude;
	public String license;
	public int typeid;
	public String license_number;
	public String identity_number;
	public String logo;
	public String phone;
	public String identity_number_star;
	public String audit_state;
	public String typename;

	public String getAudit_state() {
		return audit_state;
	}

	public void setAudit_state(String audit_state) {
		this.audit_state = audit_state;
	}

	public String getIdentity_number_star() {
		return identity_number_star;
	}

	public void setIdentity_number_star(String identity_number_star) {
		this.identity_number_star = identity_number_star;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShopsname() {
		return shopsname;
	}

	public void setShopsname(String shopsname) {
		this.shopsname = shopsname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetail_address() {
		return detail_address;
	}

	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public String getLicense_number() {
		return license_number;
	}

	public void setLicense_number(String license_number) {
		this.license_number = license_number;
	}

	public String getIdentity_number() {
		return identity_number;
	}

	public void setIdentity_number(String identity_number) {
		this.identity_number = identity_number;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public ShopsInfo() {
		super();
	}

	public ShopsInfo(int id, String shopsname, String name, String address,
			String detail_address, double longitude, double latitude,
			String license, int typeid, String license_number,
			String identity_number, String logo, String phone,
			String identity_number_star, String audit_state, String typename) {
		super();
		this.id = id;
		this.shopsname = shopsname;
		this.name = name;
		this.address = address;
		this.detail_address = detail_address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.license = license;
		this.typeid = typeid;
		this.license_number = license_number;
		this.identity_number = identity_number;
		this.logo = logo;
		this.phone = phone;
		this.identity_number_star = identity_number_star;
		this.audit_state = audit_state;
		this.typename = typename;
	}

}
