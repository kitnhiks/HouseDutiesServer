package com.kitnhiks.houseduties.server.resources;

import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.ws.rs.core.Response;



public class RESTService {
	public static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	public static String AuthKey = "user";

	protected Logger logger;

	public RESTService(){
		logger = Logger.getLogger(this.getClass().getName());
	}

	protected Response serverErrorResponse(String whileMessage, Exception e){
		logger.severe("Problem while "+whileMessage + " : " + e.getMessage());
		return Response.status(500).entity("There has been a problem while "+whileMessage+". Please try again later.").build();
	}
}
