package edu.arizona.sirls.client.event.processing;

import com.google.gwt.event.shared.EventHandler;

public interface ProcessingStartEventHandler extends EventHandler {
	void onProcessingStart(ProcessingStartEvent event);
}
