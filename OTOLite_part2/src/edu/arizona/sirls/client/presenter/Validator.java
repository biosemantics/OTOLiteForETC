package edu.arizona.sirls.client.presenter;

import com.google.gwt.user.client.Window;

public class Validator {
	public static String validateUploadID() throws Exception {
		String uploadID = Window.Location.getParameter("uploadID");
		if (uploadID == null) {
			throw new Exception("uploadID required to retrieve OTO Lite!");
		}
		return uploadID;
	}
}
