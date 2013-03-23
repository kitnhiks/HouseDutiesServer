package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/tasks")
public class TasksRESTService extends RESTService{

	@GET
	@Produces("application/json")
	/**
	 * @return the list of tasks
	 */
	public Response fetchTasks(@Context HttpHeaders headers) {
		String authKey = "";
		try{
			try{
				authKey = headers.getRequestHeader(AUTH_KEY_HEADER).get(0);
			}catch(Exception e){
				return Response.status(401).build();
			}
			// TODO : Handle auth
			PersistenceManager pm = RESTService.pmfInstance.getPersistenceManager();
			Query query = pm.newQuery("Task");

			return Response.status(200).entity(query.execute()).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving the tasks", e);
		}
	}
}
