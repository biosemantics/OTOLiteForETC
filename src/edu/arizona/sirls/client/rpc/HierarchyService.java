package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.sirls.shared.beans.hierarchy.Structure;

@RemoteServiceRelativePath("hierarchy")
public interface HierarchyService extends RemoteService {
	ArrayList<Structure> getStructureList(String uploadID) throws Exception;
}
