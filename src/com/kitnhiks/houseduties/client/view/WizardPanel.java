package com.kitnhiks.houseduties.client.view;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.kitnhiks.houseduties.client.controller.GenericRequestCallback;
import com.kitnhiks.houseduties.client.controller.HttpHelper;

public class WizardPanel extends FlowPanel{
	
	protected Logger logger = Logger.getLogger("HouseDutiesLogger");
	protected Spinner spinner;
	private Label errorLabel, messageLabel;
	private HttpHelper httpHelper = new HttpHelper();

	public WizardPanel(){
		spinner = new Spinner();
		spinner.startSpinner();
		showWizardPanel();
		spinner.stopSpinner();
	}

	// AFFICHAGE
	
	private void showWizardPanel(){

		// Zone des message d'erreur
		errorLabel = new Label();
		errorLabel.getElement().setId("errorLabel");
		this.add(errorLabel);
		errorLabel.setVisible(false);
		
		// Zone des message
		messageLabel = new Label();
		messageLabel.getElement().setId("messageLabel");
		this.add(messageLabel);
		messageLabel.setVisible(false);
	}

	protected void showError(String message){
		// TODO : faire un errorPanel
		errorLabel.setText("Error ("+new Date().getTime()+") :"+message);
		errorLabel.setVisible(true);
		// TODO : virer le errorPanel après X seconds
	}
	
	protected void showMessage(String message){
		// TODO : faire un messagePanel
		messageLabel.setText("Message ("+new Date().getTime()+") :"+message);
		messageLabel.setVisible(true);
		// TODO : virer le messageLabel après click
	}
	
	protected void show404(String message){
		showError("404 : "+message);
	}

	// Request

	/**
	 * Lance une requête http GET pour récupérer du JSON
	 * @param url
	 * @param callback
	 */
	public void httpGetJson(String url, GenericRequestCallback callback) {
		try {
			spinner.startSpinner();
			httpHelper.httpGetJson(url, callback);
		} catch (RequestException e) {
			logger.log(Level.SEVERE, e.getMessage());
			showError("Error while sending request.");
			spinner.stopSpinner();
		}
	}
	
	/**
	 * Lance une requête http POST pour envoyer du JSON
	 * @param jsonObject les données à poster au format JSON
	 * @param url
	 * @param callback
	 */
	public void httpPostJson(JSONObject jsonObject, String url, GenericRequestCallback callback) {
		try {
			spinner.startSpinner();
			httpHelper.httpPostJson(jsonObject, url, callback);
		} catch (RequestException e) {
			logger.log(Level.SEVERE, e.getMessage());
			showError("Error while sending request.");
			spinner.stopSpinner();
		}
	}

	/**
	 * Lance une requête http PUT pour envoyer du JSON
	 * @param jsonObject les données à mettre à jour au format JSON
	 * @param url
	 * @param callback
	 */
	public void httpPutJson(JSONObject jsonObject, String url, GenericRequestCallback callback) {
		try {
			spinner.startSpinner();
			httpHelper.httpPutJson(jsonObject, url, callback);
		} catch (RequestException e) {
			logger.log(Level.SEVERE, e.getMessage());
			showError("Error while sending request.");
			spinner.stopSpinner();
		}
	}
	
	/**
	 * Lance une requête http DELETE
	 * @param url
	 * @param callback
	 */
	public void httpDeleteJson(String url, GenericRequestCallback callback) {
		try {
			spinner.startSpinner();
			httpHelper.httpDeleteJson(url, callback);
		} catch (RequestException e) {
			logger.log(Level.SEVERE, e.getMessage());
			showError("Error while sending request.");
			spinner.stopSpinner();
		}
	}
	
	abstract class WizardRequestCallback extends GenericRequestCallback {

		@Override
		public void handleError(String message) {
			logger.log(Level.FINEST, "erreur de la requête ");
			showError(message);
			spinner.stopSpinner();
		}

		@Override
		public void handleResponse(Response response) {
			logger.log(Level.FINEST, "réponse de la requête");
			switch (response.getStatusCode()){
			case Response.SC_NOT_FOUND: // 404
				showError("Erreur requete not found");
				break;
			}
			showResponse(response);
			spinner.stopSpinner();
		}
		
		public abstract void showResponse(Response response);
	}
}