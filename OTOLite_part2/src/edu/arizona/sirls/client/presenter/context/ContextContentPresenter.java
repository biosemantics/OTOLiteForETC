package edu.arizona.sirls.client.presenter.context;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.Presenter;

public class ContextContentPresenter implements Presenter {
	public static interface Display {
		Widget asWidget();
	}

	private final Display display;

	public ContextContentPresenter(Display display) {
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
