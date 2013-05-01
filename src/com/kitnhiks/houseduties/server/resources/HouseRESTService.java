package com.kitnhiks.houseduties.server.resources;


import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.generateToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.isValidToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.renewToken;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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

import com.kitnhiks.houseduties.server.model.House;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house")
public class HouseRESTService extends RESTService{

	public HouseRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createAccount(House house){
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			if (house.getName().contains("#")){
				return Response.status(412).build();
			}

			// Check house doesn't already exists
			Query query = pm.newQuery(House.class, "name == \""+house.getName()+"\"");
			@SuppressWarnings("unchecked")
			List<House> houseResults = (List<House>) query.execute();
			int nbResults = houseResults.size();
			if (nbResults!=0){
				return Response.status(401).build();
			}

			// Create house
			pm.makePersistent(house);

			String returnEntity = "{\"id\":\""+house.getId()+"\"}";
			return Response.status(200).header(AUTH_KEY_HEADER, generateToken(house.getName(), house.getPassword())).entity(returnEntity).build();

		}catch (Exception e){
			return serverErrorResponse("creating the house", e);
		}
	}

	@Path("login")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response loginAccount(House house){

		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			Query query = pm.newQuery(House.class, "name == \""+house.getName()+"\" && password == \""+house.getPassword()+"\"");
			@SuppressWarnings("unchecked")
			List<House> houseResults = (List<House>) query.execute();
			int nbResults = houseResults.size();
			if (nbResults == 1){
				house = houseResults.get(0);
				return Response.status(200).header(AUTH_KEY_HEADER, generateToken(house.getName(), house.getPassword())).entity("{\"id\":\""+house.getId()+"\"}").build();
			}else if (nbResults==0){
				return Response.status(404).build();
			}else{
				throw new RuntimeException ("Corrupted data...");
			}
		}catch (Exception e){
			return serverErrorResponse("logging to the house", e);
		}
	}

	@Path("{id}")
	@GET
	@Produces("application/json")
	public Response fetchHouse(//
			@Context HttpHeaders headers, //
			@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, id);
			String token = getToken(headers);
			if (isValidToken(token, house)){
				house = pm.detachCopy(house);
				house.setPassword(null);
				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity(house).build();
			}else{
				return Response.status(403).build();
			}

		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch (Exception e){
			return serverErrorResponse("retrieving the house "+id, e);
		}
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteHouse(//
			@Context HttpHeaders headers, //
			@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House houseToDelete = pm.getObjectById(House.class, id);
			String token = getToken(headers);
			if (isValidToken(token, houseToDelete)){
				pm.deletePersistent(houseToDelete);

				return Response.ok().build();
			}else{
				return Response.status(403).build();
			}

		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch (Exception e){
			return serverErrorResponse("deleting the house "+id, e);
		}
	}
}
