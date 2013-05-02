package com.kitnhiks.houseduties.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Tasks {

	private static Tasks INSTANCE = new Tasks();
	private static long idCategory = 0;
	private static long idTask = 0;


	private final ArrayList<TaskItemCategory> categories = new ArrayList<TaskItemCategory>();
	private final HashMap<Long, ArrayList<TaskItem>> tasksByCategory = new HashMap<Long, ArrayList<TaskItem>>();

	private Tasks() {
		TaskItemCategory cleanupCategory = new TaskItemCategory(idCategory++, "cleanup");
		categories.add(cleanupCategory);
		ArrayList<TaskItem> cleanUpTasks = new ArrayList<TaskItem>();
		cleanUpTasks.add(new TaskItem(idTask++, cleanupCategory.getKey(), "Faire la vaiselle jusqu'à remplir l'égoutoir", 1));
		cleanUpTasks.add(new TaskItem(idTask++, cleanupCategory.getKey(), "Passer l'aspirateur dans une pièce", 1));
		cleanUpTasks.add(new TaskItem(idTask++, cleanupCategory.getKey(), "Ranger une pièce", 1));
		cleanUpTasks.add(new TaskItem(idTask++, cleanupCategory.getKey(), "Laver le sol d'une pièce", 1));
		tasksByCategory.put(cleanupCategory.getKey(), cleanUpTasks);
	}

	public static Tasks getInstance()
	{
		return INSTANCE;
	}

	public static ArrayList<TaskItem> getTasks(long categoryKey) {
		return INSTANCE.tasksByCategory.get(categoryKey);
	}

	public static ArrayList<TaskItemCategory> getCategories() {
		return INSTANCE.categories;
	}

	public class TaskItemCategory implements Serializable{
		private static final long serialVersionUID = 1L;
		private long key;
		private String name;

		public TaskItemCategory(long key, String name){
			this.key = key;
			this.name = name;
		}

		public long getKey() {
			return key;
		}
		public void setKey(long key) {
			this.key = key;
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
		private long key;
		private long categoryKey;
		private String name;
		private int points;

		public TaskItem(long key, long categoryKey, String name, int points){
			this.key = key;
			this.categoryKey = categoryKey;
			this.name = name;
			this.points = points;
		}

		public long getKey() {
			return key;
		}

		public void setKey(long key) {
			this.key = key;
		}

		public long getCategoryKey() {
			return categoryKey;
		}

		public void setCategoryKey(long categoryKey) {
			this.categoryKey = categoryKey;
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
