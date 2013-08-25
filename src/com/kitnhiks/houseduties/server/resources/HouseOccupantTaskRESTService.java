package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.isValidToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.renewToken;

import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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

	public HouseOccupantTaskRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@POST
	@Consumes("application/json")
	public Response addTask(//
			@Context HttpHeaders headers, //
			Task task, //
			@PathParam("houseId") Long houseId, //
			@PathParam("occupantId") Long occupantId)
	{
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		try {
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				task.setKey(null);

				Occupant occupant;

				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
				occupant = pm.getObjectById(Occupant.class, occupantKey);
				logger.log(Level.SEVERE, "caca : "+occupant.getName());
				logger.log(Level.SEVERE, "caca 2: "+occupant.getPoints());
				occupant.addTask(task);
				occupant.setPoints(occupant.getPoints() + task.getPoints());
				logger.log(Level.SEVERE, "caca 3: "+occupant.getPoints());
				pm.makePersistent(occupant);
				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity("{\"id\":\""+task.getKey().getId()+"\"}").build();
			}else{
				return Response.status(403).build();
			}
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("adding task to occupant "+occupantId+" (@ "+houseId+")", e);
		} finally {
			pm.close();
		}

	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getTask(//
			@Context HttpHeaders headers, //
			@PathParam("houseId") Long houseId, //
			@PathParam("occupantId") Long occupantId, //
			@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				Task task;
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
				Key taskKey = KeyFactory.createKey(occupantKey, Task.class.getSimpleName(), id);

				task = pm.detachCopy(pm.getObjectById(Task.class, taskKey));

				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity(task).build();
			}else{
				return Response.status(403).build();
			}
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("getting task from occupant "+occupantId+" (@ "+houseId+")", e);
		}
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	public Response updateTask(//
			@Context HttpHeaders headers, //
			Task task, //
			@PathParam("houseId") Long houseId, //
			@PathParam("occupantId") Long occupantId, //
			@PathParam("id") Long id){
		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
				Key taskKey = KeyFactory.createKey(occupantKey, Task.class.getSimpleName(), id);

				Task taskToUpdate = pm.detachCopy(pm.getObjectById(Task.class, taskKey));
				taskToUpdate.update(task);
				pm.makePersistent(taskToUpdate);

				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).build();
			}else{
				return Response.status(403).build();
			}
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("updating task for occupant "+occupantId+" (@ "+houseId+")", e);
		}
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteTask(//
			@Context HttpHeaders headers, //
			@PathParam("houseId") Long houseId, //
			@PathParam("occupantId") Long occupantId, //
			@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				Task taskToDelete;
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
				Key taskKey = KeyFactory.createKey(occupantKey, Task.class.getSimpleName(), id);

				taskToDelete = pm.getObjectById(Task.class, taskKey);
				pm.deletePersistent(taskToDelete);

				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).build();
			}else{
				return Response.status(403).build();
			}

		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("deleting task from occupant "+occupantId+" (@ "+houseId+")", e);
		}
	}
}
