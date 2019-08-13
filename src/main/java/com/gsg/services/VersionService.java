package com.gsg.services;

import java.util.List;

import com.gsg.error.GenericException;
import com.gsg.mongo.model.AppVersion;
import com.gsg.mongo.model.AppVersion.VersionFormat;

public interface VersionService {
	
	List<AppVersion> getAppVersionDetails();

	void saveNewVersionDetails(VersionFormat appVerDtl) throws GenericException;

	AppVersion getLatestVersionDetail();


	String compareVersion(VersionFormat newVer) throws GenericException;

}
