package edu.arizona.sirls.client.view.context;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;

import edu.arizona.sirls.client.presenter.context.DefinitionContentPresenter;
import edu.arizona.sirls.shared.beans.context.TermDictionary;

public class DefinitionContentView extends Composite implements
		DefinitionContentPresenter.Display {

	private CellTable<TermDictionary> table;

	public DefinitionContentView() {
		table = new CellTable<TermDictionary>();
		table.setSize("100%", "100%");
		initWidget(table);
	}

	@Override
	public CellTable<TermDictionary> getTbl() {
		return table;
	}

}
