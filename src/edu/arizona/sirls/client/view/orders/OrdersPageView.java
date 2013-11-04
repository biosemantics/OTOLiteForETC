package edu.arizona.sirls.client.view.orders;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.orders.OrdersPagePresenter;

public class OrdersPageView extends Composite implements
		OrdersPagePresenter.Display {
	private VerticalPanel panel;

	public OrdersPageView() {
		ScrollPanel layout = new ScrollPanel();
		layout.setSize("100%", "100%");
		initWidget(layout);
		
		panel = new VerticalPanel();
		panel.setWidth("100%");
		layout.add(panel);
	}

	@Override
	public VerticalPanel getPanel() {
		return panel;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

}
