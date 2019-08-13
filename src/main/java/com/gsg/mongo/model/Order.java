package com.gsg.mongo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "order")
@JsonInclude(Include.NON_NULL)
@TypeAlias("Order")
@Getter
@Setter
public class Order extends Auditable {

	@Id
	private String id;
	private String orderId;
	private String produtType;
	private String invoiceNbr;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate serviceDate = LocalDate.now();

	private double totalPrice; // with Tax
	// Extra
	private String assignedQueue;
	private String assignedToUserId;
	private String requestStatus;
	private String ccUserId;

	private String userId;
	private String userName;
	private String contactNbr;
	private String userFeedBack;

	private List<FeedBack> feedBacks = new ArrayList<FeedBack>();
	private String rtTktId;
	/*
	 * @DBRef private Ticket ticket;
	 */

	@DBRef
	private List<OrderDetails> orderDtls = new ArrayList<OrderDetails>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProdutType() {
		return produtType;
	}

	public void setProdutType(String produtType) {
		this.produtType = produtType;
	}

	public String getInvoiceNbr() {
		return invoiceNbr;
	}

	public void setInvoiceNbr(String invoiceNbr) {
		this.invoiceNbr = invoiceNbr;
	}

	public LocalDate getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(LocalDate serviceDate) {
		this.serviceDate = serviceDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getAssignedQueue() {
		return assignedQueue;
	}

	public void setAssignedQueue(String assignedQueue) {
		this.assignedQueue = assignedQueue;
	}

	public String getAssignedToUserId() {
		return assignedToUserId;
	}

	public void setAssignedToUserId(String assignedToUserId) {
		this.assignedToUserId = assignedToUserId;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getCcUserId() {
		return ccUserId;
	}

	public void setCcUserId(String ccUserId) {
		this.ccUserId = ccUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContactNbr() {
		return contactNbr;
	}

	public void setContactNbr(String contactNbr) {
		this.contactNbr = contactNbr;
	}

	public String getUserFeedBack() {
		return userFeedBack;
	}

	public void setUserFeedBack(String userFeedBack) {
		this.userFeedBack = userFeedBack;
	}

	public List<FeedBack> getFeedBacks() {
		return feedBacks;
	}

	public void setFeedBacks(List<FeedBack> feedBacks) {
		this.feedBacks = feedBacks;
	}

	public String getRtTktId() {
		return rtTktId;
	}

	public void setRtTktId(String rtTktId) {
		this.rtTktId = rtTktId;
	}

	public List<OrderDetails> getOrderDtls() {
		return orderDtls;
	}

	public void setOrderDtls(List<OrderDetails> orderDtls) {
		this.orderDtls = orderDtls;
	}

	// ////////////////////
	@JsonInclude(Include.NON_EMPTY)
	@Getter
	@Setter
	@EqualsAndHashCode(of = { "userType" })
	public static class FeedBack {
		private String comments;
		private int rating;
		private String submitterUserId;
		private String userType;
		// later
		private boolean isApproved;

		public FeedBack() {
			// TODO Auto-generated constructor stub
		}

		public boolean isEmpty() {
			if (comments.isEmpty() && rating == 0) {
				return true;
			}
			return false;

		}

		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}

		public String getSubmitterUserId() {
			return submitterUserId;
		}

		public void setSubmitterUserId(String submitterUserId) {
			this.submitterUserId = submitterUserId;
		}

		public String getUserType() {
			return userType;
		}

		public void setUserType(String userType) {
			this.userType = userType;
		}

		public boolean isApproved() {
			return isApproved;
		}

		public void setApproved(boolean isApproved) {
			this.isApproved = isApproved;
		}

		/*public FeedBack(String comments, int rating, String submitterUserId, String userType) {
			super();
			this.comments = comments;
			this.rating = rating;
			this.submitterUserId = submitterUserId;
			this.userType = userType;
		}*/

	}
	
}
