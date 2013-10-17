package edu.arizona.sirls.server.rpc;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.sirls.client.rpc.HierarchyService;
import edu.arizona.sirls.shared.beans.hierarchy.Structure;

public class HierarchyServiceImpl extends RemoteServiceServlet implements
		HierarchyService {

	private static final long serialVersionUID = -8088784352943361785L;

	@Override
	public ArrayList<Structure> getStructureList(String uploadID)
			throws Exception {
		ArrayList<Structure> structures = new ArrayList<Structure>();
		for (int i = 0; i < 20; i++) {
			Structure s = new Structure();
			s.setName("structure_" + Integer.toString(i));
			structures.add(s);
		}
		return structures;
	}

}
