package com.kitnhiks.houseduties.server.resources;


import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.generateToken;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	public Response fetchHouse(@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			House house = pm.detachCopy(pm.getObjectById(House.class, id));

			return Response.status(200).entity(house).build();

		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch (Exception e){
			return serverErrorResponse("retrieving the house "+id, e);
		}
	}

	@PUT
	@Path("{id}")
	@Consumes("application/json")
	public Response updateHouse (House house, @PathParam("id") Long id){
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House houseToUpdate = pm.getObjectById(House.class, id);

			houseToUpdate.update(house);
			pm.makePersistent(houseToUpdate);

			return Response.ok().build();

		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch (Exception e){
			return serverErrorResponse("updating the house "+id, e);
		}
	}

	@DELETE
	@Path("{id}")
	@Produces("application/json")
	public Response deleteHouse(@PathParam("id") Long id) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();

			House houseToDelete = pm.getObjectById(House.class, id);

			pm.deletePersistent(houseToDelete);

			return Response.ok().build();

		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch (Exception e){
			return serverErrorResponse("deleting the house "+id, e);
		}
	}
}
