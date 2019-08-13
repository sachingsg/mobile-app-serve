package com.gsg.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.GenericException;
import com.gsg.mongo.model.AppVersion;
import com.gsg.mongo.model.AppVersion.VersionFormat;
import com.gsg.services.VersionService;

@RestController
@RequestMapping("/version")
public class VersionController {
	private static final Logger logger = LoggerFactory.getLogger(VersionController.class);
	@Autowired
	VersionService versionService;

	/*@GetMapping
	ResponseEntity<?> getAllVersionDetails() throws Exception {
		
		logger.info("VersionController.getAllVersionDetails()");

		List<AppVersion> verList = versionService.getAppVersionDetails();
		return new ResponseWrapper<>("Version Details", HttpStatus.OK, verList).sendResponse();

	}*/
	
	@GetMapping
	ResponseEntity<?> getLatestVersionDetails() throws Exception {
		
		logger.info("VersionController.getAllVersionDetails()");

		AppVersion ver = versionService.getLatestVersionDetail();
		return new ResponseWrapper<>("Version Details", HttpStatus.OK, ver.getCurVersion()).sendResponse();

	}

	@PostMapping("/check")
	ResponseEntity<?> checkVersion(@RequestBody VersionFormat versionDtl) throws Exception {
		
		logger.info("VersionController.checkVersion()");

		String chkDtl = versionService.compareVersion(versionDtl);
		return new ResponseWrapper<>("Version comparison", HttpStatus.OK, chkDtl).sendResponse();

	}
	
	@PostMapping
	private ResponseEntity<Object> saveNewVersionDetails(@RequestBody VersionFormat appVerDtl) throws GenericException {
		logger.info("VersionController.saveNewVersionDetails()");
		
		versionService.saveNewVersionDetails(appVerDtl);
		
		return new ResponseWrapper<>("Version Details Saved", HttpStatus.OK).sendResponse();
	}
	
}
