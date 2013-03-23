package com.kitnhiks.houseduties.server.resources;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Occupant;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/occupants")
public class HouseOccupantsRestService extends RESTService{
	// TODO : error logging
	@GET
	@Produces("application/json")
	/**
	 * Retrieve all the occupants of a given house
	 * @param id the id of the house
	 * @return the occupants
	 */
	public Response getOccupants(@PathParam("houseId") Long houseId) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		House house = pm.getObjectById(House.class, houseId);
		ArrayList<Occupant> occupants = new ArrayList<Occupant>();
		for(Occupant occupant : house.getOccupants()){
			occupants.add(pm.detachCopy(pm.getObjectById(Occupant.class, occupant.getKey())));
		}

		return Response.status(200).entity(occupants).build();
	}
}
