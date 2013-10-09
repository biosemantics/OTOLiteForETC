package edu.arizona.sirls.shared.beans.to_ontologies;

import java.io.Serializable;

import edu.arizona.sirls.client.view.to_ontologies.OperationType;

public class OntologySubmissionLite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6833776057619871529L;
	private OperationType type;
	private String submissionID;
	private String term;
	private String category;
	private String ontology;
	private String superClassID;
	private String definition;
	private String synonyms;
	private String source;
	private String sampleSentence;

	public OntologySubmissionLite() {

	}

	public OntologySubmissionLite(OperationType type, String term,
			String category) {
		this.type = type;
		this.term = term;
		this.category = category;
	}

	public String getSubmissionID() {
		return submissionID;
	}

	public void setSubmissionID(String submissionID) {
		this.submissionID = submissionID;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	public String getSuperClassID() {
		return superClassID;
	}

	public void setSuperClassID(String superClassID) {
		this.superClassID = superClassID;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}

	public String getSampleSentence() {
		return sampleSentence;
	}

	public void setSampleSentence(String sampleSentence) {
		this.sampleSentence = sampleSentence;
	}
}
