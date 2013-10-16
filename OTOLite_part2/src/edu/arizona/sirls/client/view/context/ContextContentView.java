package edu.arizona.sirls.client.view.context;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.context.ContextContentPresenter;
import edu.arizona.sirls.shared.beans.context.TermContext;

public class ContextContentView extends Composite implements
		ContextContentPresenter.Display {

	public ContextContentView(ArrayList<TermContext> contexts, String term) {
		if (contexts.size() > 0) {

			CellTable<TermContext> table = new CellTable<TermContext>();
			table.setSize("100%", "100%");
			initWidget(table);

			// first column: source
			TextColumn<TermContext> sourceColumn = new TextColumn<TermContext>() {

				@Override
				public String getValue(TermContext object) {
					return object.getSource();
				}
			};
			table.addColumn(sourceColumn, "Source");
			table.setColumnWidth(sourceColumn, "30%");

			// second column: sentence
			TextColumn<TermContext> sentenceColumn = new TextColumn<TermContext>() {

				@Override
				public String getValue(TermContext object) {
					return object.getSentence();
				}
			};
			table.addColumn(sentenceColumn, "Sentence");
			table.setColumnWidth(sentenceColumn, "70%");

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
