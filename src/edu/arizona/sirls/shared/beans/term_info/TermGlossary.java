package edu.arizona.sirls.shared.beans.term_info;

import java.io.Serializable;

public class TermGlossary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2191681473010354133L;
	private String category;
	private String definition;

	public TermGlossary() {

	}

	public TermGlossary(String category, String definition) {
		this.category = category;
		this.definition = definition;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}
}
