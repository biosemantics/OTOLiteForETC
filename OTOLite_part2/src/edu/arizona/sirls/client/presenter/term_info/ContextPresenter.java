package edu.arizona.sirls.client.presenter.term_info;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.event.context.ViewTermInfoEvent;
import edu.arizona.sirls.client.event.context.ViewTermInfoEventHandler;
import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.presenter.Presenter;
import edu.arizona.sirls.client.rpc.TermInfoService;
import edu.arizona.sirls.client.rpc.TermInfoServiceAsync;
import edu.arizona.sirls.client.view.term_info.ContextContentView;
import edu.arizona.sirls.client.view.term_info.DictionaryContentView;
import edu.arizona.sirls.client.view.term_info.GlossaryContentView;
import edu.arizona.sirls.shared.beans.term_info.TermContext;
import edu.arizona.sirls.shared.beans.term_info.TermDictionary;
import edu.arizona.sirls.shared.beans.term_info.TermGlossary;

public class ContextPresenter implements Presenter {

	public static interface Display {
		TabPanel getTabPanel();

		int getCurrentTabIndex();

		void setCurrentTabIndex(int index);

		void setTerm(String term);

		String getTerm();

		void clearTerm();

		SimplePanel getCurrentTabContent();

		Widget asWidget();
	}

	private final Display display;
	private HandlerManager globalEventBus;
	private TermInfoServiceAsync rpcService = GWT.create(TermInfoService.class);

	public ContextPresenter(Display display, HandlerManager globalEventBus) {
		this.display = display;
		this.globalEventBus = globalEventBus;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
		bindEvents();
	}

	@Override
	public void bindEvents() {
		display.getTabPanel().addSelectionHandler(
				new SelectionHandler<Integer>() {

					@Override
					public void onSelection(SelectionEvent<Integer> event) {
						display.setCurrentTabIndex(event.getSelectedItem());
						displayTermInfoInTab();
					}
				});

		globalEventBus.addHandler(ViewTermInfoEvent.TYPE,
				new ViewTermInfoEventHandler() {

					@Override
					public void onViewTermInfo(ViewTermInfoEvent event) {
						display.setTerm(event.getTerm());
						displayTermInfoInTab();
					}
				});
	}

	private void displayTermInfoInTab() {
		switch (display.getCurrentTabIndex()) {
		case 0:
			getContext();
			break;
		case 1:
			getGlossary();
			break;
		case 2:
			getDictionary();
			break;
		default:
			break;
		}
	}

	private void getContext() {
		String term = display.getTerm();
		if (term == null || term.equals("")) {
			display.clearTerm();
			return;
		}
		rpcService.getTermContext(term, MainPresenter.uploadID,
				new AsyncCallback<ArrayList<TermContext>>() {

					@Override
					public void onSuccess(ArrayList<TermContext> result) {
						new ContextContentPresenter(new ContextContentView(
								result, display.getTerm())).go(display
								.getCurrentTabContent());

					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed to get term context. \n\n"
								+ caught.getMessage());
					}
				});
	}

	private void getGlossary() {
		String term = display.getTerm();
		if (term == null || term.equals("")) {
			display.clearTerm();
			return;
		}
		rpcService.getTermGlossary(term,
				Integer.toString(MainPresenter.uploadInfo.getGlossaryType()),
				new AsyncCallback<ArrayList<TermGlossary>>() {

					@Override
					public void onSuccess(ArrayList<TermGlossary> result) {
						new GlossaryContentPresenter(new GlossaryContentView(
								result, display.getTerm())).go(display
								.getCurrentTabContent());
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed to get term glossary. \n\n"
								+ caught.getLocalizedMessage());
					}
				});
	}

	private void getDictionary() {
		String term = display.getTerm();
		if (term == null || term.equals("")) {
			display.clearTerm();
			return;
		}
		rpcService.getTermDictionary(term, new AsyncCallback<TermDictionary>() {

			@Override
			public void onSuccess(TermDictionary result) {
				new DictionaryContentPresenter(new DictionaryContentView())
						.go(display.getCurrentTabContent());

			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server Error: failed to get term dictionary. \n\n"
						+ caught.getLocalizedMessage());

			}
		});
	}
}
