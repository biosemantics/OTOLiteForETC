package edu.arizona.sirls.server.rpc;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.sirls.client.rpc.TermInfoService;
import edu.arizona.sirls.server.db.ContextDAO;
import edu.arizona.sirls.server.oto.QueryOTO;
import edu.arizona.sirls.server.utilities.Utilities;
import edu.arizona.sirls.shared.beans.term_info.TermContext;
import edu.arizona.sirls.shared.beans.term_info.TermDictionary;
import edu.arizona.sirls.shared.beans.term_info.TermGlossary;

public class TermInfoServiceIml extends RemoteServiceServlet implements
		TermInfoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8348271874856917361L;

	@Override
	public ArrayList<TermContext> getTermContext(String term, String uploadID)
			throws Exception {
		return ContextDAO.getInstance().getTermContext(term,
				Integer.parseInt(uploadID));
	}

	@Override
	public ArrayList<TermGlossary> getTermGlossary(String term,
			String glossaryType) throws Exception {
		return QueryOTO.getInstance().getGlossaryInfo(term,
				Utilities.getGlossaryNameByID(Integer.parseInt(glossaryType)));
	}

	@Override
	public TermDictionary getTermDictionary(String term) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
