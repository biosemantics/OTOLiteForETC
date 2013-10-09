package edu.arizona.sirls.client.event.to_ontologies;

import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.sirls.client.view.to_ontologies.TermCategoryPairView;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryPair;

public class MoveTermCategoryPairEvent extends
		GwtEvent<MoveTermCategoryPairEventHandler> {
	public static Type<MoveTermCategoryPairEventHandler> TYPE = new Type<MoveTermCategoryPairEventHandler>();
	private TermCategoryPair data;
	private TermCategoryPairView widget;

	public MoveTermCategoryPairEvent(TermCategoryPair data, TermCategoryPairView widget) {
		this.setData(data);
		this.setWidget(widget);
	}

	@Override
	public Type<MoveTermCategoryPairEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MoveTermCategoryPairEventHandler handler) {
		handler.onMove(this);
	}

	public TermCategoryPair getData() {
		return data;
	}

	public void setData(TermCategoryPair data) {
		this.data = data;
	}

	public TermCategoryPairView getWidget() {
		return widget;
	}

	public void setWidget(TermCategoryPairView widget) {
		this.widget = widget;
	}

}
