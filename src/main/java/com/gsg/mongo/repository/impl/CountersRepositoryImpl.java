package com.gsg.mongo.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.gsg.mongo.model.master.Counters;
import com.gsg.mongo.repository.CountersRepositoryCustom;

public class CountersRepositoryImpl implements CountersRepositoryCustom {
	@Autowired
	MongoOperations mongoOps;

	@Override
	public void resetAllCounter() {
		mongoOps.updateMulti(Query.query(Criteria.where("id").all()),Update.update("seq", 0), Counters.class);
		//First(Query.query(Criteria.where("id").is("Joe")), Update.update("age", 35), Counter.class);
		
	}
	
	@Override
	public Counters findAndModifySeq(String key){
		return mongoOps.findAndModify(Query.query(Criteria.where("id").is(key)), new Update().inc("seq", 1), Counters.class);
	}
	
}
