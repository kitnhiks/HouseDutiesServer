package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.isValidToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.renewToken;

import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Occupant;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/occupant")
public class HouseOccupantRESTService extends RESTService{

	public HouseOccupantRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@POST
	@Consumes("application/json")
	public Response addOccupant(//
			@Context HttpHeaders headers, //
			Occupant occupant, //
			@PathParam("houseId") Long houseId){
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				occupant.setKey(null);

				house.addOccupant(occupant);
				pm.makePersistent(house);

				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity("{\"id\":\""+occupant.getKey().getId()+"\"}").build();
			}else{
				return Response.status(403).build();
			}
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("adding occupant to house "+houseId, e);
		}
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getOccupant(
			@Context HttpHeaders headers, //
			@PathParam("houseId") Long houseId, //
			@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				Occupant occupant;
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), id);

				occupant = pm.detachCopy(pm.getObjectById(Occupant.class, occupantKey));

				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity(occupant).build();
			}else{
				return Response.status(403).build();
			}
		}catch(JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving occupant "+id+" of house "+houseId, e);
		}
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteOccupant(
			@Context HttpHeaders headers, //
			@PathParam("houseId") Long houseId, //
			@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				Occupant occupantToDelete;
				Key houseKey = KeyFactory.createKey(House.class.getSimpleName(), houseId);
				Key occupantKey = KeyFactory.createKey(houseKey, Occupant.class.getSimpleName(), id);
				occupantToDelete = pm.getObjectById(Occupant.class, occupantKey);

				pm.deletePersistent(occupantToDelete);

				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).build();
			}else{
				return Response.status(403).build();
			}
		}catch(JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("deleting occupant "+id+" of house "+houseId, e);
		}
	}
}
