package com.kitnhiks.houseduties.client.view;

import java.util.logging.Level;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.kitnhiks.houseduties.client.Const;

public class HouseLoginPanel extends WizardPanel{

	public HouseLoginPanel(){
		super();
		logger.log(Level.FINEST, "Chargement du panel de création...");
		Window.setTitle(Const.PAGE_TITLE_HOUSE_LOGIN);
		this.getElement().setId("itemListCreationPanel");
		showLoginPanel();
		logger.log(Level.FINEST, "Panel de création chargé");
	}
	
	public void showLoginPanel(){
		FlowPanel loginPanel = new FlowPanel();
		loginPanel.add(new Label("LOGIN !!!"));
	}
}
