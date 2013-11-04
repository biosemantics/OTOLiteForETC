package edu.arizona.sirls.client.view;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.widget.OtoTabPanel;

public class MainView extends Composite implements
		MainPresenter.Display {
	private OtoTabPanel tabPanel;

	public MainView() {
		ArrayList<String> tabNames = new ArrayList<String>();
		tabNames.add("To Ontology");
		tabNames.add("Hierarchy");
		tabNames.add("Orders");

		tabPanel = new OtoTabPanel(tabNames);
		initWidget(tabPanel.asWidget());
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public OtoTabPanel getTabPanel() {
		return tabPanel;
	}

}
