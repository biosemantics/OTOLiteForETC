package edu.arizona.sirls.client.presenter.to_ontologies;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.event.to_ontologies.BackToDetailViewEvent;
import edu.arizona.sirls.client.event.to_ontologies.SubmitSubmissionEvent;
import edu.arizona.sirls.client.presenter.Presenter;
import edu.arizona.sirls.client.view.to_ontologies.OperationType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologySubmission;

public class EditSubmissionPresenter implements Presenter {
	public static interface Display {
		OperationType getType();

		Button getSubmitBtn();

		Button getBackBtn();

		String getOntologyValue();

		Image getBrowseOntologyIcon();

		OntologySubmission getDataToSubmit();

		OntologySubmission getOriginalData();

		Widget asWidget();
	}

	private final Display display;
	private final HandlerManager eventBus;

	public EditSubmissionPresenter(Display display, HandlerManager eventBus) {
		this.display = display;
		this.eventBus = eventBus;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		bindEvents();
		container.add(display.asWidget());
	}

	@Override
	public void bindEvents() {
		display.getBackBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new BackToDetailViewEvent(display
						.getOriginalData()));
			}
		});

		display.getSubmitBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new SubmitSubmissionEvent(display
						.getDataToSubmit(), display.getType()));
			}
		});

		display.getBrowseOntologyIcon().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(
						BrowseSuperClassURL.get(display.getOntologyValue()),
						"_blank", "");
			}
		});
	}

}
