package com.example.oto01.model;

import java.util.List;

public class SortManager {

	private String res;
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<list> typelist;

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public List<list> getTypelist() {
		return typelist;
	}

	public void setTypelist(List<list> typelist) {
		this.typelist = typelist;
	}

	public class list {
		private String id;
		private String shopsid;
		private String name;
		private String pid;
		private String path;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getShopsid() {
			return shopsid;
		}

		public void setShopsid(String shopsid) {
			this.shopsid = shopsid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

	}

}
