package com.gsg.mongo.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.gsg.mongo.model.AppUser;
import com.gsg.mongo.repository.AppUserRepositoryCustom;

public class AppUserRepositoryImpl implements AppUserRepositoryCustom{

	@Autowired
	MongoOperations mongoOps;


	@Override
	public List<AppUser> findUsersByRole(String role) {
		return mongoOps.find(Query.query(Criteria.where("roles").in(role)), AppUser.class);
	}
	
}
