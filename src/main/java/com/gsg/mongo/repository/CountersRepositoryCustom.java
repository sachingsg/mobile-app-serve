package com.gsg.mongo.repository;

import com.gsg.mongo.model.master.Counters;


public interface CountersRepositoryCustom {

	public void resetAllCounter();

	Counters findAndModifySeq(String key);
	
	
}
