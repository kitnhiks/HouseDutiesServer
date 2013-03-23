package com.kitnhiks.houseduties.server.resources;


import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.kitnhiks.houseduties.server.model.Task;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/task")
public class TaskRESTService extends RESTService{

	public TaskRESTService() {
		super();
		logger = Logger.getLogger("TaskRESTService");
	}

	@Path("{id}")
	@GET
	@Produces("application/json")
	/**
	 * @return the task
	 */
	public Response fetchTask(@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			Task task;
			try{
				task = pm.detachCopy(pm.getObjectById(Task.class, id));

			}catch(Exception e){
				return Response.status(404).build();
			}
			return Response.status(200).entity(task).build();
		}catch (Exception e){
			return serverErrorResponse("retrieving the task", e);
		}
	}
}
