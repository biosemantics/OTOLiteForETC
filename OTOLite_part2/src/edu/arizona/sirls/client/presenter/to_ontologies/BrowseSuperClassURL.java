package edu.arizona.sirls.client.presenter.to_ontologies;

public class BrowseSuperClassURL {
	private static String poroURL = "http://purl.bioontology.org/ontology/PORO";
	private static String OLSURL = "http://www.ebi.ac.uk/ontology-lookup/";

	public static String get(String ontologyName) {
		if (ontologyName.equals("PORO")) {
			return poroURL;
		} else {
			return OLSURL + "browse.do?ontName=" + ontologyName;
		}
	}

}
