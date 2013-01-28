package com.kitnhiks.houseduties.server.resources.house.occupants;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Occupant;
import static com.kitnhiks.houseduties.server.resources.RESTService.pmfInstance;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/occupants")
public class OccupantsRestService {

//	@GET
//	@Produces("application/json")
//	/**
//	 * Retrieve all the occupants of a given house
//	 * @param id the id of the house
//	 * @return the occupants
//	 */
//	public List<Occupant> getOccupants(@PathParam("houseId") Long houseId) {
//		PersistenceManager pm = pmfInstance.getPersistenceManager();
//		House house = pm.getObjectById(House.class, houseId);
//		return house.getOccupants();
//	}
}
