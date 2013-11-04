package edu.arizona.sirls.client.view.term_info;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.term_info.ContextContentPresenter;
import edu.arizona.sirls.shared.beans.term_info.TermContext;

public class ContextContentView extends Composite implements
		ContextContentPresenter.Display {

	public ContextContentView(ArrayList<TermContext> contexts, String term) {
		if (contexts.size() > 0) {
			ScrollPanel layout = new ScrollPanel();
			layout.setSize("100%", "100%");
			initWidget(layout);

			CellTable<TermContext> table = new CellTable<TermContext>();
			table.setSize("100%", "100%");
			layout.add(table);

			// first column: source
			TextColumn<TermContext> sourceColumn = new TextColumn<TermContext>() {

				@Override
				public String getValue(TermContext object) {
					return object.getSource();
				}
			};
			table.addColumn(sourceColumn, "Source of '" + term + "'");
			table.setColumnWidth(sourceColumn, "20%");

			// second column: sentence
			TextColumn<TermContext> sentenceColumn = new TextColumn<TermContext>() {

				@Override
				public String getValue(TermContext object) {
					return object.getSentence();
				}
			};
			table.addColumn(sentenceColumn, "Sentence of '" + term + "'");
			table.setColumnWidth(sentenceColumn, "80%");

			// fill in data
			table.setRowCount(contexts.size());
			table.setRowData(contexts);
		} else {
			// label
			Label noRecordLbl = new Label("No context record found for term '"
					+ term + "'");
			initWidget(noRecordLbl);
		}
	}

	public Widget asWidget() {
		return this;
	}

}
