package edu.arizona.sirls.client.view.term_info;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.term_info.TermInfoPresenter;

public class TermInfoView extends Composite implements TermInfoPresenter.Display {
	private TabPanel tabPanel;
	private int currentTabIndex;
	private SimplePanel currentTabContent;
	private SimplePanel contextContent;
	private SimplePanel definitionContent;
	private SimplePanel glossaryContent;
	private String term;

	public TermInfoView() {
		tabPanel = new TabPanel();
		tabPanel.setSize("100%", "100%");
		initWidget(tabPanel);
		tabPanel.setAnimationEnabled(true);

		// create context tab
		contextContent = new SimplePanel();
		contextContent.setSize("100%", "100%");
		tabPanel.add(contextContent, "Context");

		// create glossary tab
		glossaryContent = new SimplePanel();
		glossaryContent.setSize("100%", "100%");
		tabPanel.add(glossaryContent, "Ontology");

		// create definition tab
		definitionContent = new SimplePanel();
		definitionContent.setSize("100%", "100%");
		tabPanel.add(definitionContent, "Dictionary");

		tabPanel.selectTab(0);
		currentTabIndex = 0;
		currentTabContent = contextContent;
	}

	@Override
	public TabPanel getTabPanel() {
		return tabPanel;
	}

	public Widget asWidget() {
		return this;
	}

	@Override
	public int getCurrentTabIndex() {
		return currentTabIndex;
	}

	@Override
	public void setCurrentTabIndex(int index) {
		this.currentTabIndex = index;
		switch (index) {
		case 0:
			this.currentTabContent = contextContent;
			break;
		case 1:
			this.currentTabContent = glossaryContent;
			break;
		case 2:
			this.currentTabContent = definitionContent;
			break;
		default:
			break;
		}
	}

	@Override
	public SimplePanel getCurrentTabContent() {
		return currentTabContent;
	}

	@Override
	public void setTerm(String term) {
		this.term = term;
	}

	@Override
	public String getTerm() {
		return term;
	}

	@Override
	public void clearTerm() {
		contextContent.clear();
		glossaryContent.clear();
		definitionContent.clear();
	}

}
