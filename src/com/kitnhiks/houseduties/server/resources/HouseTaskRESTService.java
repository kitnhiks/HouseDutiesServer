package com.kitnhiks.houseduties.server.resources;


import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.generateToken;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Task;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/task")
public class HouseTaskRESTService extends RESTService{

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	/**
	 * add a task to the house
	 * @param task to be added
	 * @return task id and token
	 */
	public Response addTask(Task task, @PathParam("houseId") Long houseId){
		task.setKey(null);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		String token="";
		try {
			tx.begin();
			House house = pm.getObjectById(House.class, houseId);
			house.addTask(task);
			pm.makePersistent(house);
			token = generateToken(house.getName(), house.getPassword());
			tx.commit();
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		return Response.status(200).header(AUTH_KEY_HEADER, token).entity("{\"id\":\""+task.getKey().getId()+"\"}").build();
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getTask(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Task task;
		try{
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key taskKey = KeyFactory.createKey(houseKey, Task.class.getSimpleName(), id);
			task = pm.detachCopy(pm.getObjectById(Task.class, taskKey));

		}catch(Exception e){
			return Response.status(404).build();
		}
		return Response.status(200).entity(task).build();
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	public Response updateTask(Task task, @PathParam("houseId") Long houseId, @PathParam("id") Long id){
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key taskKey = KeyFactory.createKey(houseKey, Task.class.getSimpleName(), id);
			Task taskToUpdate = pm.detachCopy(pm.getObjectById(Task.class, taskKey));
			taskToUpdate.update(task);
			pm.makePersistent(taskToUpdate);
			tx.commit();
		}catch(Exception e){
			return serverErrorResponse("updating the task "+id, e);
		}finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteTask(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Task taskToDelete;
		try{
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key taskKey = KeyFactory.createKey(houseKey, Task.class.getSimpleName(), id);
			taskToDelete = pm.getObjectById(Task.class, taskKey);
		}catch(Exception e){
			return Response.status(404).build();
		}
		pm.deletePersistent(taskToDelete);
		return Response.ok().build();
	}

}
