package com.gsg.mongo.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.gsg.mongo.repository.TicketRepositoryCustom;

public class TicketRepositoryImpl implements TicketRepositoryCustom {
	@Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void getTicketCount() {
		/*org.springframework.data.mongodb.core.aggregation.GroupOperation groupByStateAndSumPop = group("status")
				  .count().as("count");
				//MatchOperation filterStates = match(new Criteria("statePop").gt(10000000));
				SortOperation sortByPopDesc = sort(new Sort(Direction.DESC, "orderId"));
				 
				Aggregation aggregation = newAggregation(  groupByStateAndSumPop, sortByPopDesc);
				AggregationResults<TicketCategory> result = mongoTemplate.aggregate(
				  aggregation, TicketCategory.class);*/
	}
	
}
