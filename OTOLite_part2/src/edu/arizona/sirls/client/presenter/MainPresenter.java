package edu.arizona.sirls.client.presenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.arizona.sirls.client.presenter.orders.OrdersPagePresenter;
import edu.arizona.sirls.client.presenter.to_ontologies.ToOntologyPresenter;
import edu.arizona.sirls.client.rpc.GeneralService;
import edu.arizona.sirls.client.rpc.GeneralServiceAsync;
import edu.arizona.sirls.client.view.orders.OrdersPageView;
import edu.arizona.sirls.client.view.to_ontologies.ToOntologyView;
import edu.arizona.sirls.shared.beans.UploadInfo;

public class MainPresenter implements Presenter {

	public interface Display {
		TabPanel getTabPanel();

		SimplePanel getOrderContentContainer();

		SimplePanel getHierarchyContentContainer();

		SimplePanel getToOntologiesContentContainer();

		void fillInTabContent(SimplePanel contentContainer, HasWidgets content);

		Widget asWidget();
	}

	private final Display display;
	public static String uploadID;
	public static UploadInfo uploadInfo;
	private GeneralServiceAsync rpcService = GWT.create(GeneralService.class);

	public MainPresenter(Display view) throws Exception {
		this.display = view;
		uploadID = Validator.validateUploadID();
	}

	public void bindEvents() {
		display.getTabPanel().addSelectionHandler(
				new SelectionHandler<Integer>() {

					@Override
					public void onSelection(SelectionEvent<Integer> event) {
						switch (event.getSelectedItem()) {
						case 0:
							new ToOntologyPresenter(new ToOntologyView())
									.go(display
											.getToOntologiesContentContainer());
							break;
						case 1:
							Window.alert("TODO: hierarchy page");
							break;
						case 2:
							new OrdersPagePresenter(new OrdersPageView())
									.go(display.getOrderContentContainer());
							break;
						default:
							break;
						}
					}
				});
	}

	@Override
	public void go(HasWidgets container) {
		bindEvents();
		container.clear();
		container.add(display.asWidget());
		fetchUploadInfo();
		display.getTabPanel().selectTab(0);
	}

	private void fetchUploadInfo() {
		rpcService.getUploadInfo(uploadID, new AsyncCallback<UploadInfo>() {

			@Override
			public void onSuccess(UploadInfo result) {
				uploadInfo = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server Error: failed in getting upload info. \n\n"
						+ caught.getMessage());
			}
		});
	}

	public UploadInfo getUploadInfo() {
		return uploadInfo;
	}

}
