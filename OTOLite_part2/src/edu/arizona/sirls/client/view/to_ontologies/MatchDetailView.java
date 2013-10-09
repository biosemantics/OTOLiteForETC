package edu.arizona.sirls.client.view.to_ontologies;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;

import edu.arizona.sirls.client.presenter.to_ontologies.MatchDetailPresenter;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyMatch;

public class MatchDetailView extends Composite implements
		MatchDetailPresenter.Display {

	public MatchDetailView(OntologyMatch match) {
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setSize("100%", "100%");
		initWidget(decPanel);

		FlexTable layout = new FlexTable();
		layout.setSize("100%", "100%");
		decPanel.setWidget(layout);
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
		ColumnFormatter columnFormatter = layout.getColumnFormatter();

		layout.setHTML(0, 0, "Ontology Match Detail");
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.addStyleName(0, 0, "tbl_title");
		columnFormatter.setWidth(0, "30%");
		columnFormatter.setWidth(1, "70%");

		int row = 1;
		layout.setHTML(row, 0, "Term: ");
		cellFormatter.addStyleName(row, 0, "tbl_field_label");
		layout.setHTML(row, 1, match.getTerm());

		row++;
		layout.setHTML(row, 0, "Category: ");
		cellFormatter.addStyleName(row, 0, "tbl_field_label");
		layout.setHTML(row, 1, match.getCategory());

		row++;
		layout.setHTML(row, 0, "Ontology: ");
		cellFormatter.addStyleName(row, 0, "tbl_field_label");
		layout.setHTML(row, 1, match.getOntologyID());

		row++;
		layout.setHTML(row, 0, "Parent Term: ");
		cellFormatter.addStyleName(row, 0, "tbl_field_label");
		layout.setHTML(row, 1, match.getParentTerm());

		row++;
		layout.setHTML(row, 0, "Definition: ");
		cellFormatter.addStyleName(row, 0, "tbl_field_label");
		layout.setHTML(row, 1, match.getDefinition());

		row++;
		layout.setHTML(row, 0, "Permanent ID: ");
		cellFormatter.addStyleName(row, 0, "tbl_field_label");
		layout.setHTML(row, 1, match.getPermanentID());
	}

	public Widget asWidget() {
		return this;
	}
}
