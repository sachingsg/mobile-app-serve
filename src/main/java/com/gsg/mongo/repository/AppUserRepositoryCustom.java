package com.gsg.mongo.repository;

import java.util.List;

import com.gsg.mongo.model.AppUser;

public interface AppUserRepositoryCustom {
	
	public List<AppUser> findUsersByRole(String role);

}
