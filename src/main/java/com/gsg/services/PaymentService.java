package com.gsg.services;

import com.gsg.error.GenericException;
import com.gsg.error.RTException;
import com.gsg.error.ResourceNotFoundException;
import com.gsg.mongo.model.OrderCheckout;

public interface PaymentService {

	String getPaymentStatus(String referenceNbr) throws ResourceNotFoundException, InterruptedException;

	String processPayment(OrderCheckout oc) throws ResourceNotFoundException, GenericException, RTException;
}
