package edu.arizona.sirls.client.event.orders;

import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.sirls.client.view.orders.DroppableContainerView;

public class DropTermToBoxEvent extends GwtEvent<DropTermToBoxEventHandler> {
	public static Type<DropTermToBoxEventHandler> TYPE = new Type<DropTermToBoxEventHandler>();

	private DroppableContainerView widget;

	public DropTermToBoxEvent(DroppableContainerView w) {
		this.setWidget(w);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DropTermToBoxEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DropTermToBoxEventHandler handler) {
		handler.onDropTermToBox(this);

	}

	public DroppableContainerView getWidget() {
		return widget;
	}

	public void setWidget(DroppableContainerView widget) {
		this.widget = widget;
	}

}
