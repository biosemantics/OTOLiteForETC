package edu.arizona.sirls.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.sirls.shared.beans.UploadInfo;

@RemoteServiceRelativePath("general")
public interface GeneralService extends RemoteService{
	UploadInfo getUploadInfo(String uploadID) throws Exception;
}
