package com.gsg.mongo.model.master;

import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "states")
@TypeAlias("States")
@Getter @Setter
public class States{
	@Id
	private String stateCd;
	private String stateName;
	private int stateRefNbr;
	private Set<String> districts = new TreeSet<String>();

	public States(String stateCd) {
		this.stateCd = stateCd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stateCd == null) ? 0 : stateCd.hashCode());
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
		States other = (States) obj;
		if (stateCd == null) {
			if (other.stateCd != null)
				return false;
		} else if (!stateCd.equals(other.stateCd))
			return false;
		return true;
	}

	public String getStateCd() {
		return stateCd;
	}

	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getStateRefNbr() {
		return stateRefNbr;
	}

	public void setStateRefNbr(int stateRefNbr) {
		this.stateRefNbr = stateRefNbr;
	}

	public Set<String> getDistricts() {
		return districts;
	}

	public void setDistricts(Set<String> districts) {
		this.districts = districts;
	}

}
