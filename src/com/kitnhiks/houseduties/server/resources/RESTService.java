package com.kitnhiks.houseduties.server.resources;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class RESTService {
	public static final PersistenceManagerFactory pmfInstance = 
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	public static String AuthKey = "user";
}
