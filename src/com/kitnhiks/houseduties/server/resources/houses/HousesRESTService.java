package com.kitnhiks.houseduties.server.resources.houses;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_ADMIN;
import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.resources.RESTService;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/houses")
public class HousesRESTService extends RESTService{
	// TODO : error logging

	@GET
	@Produces("application/json")
	/**
	 * @return the list of houses
	 */
	public Response fetchHouses(@Context HttpHeaders headers) {
		String authKey = "";
		try{
			authKey = headers.getRequestHeader(AUTH_KEY_HEADER).get(0);
		}catch(Exception e){

		}
		if (AUTH_KEY_ADMIN.equals(authKey)){
			PersistenceManager pm = RESTService.pmfInstance.getPersistenceManager();
			Query query = pm.newQuery("select id from " + House.class.getName());

			return Response.status(200).entity(query.execute()).build();
		}
		return Response.status(401).build();
	}

}
