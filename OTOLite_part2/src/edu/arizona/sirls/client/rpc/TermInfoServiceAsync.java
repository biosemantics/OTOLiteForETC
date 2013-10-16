package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.sirls.shared.beans.context.TermContext;
import edu.arizona.sirls.shared.beans.context.TermDictionary;
import edu.arizona.sirls.shared.beans.context.TermGlossary;

public interface TermInfoServiceAsync {

	void getTermContext(String term, String uploadID,
			AsyncCallback<ArrayList<TermContext>> callback);

	void getTermGlossary(String term, String glossaryType,
			AsyncCallback<ArrayList<TermGlossary>> callback);

	void getTermDictionary(String term, AsyncCallback<TermDictionary> callback);

}
