package com.gsg.mongo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class MongoFunctions {

	@Autowired
	private MongoTemplate template;

	public String nextUserSequence(String sequenceType) {
		String seq = "";
		ScriptOperations ops = template.scriptOps();
		Object obj = ops.call("getNextSequence", sequenceType);
		if (!ObjectUtils.isEmpty(obj)) {
			seq = String.valueOf(((Double) obj).intValue());
		}
		return seq;
	}

}
