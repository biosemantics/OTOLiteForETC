package edu.arizona.sirls.client.presenter.to_ontologies;

import static com.google.gwt.query.client.GQuery.$;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.event.to_ontologies.MoveTermCategoryPairEvent;
import edu.arizona.sirls.client.event.to_ontologies.TermCategoryPairSelectedEvent;
import edu.arizona.sirls.client.presenter.Presenter;
import edu.arizona.sirls.client.view.to_ontologies.TermCategoryPairView;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryPair;

public class TermCategoryPairPresenter implements Presenter {

	public static interface Display {
		TermCategoryPair getTermCategoryPair();

		Image getActionBtn();

		Label getNameLabel();

		Widget asWidget();
	}

	private final Display display;
	private final HandlerManager eventBus;

	/**
	 * constructer
	 * 
	 * @param view
	 */
	public TermCategoryPairPresenter(Display view, HandlerManager eventBus) {
		this.display = view;
		this.eventBus = eventBus;
	}

	@Override
	public void go(HasWidgets container) {
		bindEvents();
		// container.clear();
		container.add(display.asWidget());
	}

	@Override
	public void bindEvents() {
		display.getNameLabel().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				$(".TO_ONTOLOGY_currentPair").removeClass(
						"TO_ONTOLOGY_currentPair");
				display.getNameLabel().addStyleName("TO_ONTOLOGY_currentPair");

				eventBus.fireEvent(new TermCategoryPairSelectedEvent(display
						.getTermCategoryPair(), display.getNameLabel()));
			}
		});

		display.getActionBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new MoveTermCategoryPairEvent(display
						.getTermCategoryPair(), (TermCategoryPairView) display));
			}
		});

	}
}
