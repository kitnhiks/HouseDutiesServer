package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getHouseFromToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.isAdminToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.renewToken;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Tasks;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/tasks")
public class TasksRESTService extends RESTService{

	public TasksRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@GET
	@Produces("application/json")
	public Response fetchTasks(//
			@Context HttpHeaders headers) {
		try{
			String token = getToken(headers);
			if (token != null){
				House house = getHouseFromToken(token);

				if (house != null || isAdminToken(token)){
					// TODO pour l'instant je ne retourne que les tache de cleanup
					return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity(Tasks.getTasks(0)).build();
				}
			}
			return Response.status(403).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving the tasks", e);
		}
	}
}