package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.gsg.mongo.model.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {

	Ticket findById(String id);

	Ticket findByTicketId(String tktId);

	List<Ticket> findByAssignedToUserId(String userId);

	List<Ticket> findByStatus(String status);

	//List<Ticket> findByTicketType(String ticketType);

	@Query(value="{}", fields="{status : 1, orderId : 1}")
	List<Ticket> getAllTickets();

	Ticket findByOrderId(String orderId);

}
