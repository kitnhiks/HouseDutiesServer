package com.kitnhiks.houseduties.server.resources.house.occupant;

import java.net.URI;
import java.util.List;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.kitnhiks.houseduties.server.model.Occupant;
import com.kitnhiks.houseduties.server.model.House;
import static com.kitnhiks.houseduties.server.resources.RESTService.pmfInstance;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/house/{houseId}/occupant")
public class OccupantRESTService {
	
//	@POST
//	@Consumes("application/json")
//	/**
//	 * Add an occupant to the house
//	 * @param itemList
//	 * @return
//	 */
//	public Response addOccupant(Occupant occupant, @PathParam("houseId") Long houseId){
//		occupant.setKey(null);
//		PersistenceManager pm = pmfInstance.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();
//		try {
//			tx.begin();
//			House house = pm.getObjectById(House.class, houseId);
//			house.addItem(occupant);
//			pm.makePersistent(house);
//			tx.commit();
//		} catch (JDOObjectNotFoundException e){
//			return Response.status(Status.NOT_FOUND).build();
//		}finally {
//			if (tx.isActive()) {
//				tx.rollback();
//			}
//			pm.close();
//		}
//		return Response.created(URI.create("/"+occupant.getKey().getId())).build();
//	}
	
//	@GET
//	@Path("{id}")
//	@Produces("application/json")
//	public Occupant getItem(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
//		PersistenceManager pm = pmfInstance.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();
//		Occupant occupant = null;
//		tx.begin();
//		House house = pm.getObjectById(House.class, houseId);
//		occupant = house.getOccupant(id);
//		return occupant;
//	}
	
//	@PUT
//	@Path("{id}")
//	@Consumes("application/json")
//	public void updateItem(Occupant occupant, @PathParam("houseId") Long houseId, @PathParam("id") Long id){
//		PersistenceManager pm = pmfInstance.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();
//		try {
//			tx.begin();
//			House house = pm.getObjectById(House.class, houseId);
//			Occupant occupantToUpdate = house.getOccupant(id);
//			occupantToUpdate.update(occupant);
//			pm.makePersistent(occupantToUpdate);
//			tx.commit();
//		} finally {
//			if (tx.isActive()) {
//				tx.rollback();
//			}
//			pm.close();
//		}
//	}
//	
//	@DELETE
//	@Path("{id}")
//	@Produces("application/json")
//	public void deleteItem(@PathParam("houseId") Long houseId, @PathParam("id") Long id) {
//		PersistenceManager pm = pmfInstance.getPersistenceManager();
//		Transaction tx = pm.currentTransaction();
//		try {
//			tx.begin();
//			House house = pm.getObjectById(House.class, houseId);
//			pm.deletePersistent(house.getOccupant(id));
//			tx.commit();
//		} finally {
//			if (tx.isActive()) {
//				tx.rollback();
//			}
//			pm.close();
//		}
//	}
	
}
