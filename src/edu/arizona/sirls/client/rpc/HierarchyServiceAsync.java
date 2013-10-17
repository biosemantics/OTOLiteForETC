package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;


import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.sirls.shared.beans.hierarchy.Structure;

public interface HierarchyServiceAsync {

	void getStructureList(String uploadID,
			AsyncCallback<ArrayList<Structure>> callback);

}
