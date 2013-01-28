package com.kitnhiks.houseduties.server.model;

import java.util.Iterator;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class House
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    private String name;
    @Persistent (defaultFetchGroup = "false")
	private String password;
    @Element(dependent = "true")
    private List<Occupant> occupants;

    public House() {
    	
    }
    
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
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

	/**
	 * Add a new occupant to the house
	 * @param occupant the occupant to add
	 */
	public void addItem(Occupant newOccupant){
		occupants.add(newOccupant);
	}
	
	/**
	 * Get an occupant of the house
	 * @param id
	 * @return the occupant
	 */
	public Occupant getOccupant(Long id){
		Iterator<Occupant> it = occupants.iterator();
		Occupant occupant;
		while(it.hasNext()){
			occupant = it.next();
			if (occupant.getKey().getId() == id){
				return occupant; 
			}
		}
		return null;
	}

	/**
	 * Update the house with another house values
	 * @param house
	 */
	public void update(House house) {
		setName(house.getName());
	}
}