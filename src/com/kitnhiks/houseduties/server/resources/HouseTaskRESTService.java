package com.kitnhiks.houseduties.server.resources;


import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.generateToken;

import java.util.logging.Logger;

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

	public HouseTaskRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addTask(Task task, @PathParam("houseId") Long houseId){
		try{
			task.setKey(null);
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);

			house.addTask(task);
			pm.makePersistent(house);

			String token = generateToken(house.getName(), house.getPassword());
			return Response.status(200).header(AUTH_KEY_HEADER, token).entity("{\"id\":\""+task.getKey().getId()+"\"}").build();

		}catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("adding task to the house "+houseId, e);
		}
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getTask(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			Task task;
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key taskKey = KeyFactory.createKey(houseKey, Task.class.getSimpleName(), id);

			task = pm.detachCopy(pm.getObjectById(Task.class, taskKey));

			return Response.status(200).entity(task).build();

		}catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving task "+id+" from house "+houseId, e);
		}
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	public Response updateTask(Task task, @PathParam("houseId") Long houseId, @PathParam("id") Long id){
		Transaction tx = null;
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			tx = pm.currentTransaction();
			tx.begin();
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key taskKey = KeyFactory.createKey(houseKey, Task.class.getSimpleName(), id);

			Task taskToUpdate = pm.detachCopy(pm.getObjectById(Task.class, taskKey));
			taskToUpdate.update(task);
			pm.makePersistent(taskToUpdate);
			tx.commit();

			return Response.ok().build();

		}catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("updating the task "+id, e);
		}finally {
			if (tx!=null && tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteTask(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			Task taskToDelete;
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key taskKey = KeyFactory.createKey(houseKey, Task.class.getSimpleName(), id);
			taskToDelete = pm.getObjectById(Task.class, taskKey);

			pm.deletePersistent(taskToDelete);

			return Response.ok().build();

		}catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("updating the task "+id, e);
		}
	}
}