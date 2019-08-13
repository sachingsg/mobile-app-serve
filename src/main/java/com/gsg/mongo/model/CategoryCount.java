package com.gsg.mongo.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryCount {
	private String category;
	private int count=1;

	public CategoryCount(String category) {
		super();
		this.category = category;
	}
	

	public CategoryCount(String category, int count) {
		super();
		this.category = category;
		this.count = count;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
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
		CategoryCount other = (CategoryCount) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

	
}
