package com.kitnhiks.houseduties.server.model;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Occupant implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String name;
	@Persistent (defaultFetchGroup = "false")
	@JsonIgnore
	private String password;
	@Persistent
	private String email;
	@Persistent
	private int points = 0;
	@Element(dependent = "true")
	private List<Task> tasks;

	public Occupant() {

	}


	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
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

	public void update(Occupant occupant) {
		setName(occupant.getName());
		setEmail(occupant.getEmail());
		setPoints(occupant.getPoints());
	}

}