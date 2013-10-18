package edu.arizona.sirls.server.oto;

import java.io.IOException;

import oto.full.OTOClient;

public class QueryOTO extends AbstractOTOAccessObject {

	private static QueryOTO instance;

	public static QueryOTO getInstance() throws IOException {
		if (instance == null) {
			instance = new QueryOTO();
		}
		return instance;
	}

	public QueryOTO() throws IOException {
		super();
	}

	/**
	 * given a triple with optional definition, get existing or generated uuid
	 * from OTO
	 * 
	 * @param term
	 * @param category
	 * @param glossaryType
	 *            : in string
	 * @param definition
	 * @return
	 */
	public String getUUID(String term, String category, String glossaryType,
			String definition) {
		OTOClient otoClient = createOTOClient();
		return otoClient.insertAndGetGlossaryDictionaryEntry(glossaryType,
				term, category, definition).getTermID();
	}

}
