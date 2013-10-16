package edu.arizona.sirls.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.presenter.term_info.ContextPresenter;
import edu.arizona.sirls.client.view.MainView;
import edu.arizona.sirls.client.view.term_info.ContextView;

public class OTOLite_part2 implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		try {
			HandlerManager globalEventBus = new HandlerManager(null);

			new MainPresenter(new MainView(), globalEventBus).go(RootPanel
					.get("MAIN_CONTENT"));
			new ContextPresenter(new ContextView(), globalEventBus)
					.go(RootPanel.get("TERM_INFO"));
		} catch (Exception e) {
			Window.alert(e.getMessage());
			e.printStackTrace();
		}
	}
}
