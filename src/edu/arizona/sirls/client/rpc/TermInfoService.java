package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.sirls.shared.beans.term_info.TermContext;
import edu.arizona.sirls.shared.beans.term_info.TermDictionary;
import edu.arizona.sirls.shared.beans.term_info.TermGlossary;

@RemoteServiceRelativePath("termInfo")
public interface TermInfoService extends RemoteService {
	ArrayList<TermContext> getTermContext(String term, String uploadID)
			throws Exception;

	ArrayList<TermGlossary> getTermGlossary(String term, String glossaryType)
			throws Exception;

	TermDictionary getTermDictionary(String term) throws Exception;
}
