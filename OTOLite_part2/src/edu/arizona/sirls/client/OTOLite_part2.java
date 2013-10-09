package edu.arizona.sirls.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.view.MainView;

public class OTOLite_part2 implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		try {
			new MainPresenter(new MainView()).go(RootPanel.get("CONTENT"));
		} catch (Exception e) {
			Window.alert(e.getMessage());
			e.printStackTrace();
		}
	}
}
