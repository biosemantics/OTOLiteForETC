package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.sirls.shared.beans.hierarchy.Structure;
import edu.arizona.sirls.shared.beans.hierarchy.StructureNodeData;

public interface HierarchyServiceAsync {

	void getStructureList(String uploadID,
			AsyncCallback<ArrayList<Structure>> callback);

	void getNodeList(String uploadID,
			AsyncCallback<ArrayList<StructureNodeData>> callback);

	void saveTree(String uploadID, ArrayList<StructureNodeData> nodeDataList,
			AsyncCallback<Void> callback);

}
