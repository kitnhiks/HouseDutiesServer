package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.isAdmin;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.kitnhiks.houseduties.server.model.House;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/houses")
public class HousesRESTService extends RESTService{

	public HousesRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@GET
	@Produces("application/json")
	public Response fetchHouses(//
			@Context HttpHeaders headers){
		try{
			if (isAdmin(headers)){
				PersistenceManager pm = RESTService.pmfInstance.getPersistenceManager();

				Query query = pm.newQuery("select id from " + House.class.getName());

				return Response.status(200).entity(query.execute()).build();
			}else{
				return Response.status(403).build();
			}
		}catch(Exception e){
			return serverErrorResponse("retrieving houses", e);
		}
	}
}
