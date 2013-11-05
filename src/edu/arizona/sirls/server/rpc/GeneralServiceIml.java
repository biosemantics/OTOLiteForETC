package edu.arizona.sirls.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.sirls.client.rpc.GeneralService;
import edu.arizona.sirls.server.db.GeneralDAO;
import edu.arizona.sirls.shared.beans.UploadInfo;

public class GeneralServiceIml extends RemoteServiceServlet implements
		GeneralService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2193065585421857808L;

	@Override
	public UploadInfo getUploadInfo(String uploadID, String secret) throws Exception {
		return GeneralDAO.getInstance().getUploadInfo(
				Integer.parseInt(uploadID), secret);
	}

}
