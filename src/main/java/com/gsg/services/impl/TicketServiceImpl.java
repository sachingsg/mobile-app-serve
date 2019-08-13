package com.gsg.services.impl;

import org.springframework.stereotype.Service;

import com.gsg.services.TicketService;

@Service
public class TicketServiceImpl implements TicketService {/*

	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	AppUserService userService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	TicketUtility ticketUtility;

	
	
	///////////////////////
	
	
	@Override
	public List<TicketCategory> getAllTicketCount() {
		logger.info("TicketServiceImpl.getAllTicketCount()");
		
		List<Ticket> tktList = ticketRepository.getAllTickets();
		System.out.println("tktList" + tktList);
		
		//Map<String, Integer> tktCount = new HashMap<String, Integer>();
		
		List<TicketCategory> tktCategoryList = new ArrayList<TicketCategory>();
		
		
		TicketCategory tktCat = null;
		for(Ticket tkt : tktList){
			tktCat = new TicketCategory(tkt.getStatus());
			if(tktCategoryList.contains(tktCat)){
				int ix = tktCategoryList.indexOf(tktCat);
				TicketCategory tempTkt =  tktCategoryList.get(ix);
				tempTkt.setCount(tempTkt.getCount() +1);
			}else{
				tktCategoryList.add(tktCat);
			}
		}
		int totalCount = (int) ticketRepository.count();
		// count all
		tktCategoryList.add(new TicketCategory("ALL",totalCount));
		return tktCategoryList;
	}
	
	@Override
	public List<Ticket> getAllTicketsByStatus(String status) {
		logger.info("TicketServiceImpl.getAllTicketsByStatus()");
		if(status.equals("ALL"))	{
			return ticketRepository.findAll();
		}
		return ticketRepository.findByStatus(status);
	}
	
	
	@Override
	public Ticket saveTicket(Ticket tkt) {

		ticketRepository.save(tkt);

		return tkt;
	}

	@Override
	public Ticket getByTicketId(String ticketId) throws ResourceNotFoundException {
		logger.info("TicketServiceImpl.getByTicketId()");
		Ticket tkt = ticketRepository.findById(ticketId);
		if (ObjectUtils.isEmpty(tkt)) {
			throw new ResourceNotFoundException(Ticket.class, "Ticket", String.valueOf(ticketId));
		}
		return tkt;
	}

	@Override
	public Ticket getTicketByOrderId(String orderId) throws ResourceNotFoundException {

		Ticket tkt = ticketRepository.findByOrderId(orderId);
		if (ObjectUtils.isEmpty(tkt)) {
			throw new ResourceNotFoundException(Ticket.class, "Ticket Order", orderId);
		}

		return tkt;
	}

	@Override
	public Ticket updateTicketBytktId(String tktId, Ticket tkt) throws ResourceNotFoundException {
		logger.info("TicketServiceImpl.updateTicketBytktId()");

		Ticket dbTkt = getByTicketId(tktId);
		tkt.setId(dbTkt.getId());
		// Update tkt status
		ticketRepository.save(tkt);

		// updateRT
		TicketRequest treq = new TicketRequest();
		treq.setStatus(tkt.getStatus());
		treq.setQueue(tkt.getAssignedQueue());

		try {
			ticketUtility.updateRTTicket(tktId, treq);
		} catch (RTException e) {
			e.printStackTrace();
		}

		// update same status to Order

		Order order = orderRepository.findByOrderId(tkt.getOrderId());
		order.setRequestStatus(tkt.getStatus());
		orderRepository.save(order);

		return tkt;

	}

	@Override
	public List<Ticket> getAllTicketsAssignedToUser(String userId) throws ResourceNotFoundException {
		logger.info("TicketServiceImpl.getAllTicketsAssignedTo()");
		userService.getUserByUserID(userId);
		return ticketRepository.findByAssignedToUserId(userId);
	}

	@Override
	public void updateRTdetailsToTicket(String id, String rtDetail) {
		logger.info("TicketServiceImpl.updateRTdetailsToTicket()");

		logger.info("RT Ticket Id :: " + rtDetail);

		Ticket tkt = ticketRepository.findById(id);
		tkt.setTicketId(rtDetail);
		// Saving Rt id to ticket

		ticketRepository.save(tkt);

	}

*/}
