package com.kitnhiks.houseduties.server.resources;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Task;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/tasks")
public class HouseTasksRESTService extends RESTService{

	public HouseTasksRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@GET
	@Produces("application/json")
	/**
	 * Retrieve all the tasks of a given house
	 * @param id the id of the house
	 * @return the tasks
	 */
	public Response getTasks(@PathParam("houseId") Long houseId) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			ArrayList<Task> tasks = new ArrayList<Task>();
			for(Task task : house.getTasks()){
				tasks.add(pm.detachCopy(pm.getObjectById(Task.class, task.getKey())));
			}

			return Response.status(200).entity(tasks).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving the house "+houseId+" tasks", e);
		}
	}
}
