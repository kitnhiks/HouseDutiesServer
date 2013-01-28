package com.kitnhiks.houseduties.client.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.json.client.JSONObject;

public class HttpHelper {
	
	private Logger logger = Logger.getLogger("HouseDutiesLogger");

	private void httpRequestJson(Method httpMethod, JSONObject jsonObject, String url, GenericRequestCallback callback) throws RequestException{
		logger.log(Level.FINEST, "lance la requête "+url);
		RequestBuilder builder = new RequestBuilder(httpMethod, url);
		builder.setHeader("Content-Type", "application/json");
		builder.sendRequest(jsonObject==null?null:jsonObject.toString(), callback);
	}
	
	/**
	 * Lance une requête http GET pour récupérer du JSON
	 * @param url
	 * @param callback
	 * @throws RequestException 
	 */
	public void httpGetJson(String url, GenericRequestCallback callback) throws RequestException {
		httpRequestJson(RequestBuilder.GET, null, url, callback);
	}
	
	/**
	 * Lance une requête http POST pour envoyer du JSON
	 * @param jsonObject les données à poster au format JSON
	 * @param url
	 * @param callback
	 * @throws RequestException 
	 */
	public void httpPostJson(JSONObject jsonObject, String url, GenericRequestCallback callback) throws RequestException {
		httpRequestJson(RequestBuilder.POST, jsonObject, url, callback);
	}

	/**
	 * Lance une requête http PUT pour envoyer du JSON
	 * @param jsonObject les données à mettre à jour au format JSON
	 * @param url
	 * @param callback
	 * @throws RequestException 
	 */
	public void httpPutJson(JSONObject jsonObject, String url, GenericRequestCallback callback) throws RequestException {
		httpRequestJson(RequestBuilder.PUT, jsonObject, url, callback);
	}
	
	/**
	 * Lance une requête http DELETE
	 * @param url
	 * @param callback
	 */
	public void httpDeleteJson(String url, GenericRequestCallback callback) throws RequestException {
		httpRequestJson(RequestBuilder.DELETE, null, url, callback);
	}

}