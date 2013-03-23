package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.generateToken;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
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
import com.kitnhiks.houseduties.server.model.Occupant;
import com.kitnhiks.houseduties.server.model.Task;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/occupant/{occupantId}/task")
public class HouseOccupantTaskRESTService extends RESTService{

	@POST
	@Consumes("application/json")
	// TODO : remove all comments
	public Response addTask(Task task, @PathParam("houseId") Long houseId, @PathParam("occupantId") Long occupantId){
		// TODO : add try catch everywhere
		try {
			task.setKey(null);
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			String token="";
			try {
				// TODO : review transaction and remove it ?
				Occupant occupant;
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
				occupant = pm.getObjectById(Occupant.class, occupantKey);
				House house = pm.getObjectById(House.class, houseId);
				occupant.addTask(task);
				pm.makePersistent(occupant);
				token = generateToken(house.getName(), house.getPassword());
			} catch (JDOObjectNotFoundException e){
				return Response.status(404).build();
			}
			return Response.status(200).header(AUTH_KEY_HEADER, token).entity("{\"id\":\""+task.getKey().getId()+"\"}").build();
		}catch(Exception e){
			return serverErrorResponse("adding task to occupant "+occupantId+" (@ "+houseId+")", e);
		}
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getTask(@PathParam("houseId") Long houseId, @PathParam("occupantId") Long occupantId, @PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			Task task;
			try{
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
				Key taskKey = KeyFactory.createKey(occupantKey, Task.class.getSimpleName(), id);

				task = pm.detachCopy(pm.getObjectById(Task.class, taskKey));

			}catch(Exception e){
				return Response.status(404).build();
			}
			return Response.status(200).entity(task).build();
		}catch(Exception e){
			return serverErrorResponse("getting task from occupant "+occupantId+" (@ "+houseId+")", e);
		}
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	public Response updateTask(Task task, @PathParam("houseId") Long houseId, @PathParam("occupantId") Long occupantId, @PathParam("id") Long id){
		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
			Key taskKey = KeyFactory.createKey(occupantKey, Task.class.getSimpleName(), id);

			Task taskToUpdate = pm.detachCopy(pm.getObjectById(Task.class, taskKey));
			taskToUpdate.update(task);
			pm.makePersistent(taskToUpdate);

			return Response.ok().build();
		}catch(Exception e){
			return serverErrorResponse("updating task for occupant "+occupantId+" (@ "+houseId+")", e);
		}
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteTask(@PathParam("houseId") Long houseId, @PathParam("occupantId") Long occupantId, @PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			Task taskToDelete;
			try{
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
				Key taskKey = KeyFactory.createKey(occupantKey, Task.class.getSimpleName(), id);
				taskToDelete = pm.getObjectById(Task.class, taskKey);
			}catch(Exception e){
				return Response.status(404).build();
			}
			pm.deletePersistent(taskToDelete);
			return Response.ok().build();
		}catch(Exception e){
			return serverErrorResponse("deleting task from occupant "+occupantId+" (@ "+houseId+")", e);
		}
	}
}