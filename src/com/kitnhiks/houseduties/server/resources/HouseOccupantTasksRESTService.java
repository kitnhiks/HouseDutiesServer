package com.kitnhiks.houseduties.server.resources;

import java.util.ArrayList;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
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
@Path("/house/{houseId}/occupant/{occupantId}/tasks")
public class HouseOccupantTasksRESTService extends RESTService{

	public HouseOccupantTasksRESTService(){
		super();
	}

	@GET
	@Produces("application/json")
	public Response getTasks(@PathParam("houseId") Long houseId, @PathParam("occupantId") Long occupantId) {

		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			Occupant occupant;
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), occupantId);
			occupant = pm.getObjectById(Occupant.class, occupantKey);

			ArrayList<Task> tasks = new ArrayList<Task>();
			for(Task task : occupant.getTasks()){
				tasks.add(pm.detachCopy(pm.getObjectById(Task.class, task.getKey())));
			}

			return Response.status(200).entity(tasks).build();

		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving the house "+houseId+" tasks", e);
		}
	}
}