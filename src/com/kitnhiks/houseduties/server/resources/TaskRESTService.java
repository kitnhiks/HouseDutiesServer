package com.kitnhiks.houseduties.server.resources;


import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getHouseFromToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.isAdminToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.renewToken;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Task;
import com.kitnhiks.houseduties.server.model.Tasks;
import com.kitnhiks.houseduties.server.model.Tasks.TaskItem;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/task")
public class TaskRESTService extends RESTService{

	public TaskRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@Path("{category}/{id}")
	@GET
	@Produces("application/json")
	/**
	 * @return the task
	 */
	public Response fetchTask(//
			@PathParam("category") String category, //
			@PathParam("id") Long id, //
			@Context HttpHeaders headers) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			Task task;
			String token = getToken(headers);
			if (token != null){
				House house = getHouseFromToken(token);

				if (house != null || isAdminToken(token)){
					Tasks tasksList = new Tasks();
					TaskItem taskItem = tasksList.getTasks().get(category).get(id);
					return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity(taskItem).build();
				}
			}
			return Response.status(403).build();

		}catch (Exception e){
			return serverErrorResponse("retrieving the task", e);
		}
	}
}
