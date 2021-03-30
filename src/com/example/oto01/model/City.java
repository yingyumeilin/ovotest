package com.example.oto01.model;
/**
 * 城市
 * @author lqq
 *
 */
public class City {

	/**
  id	int	城市id
  name	string	城市�?
  pid	int	父级id
  path	string	id路径
  longitude	float	经度
  latitude	float	纬度
  level	int	城市等级

	 */
	private int id;
	private String  name;
	private int pid;
	private String path;
	private float longitude;
	private float latitude;
	private int level;
	
	
	public City(int id, String name, int pid, String path, float longitude,
			float latitude, int level) {
		super();
		this.id = id;
		this.name = name;
		this.pid = pid;
		this.path = path;
		this.longitude = longitude;
		this.latitude = latitude;
		this.level = level;
	}
	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", pid=" + pid + ", path="
				+ path + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", level=" + level + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
	
}
