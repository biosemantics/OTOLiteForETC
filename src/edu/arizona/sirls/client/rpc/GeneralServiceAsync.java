package edu.arizona.sirls.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.sirls.shared.beans.UploadInfo;

@RemoteServiceRelativePath("general")
public interface GeneralServiceAsync {

	void getUploadInfo(String uploadID, String secret, AsyncCallback<UploadInfo> callback);

}
