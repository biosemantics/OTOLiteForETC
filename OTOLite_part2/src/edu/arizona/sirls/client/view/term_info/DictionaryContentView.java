package edu.arizona.sirls.client.view.term_info;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;

import edu.arizona.sirls.client.presenter.term_info.DictionaryContentPresenter;
import edu.arizona.sirls.shared.beans.term_info.TermDictionary;

public class DictionaryContentView extends Composite implements
		DictionaryContentPresenter.Display {

	private CellTable<TermDictionary> table;

	public DictionaryContentView() {
		table = new CellTable<TermDictionary>();
		table.setSize("100%", "100%");
		initWidget(table);
	}

	@Override
	public CellTable<TermDictionary> getTbl() {
		return table;
	}

}
