package edu.arizona.sirls.client.event.hierarchy;

import com.google.gwt.event.shared.EventHandler;

public interface DragTreeNodeStartEventHandler extends EventHandler {
	void onDragStart(DragTreeNodeStartEvent event);
}
