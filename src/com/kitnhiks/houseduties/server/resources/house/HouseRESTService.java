package com.kitnhiks.houseduties.server.resources.house;


import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.resources.RESTService.pmfInstance;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.generateToken;

import java.util.List;

import javax.jdo.FetchGroup;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.datanucleus.FetchPlan;

import com.kitnhiks.houseduties.server.model.House;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house")
public class HouseRESTService {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	/**
	 * Create a house
	 * @param house to be created
	 * @return house id and token
	 */
	public Response createAccount(House house){
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		pm.makePersistent(house);
		
		return Response.status(200).header(AUTH_KEY_HEADER, generateToken(house.getName(), house.getPassword())).entity("{\"id\":\""+house.getId()+"\"}").build();
	}
	
	@Path("login")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	/**
	 * Check a Login
	 * @param login to be checked
	 * @return house id and token
	 */
	public Response loginAccount(House house){

		PersistenceManager pm = pmfInstance.getPersistenceManager();
			Query query = pm.newQuery(House.class, "name == "+house.getName()+" && password == "+house.getPassword());
			@SuppressWarnings("unchecked")
			List<House> houseResults = (List<House>) query.execute();
			int nbResults = houseResults.size();
			if (nbResults == 1){
				house = houseResults.get(0);
				return Response.status(200).header(AUTH_KEY_HEADER, generateToken(house.getName(), house.getPassword())).entity("{\"id\":\""+house.getId()+"\"}").build();
			}else if (nbResults==0){
				return Response.status(404).build();
			}else{
				return Response.status(500).entity("Corrupted Data...").build();
			}
	}

	@Path("{id}")
	@GET
	@Produces("application/json")
	/**
	 * @return the house
	 */
	public Response fetchHouse(@PathParam("id") Long id) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		House house;
		try{
			house = pm.detachCopy(pm.getObjectById(House.class, id));
			
		}catch(Exception e){
			return Response.status(404).build();
		}
		return Response.status(200).entity(house).build();
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	/**
	 * Update a house
	 * @param house
	 */
	public Response updateHouse (House house, @PathParam("id") Long id){
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		tx.begin();
		House houseToUpdate = pm.getObjectById(House.class, id);
		houseToUpdate.update(house);
		pm.makePersistent(houseToUpdate);
		tx.commit();
		pm.close();
		return Response.ok().build();
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	/**
	 * Delete the house
	 * @param id
	 */
	public Response deleteHouse(@PathParam("id") Long id) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		House houseToDelete;
		try{
			houseToDelete = pm.getObjectById(House.class, id);
		}catch(Exception e){
			return Response.status(404).build();
		}
		pm.deletePersistent(houseToDelete);
		return Response.ok().build();
	}
}