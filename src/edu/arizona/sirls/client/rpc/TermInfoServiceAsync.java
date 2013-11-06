package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.sirls.shared.beans.term_info.TermContext;
import edu.arizona.sirls.shared.beans.term_info.TermDictionary;
import edu.arizona.sirls.shared.beans.term_info.TermGlossary;

public interface TermInfoServiceAsync {

	void getTermContext(String term, String uploadID,
			AsyncCallback<ArrayList<TermContext>> callback);

	void getTermGlossary(String term, String glossaryType,
			AsyncCallback<ArrayList<TermGlossary>> callback);

	void getTermDictionary(String term, AsyncCallback<TermDictionary> callback);

	void getFileContent(String uploadID, String sourceName,
			AsyncCallback<String> callback);

}
