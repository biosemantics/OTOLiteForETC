package edu.arizona.sirls.server.rpc;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.sirls.client.rpc.HierarchyService;
import edu.arizona.sirls.server.db.HierarchyDAO;
import edu.arizona.sirls.shared.beans.hierarchy.Structure;
import edu.arizona.sirls.shared.beans.hierarchy.StructureNodeData;

public class HierarchyServiceImpl extends RemoteServiceServlet implements
		HierarchyService {

	private static final long serialVersionUID = -8088784352943361785L;

	@Override
	public ArrayList<Structure> getStructureList(String uploadID)
			throws Exception {
		return HierarchyDAO.getInstance().getStructuresList(
				Integer.parseInt(uploadID));
	}

	@Override
	public ArrayList<StructureNodeData> getNodeList(String uploadID)
			throws Exception {
		return HierarchyDAO.getInstance().getNodeList(
				Integer.parseInt(uploadID));
	}

	@Override
	public void saveTree(String uploadID,
			ArrayList<StructureNodeData> nodeDataList) throws Exception {
		HierarchyDAO.getInstance().saveTree(uploadID, nodeDataList);
	}

}
