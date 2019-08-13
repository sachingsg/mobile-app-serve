package com.gsg.services.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gsg.error.GenericException;
import com.gsg.mongo.model.AppVersion;
import com.gsg.mongo.model.AppVersion.VersionFormat;
import com.gsg.mongo.repository.VersioRepository;
import com.gsg.services.VersionService;

@Service
public class VersionServiceImpl implements VersionService {

	private static final Logger logger = LoggerFactory.getLogger(VersionServiceImpl.class);

	@Autowired
	VersioRepository versioRepository;

	@Override
	public List<AppVersion> getAppVersionDetails() {
		logger.info("VersionServiceImpl.getAppVersionDetails()");
		List<AppVersion> versionList = versioRepository.findAll(new Sort(Direction.DESC, "lastUpdateDt"));
		return versionList;
	}

	@Override
	public void saveNewVersionDetails(VersionFormat newVerDtl) throws GenericException {
		logger.info("VersionServiceImpl.saveNewVersionDetails()");

		List<String> v = Arrays.asList("MAJOR","MINOR","PATCH");
		AppVersion curVerDtl = getLatestVersionDetail();
		
		if (!compareVersion(newVerDtl).isEmpty()) {
			throw new GenericException("Verios details should be new.");
		}

		AppVersion appVer = new AppVersion(curVerDtl.getCurVersion(), newVerDtl);
		versioRepository.save(appVer);
	}

	@Override
	public AppVersion getLatestVersionDetail() {
		logger.info("VersionServiceImpl.getLatestVersionDetail()");

		AppVersion appVer = null;
		if (versioRepository.count() > 0) {
			appVer = versioRepository.findAll(new Sort(Direction.DESC, "lastUpdateDt")).get(0);

		}
		if (ObjectUtils.isEmpty(appVer)) {
			appVer = new AppVersion();
		}
		return appVer;

	}

	@Override
	public String compareVersion(VersionFormat appVer) throws GenericException {
		logger.info("VersionServiceImpl.validateVersion()");

		String verCheck = "";

		if(appVer.getMajor() == 0){
			throw new GenericException("Invalid Version");
		}
		
		VersionFormat latestVer = getLatestVersionDetail().getCurVersion();
		
		if(latestVer.getVersionNumber() == 0){
			verCheck = "MAJOR";
			return verCheck;
		}

		if (appVer.getVersionNumber() == latestVer.getVersionNumber()) {
			verCheck = "NA";
		}else if (latestVer.getMajor() > appVer.getMajor()) {
			verCheck = "MAJOR";
		} else if (latestVer.getMinor() > appVer.getMinor()) {
			verCheck = "MINOR";
		} else if (latestVer.getPatch() > appVer.getPatch()) {
			verCheck = "PATCH";
		}else{
			verCheck = "OLD";
		}

		return verCheck;

	}

}
