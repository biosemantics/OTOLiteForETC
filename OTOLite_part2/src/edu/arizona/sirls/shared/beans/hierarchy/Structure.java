package edu.arizona.sirls.shared.beans.hierarchy;

import java.io.Serializable;

public class Structure implements Serializable {

	private static final long serialVersionUID = 2829643559543281759L;
	private String name;
	private boolean dragged;
	private int structureID;

	public Structure() {
		// has to be here for GWT serializabl
	}

	public Structure(int id, String name, boolean dragged) {
		this.structureID = id;
		this.name = name;
		this.dragged = dragged;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDragged() {
		return dragged;
	}

	public void setDragged(boolean dragged) {
		this.dragged = dragged;
	}

	public int getStructureID() {
		return structureID;
	}

	public void setStructureID(int structureID) {
		this.structureID = structureID;
	}
}
