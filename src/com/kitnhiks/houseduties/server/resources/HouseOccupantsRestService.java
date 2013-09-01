package com.kitnhiks.houseduties.server.resources;

import static com.kitnhiks.houseduties.server.resources.RESTConst.AUTH_KEY_HEADER;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.getToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.isValidToken;
import static com.kitnhiks.houseduties.server.utils.AuthTokenizer.renewToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.kitnhiks.houseduties.server.model.House;
import com.kitnhiks.houseduties.server.model.Occupant;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/occupants")
public class HouseOccupantsRESTService extends RESTService{

	public HouseOccupantsRESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	@GET
	@Produces("application/json")
	public Response getOccupants(//
			@Context HttpHeaders headers, //
			@PathParam("houseId") Long houseId) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);

			if (isValidToken(token, house)){
				ArrayList<Occupant> occupants = new ArrayList<Occupant>();

				for(Occupant occupant : house.getOccupants()){
					occupants.add(pm.detachCopy(pm.getObjectById(Occupant.class, occupant.getKey())));
				}

				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity(occupants).build();
			}else{
				return Response.status(403).build();
			}
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving occupants from house "+houseId, e);
		}
	}

	@GET
	@Path("/ladder")
	@Produces("application/json")
	public Response getOccupantsLadder(//
			@Context HttpHeaders headers, //
			@PathParam("houseId") Long houseId) {
		try{
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			House house = pm.getObjectById(House.class, houseId);
			String token = getToken(headers);

			if (isValidToken(token, house)){
				ArrayList<Occupant> occupants = new ArrayList<Occupant>();

				for(Occupant occupant : house.getOccupants()){
					occupants.add(pm.detachCopy(pm.getObjectById(Occupant.class, occupant.getKey())));
				}

				Collections.sort(occupants, new Comparator<Occupant>(){
					@Override
					public int compare(Occupant firstOccupant, Occupant secondOccupant) {
						if (firstOccupant.getPoints() == secondOccupant.getPoints()){
							return 0;
						}else if (firstOccupant.getPoints() > secondOccupant.getPoints()){
							return -1;
						}else{
							return 1;
						}
					}});
				return Response.status(200).header(AUTH_KEY_HEADER, renewToken(token)).entity(occupants).build();
			}else{
				return Response.status(403).build();
			}
		} catch (JDOObjectNotFoundException e){
			return Response.status(404).build();
		}catch(Exception e){
			return serverErrorResponse("retrieving occupants from house "+houseId, e);
		}
	}
}
