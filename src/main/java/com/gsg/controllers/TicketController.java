package com.gsg.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {/*
	
	private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
	
	@Autowired
	TicketService tktService;
	
	// By svc engr, call center
	@GetMapping("/user/{userId}/assigned")
	//@PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS')")
	public ResponseEntity<?> getTicketsAssignedToAUser(@PathVariable("userId") String userId) throws ResourceNotFoundException {
		
		logger.info("TicketController.getTicketsAssignedToAUser()");
		List<Ticket> ticketList = tktService.getAllTicketsAssignedToUser(userId);

		return new ResponseWrapper<>("List of tickets", HttpStatus.OK, ticketList).sendResponse();
	}
	
	@PutMapping("/{tktId}")
	//@PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS','ROLE_ADMIN')")
	public ResponseEntity<?> updateTicketStatus(@PathVariable String tktId, @RequestBody Ticket tkt) throws RTException, ResourceNotFoundException {
		
		logger.info("TicketController.updateTicketStatus()");
		Ticket ticket = tktService.updateTicketBytktId(tktId, tkt);

		return new ResponseWrapper<>("List of tickets", HttpStatus.OK, ticket).sendResponse();
	}

	@GetMapping
	//@PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS','ROLE_ADMIN')")
	public ResponseEntity<?> getAllTicketsCount(){
		
		logger.info("TicketController.getAllTicketsCount()");
		List<TicketCategory> tktCount = tktService.getAllTicketCount();
		return new ResponseWrapper<>("Ticket Counts", HttpStatus.OK,tktCount).sendResponse();
	}
	
	@GetMapping("/status/{status}")
	//@PreAuthorize("hasAnyAuthority('ROLE_SE','ROLE_CS','ROLE_ADMIN')")
	public ResponseEntity<?> getAllTicketsByStatus(@PathVariable String status){
		
		logger.info("TicketController.getAllTicketsByStatus()");
		List<Ticket> tickets = tktService.getAllTicketsByStatus(status);

		return new ResponseWrapper<>("List of tickets", HttpStatus.OK, tickets).sendResponse();
	}
*/}
