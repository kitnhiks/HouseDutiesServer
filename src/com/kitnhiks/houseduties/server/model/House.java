package com.kitnhiks.houseduties.server.model;

import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


@PersistenceCapable
public class House
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String name;
	@Persistent (defaultFetchGroup = "false")
	@JsonIgnore
	private String password;
	@Element(dependent = "true")
	private List<Occupant> occupants;
	@Element(dependent = "true")
	private List<Task> tasks;

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
	@JsonIgnore
	public String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	@JsonProperty
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
	public void addOccupant(Occupant newOccupant){
		occupants.add(newOccupant);
	}

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}


	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Add a new occupant to the house
	 * @param occupant the occupant to add
	 */
	public void addTask(Task newTask){
		tasks.add(newTask);
	}
}