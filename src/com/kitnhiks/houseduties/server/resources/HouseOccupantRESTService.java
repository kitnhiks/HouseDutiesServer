package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.generateToken;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Occupant;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/occupant")
public class HouseOccupantRESTService extends RESTService{
	// TODO : handle errors logging

	@POST
	@Consumes("application/json")
	public Response addOccupant(Occupant occupant, @PathParam("houseId") Long houseId){
		occupant.setKey(null);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		String token="";
		try {
			tx.begin();
			House house = pm.getObjectById(House.class, houseId);
			house.addOccupant(occupant);
			pm.makePersistent(house);
			token = generateToken(house.getName(), house.getPassword());
			tx.commit();
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		return Response.status(200).header(AUTH_KEY_HEADER, token).entity("{\"id\":\""+occupant.getKey().getId()+"\"}").build();
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getOccupant(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Occupant occupant;
		try{
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), id);
			occupant = pm.detachCopy(pm.getObjectById(Occupant.class, occupantKey));

		}catch(Exception e){
			return Response.status(404).build();
		}
		return Response.status(200).entity(occupant).build();
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	public Response updateOccupant(Occupant occupant, @PathParam("houseId") Long houseId, @PathParam("id") Long id){
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), id);
			Occupant occupantToUpdate = pm.detachCopy(pm.getObjectById(Occupant.class, occupantKey));
			occupantToUpdate.update(occupant);
			pm.makePersistent(occupantToUpdate);
			tx.commit();
		}catch(Exception e){
			return serverErrorResponse("updating the occupant "+id, e);
		}finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteOccupant(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		Occupant occupantToDelete;
		try{
			Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
			Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), id);
			occupantToDelete = pm.getObjectById(Occupant.class, occupantKey);
		}catch(Exception e){
			return Response.status(404).build();
		}
		pm.deletePersistent(occupantToDelete);
		return Response.ok().build();
	}
}
