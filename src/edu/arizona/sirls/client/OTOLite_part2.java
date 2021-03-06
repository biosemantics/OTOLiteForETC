package edu.arizona.sirls.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.presenter.processing.ProcessingMsgPresenter;
import edu.arizona.sirls.client.presenter.term_info.TermInfoPresenter;
import edu.arizona.sirls.client.view.MainView;
import edu.arizona.sirls.client.view.processing.ProcessingMsgView;
import edu.arizona.sirls.client.view.term_info.TermInfoView;

public class OTOLite_part2 implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		try {
			// create global event bus
			HandlerManager globalEventBus = new HandlerManager(null);

			// build page
			new MainPresenter(new MainView(), globalEventBus,
					RootPanel.get("MAIN_CONTENT")).go(null);
			new TermInfoPresenter(new TermInfoView(), globalEventBus)
					.go(RootPanel.get("TERM_INFO"));

			// pop up message panel during processing
			new ProcessingMsgPresenter(new ProcessingMsgView(), globalEventBus)
					.go(null);
		} catch (Exception e) {
			Window.alert(e.getMessage());
			e.printStackTrace();
		}
	}
}
