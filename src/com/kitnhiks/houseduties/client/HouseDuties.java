package com.kitnhiks.houseduties.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.kitnhiks.houseduties.client.view.HouseLoginPanel;

public class HouseDuties implements EntryPoint, ValueChangeHandler<String> {

	private Panel wizardPanel;
	protected Logger logger = Logger.getLogger("HouseDutiesLogger");
	
	public void onModuleLoad() {
		logger.log(Level.FINEST, "Ouverture de la page...");
		RootPanel.get("howto").setStyleName("hidden", true);
		RootPanel.get("helpButton").addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel howToDesc = RootPanel.get("howto");
				if (howToDesc.getStyleName().contains("hidden")){
					howToDesc.setStyleName("hidden", false);
				}else{
					howToDesc.setStyleName("hidden", true);
				}
			}
		}, ClickEvent.getType());

		Window.setTitle(Const.PAGE_TITLE_HOME);

		// On vire le spinner de base
		Document.get().getElementById("spinner").removeFromParent();

		// Add history listener
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String url = event.getValue();
		String[] urlTokens = url.split("/");
		int nbTokens = urlTokens.length;

		//Affiche le Panel d'accueil
		wizardPanel = new HouseLoginPanel();
		
		// On ajoute le panel dans la page
		RootPanel.get("menu").clear();
		RootPanel.get("wizard").clear();
		RootPanel.get("wizard").add(wizardPanel);
	}
}
