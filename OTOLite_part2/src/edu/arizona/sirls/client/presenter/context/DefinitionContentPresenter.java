package edu.arizona.sirls.client.presenter.context;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.Presenter;
import edu.arizona.sirls.shared.beans.context.TermDictionary;

public class DefinitionContentPresenter implements Presenter {

	public static interface Display {
		CellTable<TermDictionary> getTbl();

		Widget asWidget();
	}

	private final Display display;

	public DefinitionContentPresenter(Display display) {
		this.display = display;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
		bindEvents();
	}

	@Override
	public void bindEvents() {
		// TODO Auto-generated method stub

	}
}
