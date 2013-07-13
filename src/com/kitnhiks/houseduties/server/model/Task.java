package com.kitnhiks.houseduties.server.model;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Task implements Serializable {

	private static final long serialVersionUID = 3L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String name;
	@Persistent
	private int points = 0;
	@Persistent
	private long categoryKey;
	@Persistent
	private int priority;
	@Persistent
	private long deadline;
	@Persistent
	private long doneDate;

	public Task() {

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
	 * @return the categoryKey
	 */
	public long getCategoryKey() {
		return categoryKey;
	}


	/**
	 * @param categoryKey the categoryKey to set
	 */
	public void setCategoryKey(long categoryKey) {
		this.categoryKey = categoryKey;
	}


	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}


	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}


	/**
	 * @return the deadline
	 */
	public long getDeadline() {
		return deadline;
	}


	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}


	/**
	 * @return the doneDate
	 */
	public long getDoneDate() {
		return doneDate;
	}


	/**
	 * @param doneDate the doneDate to set
	 */
	public void setDoneDate(long doneDate) {
		this.doneDate = doneDate;
	}

	/**
	 * Update from task
	 * @param Task
	 */
	public void update(Task task) {
		setName(task.getName());
		setPoints(task.getPoints());
		setCategoryKey(task.getCategoryKey());
		setPriority(task.getPriority());
		setDeadline(task.getDeadline());
		setDoneDate(task.getDoneDate());
	}
}