package edu.arizona.sirls.client.view.term_info;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.term_info.GlossaryContentPresenter;
import edu.arizona.sirls.shared.beans.term_info.TermGlossary;

public class GlossaryContentView extends Composite implements
		GlossaryContentPresenter.Display {

	public GlossaryContentView(ArrayList<TermGlossary> glossaries, String term) {
		if (glossaries.size() > 0) {
			CellTable<TermGlossary> table = new CellTable<TermGlossary>();
			table.setSize("100%", "100%");
			initWidget(table);

			// first column: category
			TextColumn<TermGlossary> categoryColumn = new TextColumn<TermGlossary>() {

				@Override
				public String getValue(TermGlossary object) {
					return object.getCategory();
				}
			};
			table.addColumn(categoryColumn, "Category");
			table.setColumnWidth(categoryColumn, "25%");

			// second column: sentence
			TextColumn<TermGlossary> definitionColumn = new TextColumn<TermGlossary>() {

				@Override
				public String getValue(TermGlossary object) {
					return object.getDefinition();
				}
			};
			table.addColumn(definitionColumn, "Definition");
			table.setColumnWidth(definitionColumn, "80%");

			// fill in data
			table.setRowCount(glossaries.size());
			table.setRowData(glossaries);
		} else {
			// label
			Label noRecordLbl = new Label("No glossary record found for term '"
					+ term + "'");
			initWidget(noRecordLbl);
		}
	}

	public Widget asWidget() {
		return this;
	}

}
