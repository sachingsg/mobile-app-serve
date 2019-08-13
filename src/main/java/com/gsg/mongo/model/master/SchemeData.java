package com.gsg.mongo.model.master;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gsg.mongo.model.Auditable;
import com.gsg.utilities.GSGCommon;

@Document(collection = "scheme")
@TypeAlias("Scheme")
@Getter @Setter
public class SchemeData extends Auditable {

	@Id
	@JsonIgnore
	private String id;
	private String schemeId;

	private String schemeType;
	@Field("desc")
	private String description;
	private double price;
	private double gst;
	
	private double effectivePrice;
	private double effectivePriceWithoutTax;
	
	private int durationInDays; // in Days
	@Field("discConsumable")
	private int discountOnConsumable;
	@Field("discLabour")
	private int discountOnLabour;
	@Field("discSpare")
	private double discountOnSparePart;

	@Field("services")
	private List<SchemeService> schemeServiceDtls = new ArrayList<>();

	private boolean active;
	private boolean userSchemeExpired;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate subscriptionDt;
	private ReferralDetails refrralDtl;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public double getEffectivePrice() {
		return effectivePrice;
	}

	public void setEffectivePrice(double effectivePrice) {
		this.effectivePrice = effectivePrice;
	}

	public double getEffectivePriceWithoutTax() {
		return effectivePriceWithoutTax;
	}

	public void setEffectivePriceWithoutTax(double effectivePriceWithoutTax) {
		this.effectivePriceWithoutTax = effectivePriceWithoutTax;
	}

	public int getDurationInDays() {
		return durationInDays;
	}

	public void setDurationInDays(int durationInDays) {
		this.durationInDays = durationInDays;
	}

	public int getDiscountOnConsumable() {
		return discountOnConsumable;
	}

	public void setDiscountOnConsumable(int discountOnConsumable) {
		this.discountOnConsumable = discountOnConsumable;
	}

	public int getDiscountOnLabour() {
		return discountOnLabour;
	}

	public void setDiscountOnLabour(int discountOnLabour) {
		this.discountOnLabour = discountOnLabour;
	}

	public double getDiscountOnSparePart() {
		return discountOnSparePart;
	}

	public void setDiscountOnSparePart(double discountOnSparePart) {
		this.discountOnSparePart = discountOnSparePart;
	}

	public List<SchemeService> getSchemeServiceDtls() {
		return schemeServiceDtls;
	}

	public void setSchemeServiceDtls(List<SchemeService> schemeServiceDtls) {
		this.schemeServiceDtls = schemeServiceDtls;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isUserSchemeExpired() {
		return userSchemeExpired;
	}

	public void setUserSchemeExpired(boolean userSchemeExpired) {
		this.userSchemeExpired = userSchemeExpired;
	}

	public LocalDate getSubscriptionDt() {
		return subscriptionDt;
	}

	public void setSubscriptionDt(LocalDate subscriptionDt) {
		this.subscriptionDt = subscriptionDt;
	}

	public ReferralDetails getRefrralDtl() {
		return refrralDtl;
	}

	public void setRefrralDtl(ReferralDetails refrralDtl) {
		this.refrralDtl = refrralDtl;
	}

	@Getter @Setter
	public static class ReferralDetails{
		private String referralCd;
		private String referType;
		private String mobileNbr;
		public String getReferralCd() {
			return referralCd;
		}
		public void setReferralCd(String referralCd) {
			this.referralCd = referralCd;
		}
		public String getReferType() {
			return referType;
		}
		public void setReferType(String referType) {
			this.referType = referType;
		}
		public String getMobileNbr() {
			return mobileNbr;
		}
		public void setMobileNbr(String mobileNbr) {
			this.mobileNbr = mobileNbr;
		}
		
		
	}

	
	//////////////////////////////
	public SchemeData() {
		super();
	}

	public SchemeData(String description, double price, int durationInDays, boolean active,
			List<SchemeService> schemeServiceDtls) {
		this.description = description;
		this.price = price;
		this.durationInDays = durationInDays;
		this.active = active;
		this.schemeServiceDtls = schemeServiceDtls;
	}

	public void calculatePrice() {
		this.effectivePriceWithoutTax = this.price;
		this.effectivePrice = //GSGCommon.getTwoPrecisionDoubleValue(this.effectivePriceWithoutTax * (1 + (this.gst / 100)));
				GSGCommon.getTaxPrice(effectivePriceWithoutTax, gst);

	}
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchemeData other = (SchemeData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Getter @Setter
	public static class SchemeService {
		private String serviceId;
		private String serviceDesc;
		private int freeUsage;
		private int used = 0;

		public SchemeService() {
		}

		public SchemeService(String serviceRefId, String serviceDesc, int freeUsage, int used) {
			this.serviceId = serviceRefId;
			this.serviceDesc = serviceDesc;
			this.freeUsage = freeUsage;
			this.used = used;
		}

		public SchemeService(Services svc, int freeUsage) {
			this.serviceId = svc.getServiceId();
			this.serviceDesc = svc.getSubCategory();
			this.freeUsage = freeUsage;
		}

		public SchemeService(String serviceRefId) {
			this.serviceId = serviceRefId;
		}

		// //////////////


		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

		public String getServiceDesc() {
			return serviceDesc;
		}

		public void setServiceDesc(String serviceDesc) {
			this.serviceDesc = serviceDesc;
		}

		public int getFreeUsage() {
			return freeUsage;
		}

		public void setFreeUsage(int freeUsage) {
			this.freeUsage = freeUsage;
		}

		public int getUsed() {
			return used;
		}

		public void setUsed(int used) {
			this.used = used;
		}

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SchemeService other = (SchemeService) obj;
			if (serviceId == null) {
				if (other.serviceId != null)
					return false;
			} else if (!serviceId.equals(other.serviceId))
				return false;
			return true;
		}

	}

}
