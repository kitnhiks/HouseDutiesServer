package com.kitnhiks.houseduties.client.model;

import java.util.List;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class House {

	private long id;
	private String name;
	private String password;
	private List<Occupant> occupants;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the occupants
	 */
	public List<Occupant> getOccupants() {
		return occupants;
	}
	/**
	 * @param occupants the occupants to set
	 */
	public void setOccupants(List<Occupant> occupants) {
		this.occupants = occupants;
	}
	// JSON
	/**
	 * @return json representation
	 */
	public JSONObject toJson() {
		JSONObject houseAsJSONObject = new JSONObject();
		houseAsJSONObject.put("name", new JSONString(this.name));
		houseAsJSONObject.put("password", new JSONString(this.password));
		return houseAsJSONObject;
	}

}