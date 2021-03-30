package com.example.oto01.model;

import java.io.Serializable;
/**
 * 商品�?
 * @author lqq
 *
 */
public class VersionInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String filepath;
	private String updateinfo;
	private String versioncode;
	private String versionname;
	private String is_update;
	
	
	
	public VersionInfo(String filepath, String updateinfo, String versioncode , String versionname , String is_update) {
		super();
		this.filepath = filepath;
		this.updateinfo = updateinfo;
		this.versioncode = versioncode;
		this.versionname = versionname;
		this.is_update = is_update;
	}
	public VersionInfo(){
		super();
	}
	
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getUpdateinfo() {
		return updateinfo;
	}
	public void setUpdateinfo(String updateinfo) {
		this.updateinfo = updateinfo;
	}
	public String getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public String getIs_update() {
		return is_update;
	}
	public void setIs_update(String is_update) {
		this.is_update = is_update;
	}
	
}