package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.sirls.shared.beans.hierarchy.Structure;
import edu.arizona.sirls.shared.beans.hierarchy.StructureNodeData;

@RemoteServiceRelativePath("hierarchy")
public interface HierarchyService extends RemoteService {
	ArrayList<Structure> getStructureList(String uploadID) throws Exception;

	ArrayList<StructureNodeData> getNodeList(String uploadID) throws Exception;

	void saveTree(String uploadID, ArrayList<StructureNodeData> nodeDataList)
			throws Exception;
}
