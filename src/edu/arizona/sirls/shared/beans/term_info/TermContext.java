package edu.arizona.sirls.shared.beans.term_info;

import java.io.Serializable;

public class TermContext implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2446934367710935529L;
	private String source;
	private String srcFilePath;
	private boolean srcFileAccessable;
	private String sentence;

	public TermContext() {

	}

	public TermContext(String src, String sentence) {
		this.sentence = sentence;
		this.source = src;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSrcFilePath() {
		return srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	public boolean isSrcFileAccessable() {
		return srcFileAccessable;
	}

	public void setSrcFileAccessable(boolean srcFileAccessable) {
		this.srcFileAccessable = srcFileAccessable;
	}
}
