package edu.arizona.sirls.server.rpc;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.sirls.client.rpc.HierarchyService;
import edu.arizona.sirls.server.db.HierarchyDAO;
import edu.arizona.sirls.server.oto.QueryOTO;
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

	@Override
	public boolean isStructureExistInOTO(String termName, String glossaryType)
			throws Exception {
		boolean result = QueryOTO.getInstance().isTripleExistInOTO(termName,
				"structure", glossaryType);
		return result;
	}

	@Override
	public Structure addStructure(String termName, String uploadID)
			throws Exception {
		return HierarchyDAO.getInstance().addStructure(uploadID, termName);
	}

	@Override
	public Structure addStructureToOTOAndDB(String termName, String uploadID,
			String glossaryType, String definition) throws Exception {
		QueryOTO.getInstance().insertTripleToOTO(termName, "structure",
				glossaryType, definition);
		return HierarchyDAO.getInstance().addStructure(uploadID, termName);
	}

	@Override
	public void prepopulateTree(String uploadID) throws Exception {
		// TODO wait for Hong's implementation

		/**
		 * first delete the tree
		 * 
		 * input: a list of terms with permanentID in ontology_match table
		 * particular ontology
		 * 
		 * do: for each term, trace part_of to top, check if any term exist in
		 * the input list. if do exist, add record in table [trees]
		 * 
		 * 
		 * 
		 * when insert to trees: always insert 2 records:
		 * 
		 * 1. (insert or update if A's parent is 0) A is child of B
		 * 
		 * 2. B is child of 0
		 * 
		 * Then, remove B from input term list
		 * 
		 * keep doing this, until the input term list is empty
		 * 
		 * 
		 */

	}

}
