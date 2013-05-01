package com.kitnhiks.houseduties.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Tasks {

	private static Tasks INSTANCE = new Tasks();
	private static int idCategory = 0;
	private static int idTask = 0;


	private final ArrayList<TaskItemCategory> categories = new ArrayList<TaskItemCategory>();
	private final HashMap<Integer, ArrayList<TaskItem>> tasksByCategory = new HashMap<Integer, ArrayList<TaskItem>>();

	private Tasks() {
		TaskItemCategory cleanupCategory = new TaskItemCategory(++idCategory, "cleanup");
		categories.add(cleanupCategory);
		ArrayList<TaskItem> cleanUpTasks = new ArrayList<TaskItem>();
		cleanUpTasks.add(new TaskItem(++idTask, cleanupCategory.getId(), "Faire la vaiselle jusqu'à remplir l'égoutoir", 1));
		cleanUpTasks.add(new TaskItem(++idTask, cleanupCategory.getId(), "Passer l'aspirateur dans une pièce", 1));
		cleanUpTasks.add(new TaskItem(++idTask, cleanupCategory.getId(), "Ranger une pièce", 1));
		cleanUpTasks.add(new TaskItem(++idTask, cleanupCategory.getId(), "Laver le sol d'une pièce", 1));
		tasksByCategory.put(cleanupCategory.getId(), cleanUpTasks);
	}

	public static Tasks getInstance()
	{
		return INSTANCE;
	}

	public static ArrayList<TaskItem> getTasks(int categoryId) {
		return INSTANCE.tasksByCategory.get(categoryId);
	}

	public static ArrayList<TaskItemCategory> getCategories() {
		return INSTANCE.categories;
	}

	public class TaskItemCategory implements Serializable{
		private static final long serialVersionUID = 1L;
		private int id;
		private String name;

		public TaskItemCategory(int id, String name){
			this.name = name;
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	public class TaskItem implements Serializable{
		private static final long serialVersionUID = 1L;
		private int id;
		private int categoryId;
		private String name;
		private int points;

		public TaskItem(int id, int categoryId, String name, int points){
			this.id = id;
			this.setCategoryId(categoryId);
			this.name = name;
			this.points = points;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getCategoryId() {
			return categoryId;
		}

		public void setCategoryId(int categoryId) {
			this.categoryId = categoryId;
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
