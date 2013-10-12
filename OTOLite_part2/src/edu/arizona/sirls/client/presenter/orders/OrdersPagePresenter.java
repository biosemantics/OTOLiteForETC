package edu.arizona.sirls.client.presenter.orders;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.MainPresenter;
import edu.arizona.sirls.client.presenter.Presenter;
import edu.arizona.sirls.client.rpc.OrderService;
import edu.arizona.sirls.client.rpc.OrderServiceAsync;
import edu.arizona.sirls.client.view.orders.SingleOrderSetView;
import edu.arizona.sirls.shared.beans.orders.OrderCategory;

public class OrdersPagePresenter implements Presenter {

	public static interface Display {
		VerticalPanel getPanel();

		Widget asWidget();
	}

	private final Display display;
	private OrderServiceAsync rpcService = GWT.create(OrderService.class);

	public OrdersPagePresenter(Display display) {
		this.display = display;

	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		bindEvents();
		fetchOrderCategories();
		container.add(display.asWidget());
	}

	@Override
	public void bindEvents() {
		// nothing to bind here
	}

	private void fetchOrderCategories() {
		rpcService.getOrderCategories(MainPresenter.uploadID,
				new AsyncCallback<ArrayList<OrderCategory>>() {

					@Override
					public void onSuccess(ArrayList<OrderCategory> result) {
						for (OrderCategory category : result) {
							new SingleOrderSetPresenter(new SingleOrderSetView(
									category), rpcService).go(display
									.getPanel());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server Error: failed to get order categories. \n\n"
								+ caught.getMessage());
					}
				});
	}

}
