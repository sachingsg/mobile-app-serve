package com.gsg.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsg.component.ResponseWrapper;
import com.gsg.error.GenericException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.Cart;
import com.gsg.mongo.model.OrderCheckout;
import com.gsg.services.CartService;
import com.gsg.services.OrderService;
import com.gsg.services.ServiceModule;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	ServiceModule serviceModule;
	
	@Autowired
	OrderService orderService;

	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	/*@GetMapping("/{userId}/{orderId}")
	public ResponseEntity<?> buyCartOrder(@PathVariable String userId,@PathVariable String orderId) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, ResourceNotFoundException, GenericException {

		logger.info("OrderController.buyCartOrder()");
		OrderCheckout oc = orderService.buyCartOrders(userId, orderId);

		return new ResponseWrapper<>("Ticket Created", HttpStatus.OK, oc).sendResponse();
	}*/

	@GetMapping("/{orderId}")
	public ResponseEntity<?> getCartOrderByOrderId(@PathVariable String orderId) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, ResourceNotFoundException, GenericException {

		logger.info("OrderController.getCartOrder()");
		List<Cart> cartOrders = cartService.getByOrderId(orderId);

		return new ResponseWrapper<>("Cart details", HttpStatus.OK, cartOrders).sendResponse();
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getCartDetailsOfUser(@PathVariable String userId) throws ResourceNotFoundException{
		logger.info("OrderController.getCartDetailsOfUser()"); 
		List<Cart> cartList = cartService.getByUserId(userId);
		 return new ResponseWrapper<>("User Cart Details", HttpStatus.OK, cartList).sendResponse();
		
	}
	
	@DeleteMapping("/{userId}/{cartId}")
	public ResponseEntity<?> removeUserCart(@PathVariable String userId, @PathVariable String cartId) throws ResourceNotFoundException {
		logger.info("CartController.removeUserCart()");
		List<Cart> cartList = cartService.removeFromUserCart(userId, cartId);
		
		return new ResponseWrapper<>("Cart Details deleted", HttpStatus.OK, cartList).sendResponse();

	}
	
	@GetMapping("/{userId}/{cartId}")
	public ResponseEntity<?> buyCartOrder(@PathVariable String userId,@PathVariable String cartId) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, ResourceNotFoundException, GenericException {

		logger.info("OrderController.buyCartOrder()");
		OrderCheckout oc = serviceModule.buyCartOrders(userId, cartId);

		return new ResponseWrapper<>("OrderCheckout details", HttpStatus.OK, oc).sendResponse();
	}
	
	
	@DeleteMapping("/{cartId}/{position}/remove")
	//@PreAuthorize("hasAuthority('ROLE_ADMIN','ROLE_OPERATION')")
	public ResponseEntity<?> removeItemFromCart(@PathVariable String cartId,@PathVariable int position) throws GenericException, 
								ResourceNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException{
		logger.info("DashboardController.removeServiceFromOrder()");

		Cart cart = serviceModule.removeServiceFromCart(cartId, position);
		return new ResponseWrapper<>("Service Removed.", HttpStatus.OK, cart).sendResponse();
		
	}
}
