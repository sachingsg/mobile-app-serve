package com.gsg.mongo.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appversion")
@TypeAlias("AppVersion")
@Getter
@Setter
public class AppVersion extends Auditable {

	@Id
	private String id;
	private VersionFormat prevVersion = new VersionFormat();
	private VersionFormat curVersion = new VersionFormat();
	private Date lastUpdateDt = new Date();
	
	public AppVersion() {
		// TODO Auto-generated constructor stub
	}

	public AppVersion(VersionFormat prevVersion, VersionFormat curVersion) {
		super();
		this.prevVersion = prevVersion;
		this.curVersion = curVersion;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VersionFormat getPrevVersion() {
		return prevVersion;
	}

	public void setPrevVersion(VersionFormat prevVersion) {
		this.prevVersion = prevVersion;
	}

	public VersionFormat getCurVersion() {
		return curVersion;
	}

	public void setCurVersion(VersionFormat curVersion) {
		this.curVersion = curVersion;
	}

	public Date getLastUpdateDt() {
		return lastUpdateDt;
	}

	public void setLastUpdateDt(Date lastUpdateDt) {
		this.lastUpdateDt = lastUpdateDt;
	}



	@Getter
	@Setter
	public static class VersionFormat {
		private int major = 0;
		private int minor = 0;
		private int patch = 0;

		public VersionFormat() {
			// TODO Auto-generated constructor stub
		}
		public String getVersionString() {
			return this.major + "." + this.minor + "." + this.patch;
		}

		public int getVersionNumber() {
			return this.major * 100 + this.minor * 10 + this.patch;
		}
		public int getMajor() {
			return major;
		}
		public void setMajor(int major) {
			this.major = major;
		}
		public int getMinor() {
			return minor;
		}
		public void setMinor(int minor) {
			this.minor = minor;
		}
		public int getPatch() {
			return patch;
		}
		public void setPatch(int patch) {
			this.patch = patch;
		}

	}

}
