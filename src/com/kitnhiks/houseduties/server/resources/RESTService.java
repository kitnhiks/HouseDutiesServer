package com.kitnhiks.houseduties.server.resources;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.ws.rs.core.Response;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class RESTService {
	public static final PersistenceManagerFactory pmfInstance =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	public static String AuthKey = "user";

	protected Logger logger = Logger.getLogger(this.getClass().getName());

	public RESTService(){
		BasicConfigurator.configure();
	}

	protected Response serverErrorResponse(String whileMessage, Exception e){
		logger.error("Problem while "+whileMessage + " : " + e.getMessage());
		return Response.status(500).entity("There has been a problem while "+whileMessage+". Please try again later.").build();
	}
}
