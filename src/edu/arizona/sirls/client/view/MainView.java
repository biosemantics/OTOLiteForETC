package edu.arizona.sirls.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.MainPresenter;

public class MainView extends Composite implements MainPresenter.Display {
	private TabPanel mainTabPanel;
	private SimplePanel to_ontology_content_panel;
	private SimplePanel hierarchy_content_panel;
	private SimplePanel order_content_panel;

	public MainView() {
		mainTabPanel = new TabPanel();
		mainTabPanel.addStyleName("main_tab_panel");
		initWidget(mainTabPanel);
		mainTabPanel.setSize("100%", "100%");

		to_ontology_content_panel = new SimplePanel();
		to_ontology_content_panel.addStyleName("contentPanel");
		to_ontology_content_panel.setSize("100%", "100%");

		hierarchy_content_panel = new SimplePanel();
		hierarchy_content_panel.addStyleName("contentPanel");
		hierarchy_content_panel.setSize("100%", "100%");

		order_content_panel = new SimplePanel();
		order_content_panel.addStyleName("contentPanel");
		order_content_panel.setSize("100%", "100%");

		mainTabPanel.add(to_ontology_content_panel, "To Ontologies");
		mainTabPanel.add(hierarchy_content_panel, "Hierarchy");
		mainTabPanel.add(order_content_panel, "Order");
	}

	@Override
	public TabPanel getTabPanel() {
		return this.mainTabPanel;
	}

	@Override
	public SimplePanel getOrderContentContainer() {
		return this.order_content_panel;
	}

	@Override
	public SimplePanel getHierarchyContentContainer() {
		return this.hierarchy_content_panel;
	}

	@Override
	public SimplePanel getToOntologiesContentContainer() {
		return this.to_ontology_content_panel;
	}

	@Override
	public void fillInTabContent(SimplePanel contentContainer,
			HasWidgets content) {
		contentContainer.clear();
		contentContainer.add(contentContainer);

	}

	public Widget asWidget() {
		return this;
	}

}
