package com.kitnhiks.houseduties.client.view;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Spinner {
	protected Logger logger = Logger.getLogger("HouseDutiesLogger");
	private FlowPanel layer;

	public Spinner(){
		layer = new FlowPanel();
		layer.getElement().setId("spinner");
	}

	public void startSpinner() {
		if (Document.get().getElementById("spinner") == null){
			RootPanel.get("wizard").add(layer);
		}
	}

	public void stopSpinner() {
		RootPanel.get("wizard").remove(layer);
	}
}
