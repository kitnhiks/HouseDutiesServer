package com.kitnhiks.houseduties.server.model;

import java.io.Serializable;
import java.util.HashMap;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Tasks implements Serializable{

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private final HashMap<String, HashMap<Long, TaskItem>> tasks = new HashMap<String, HashMap<Long, TaskItem>>();

	public Tasks() {
		HashMap<Long, TaskItem> cleanup = new HashMap<Long, TaskItem>();
		cleanup.put(new Long(1), new TaskItem("Faire la vaiselle jusqu'à remplir l'égoutoir", 1));
		cleanup.put(new Long(2), new TaskItem("Passer l'aspirateur dans une pièce", 1));
		cleanup.put(new Long(3), new TaskItem("Ranger une pièce", 1));
		cleanup.put(new Long(4), new TaskItem("Laver le sol d'une pièce", 1));
		tasks.put("cleanup", cleanup);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HashMap<String, HashMap<Long, TaskItem>> getTasks() {
		return tasks;
	}

	public class TaskItem implements Serializable{
		private static final long serialVersionUID = 1L;
		private String name;
		private int points;

		public TaskItem(String name, int points){
			this.name = name;
			this.points = points;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getPoints() {
			return points;
		}

		public void setPoints(int points) {
			this.points = points;
		}
	}
}
