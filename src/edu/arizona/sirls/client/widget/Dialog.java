package edu.arizona.sirls.client.widget;

import edu.arizona.sirls.client.presenter.general.AlertDialogCallback;
import edu.arizona.sirls.client.presenter.general.ConfirmBoxPresenter;
import edu.arizona.sirls.client.presenter.general.ConfirmDialogCallback;
import edu.arizona.sirls.client.view.general.ConfirmBoxView;

/**
 * Use this class to replace Window.confirm since Window.confirm causes dnd
 * problem in firefox
 * 
 * copied from: http://stackoverflow.com/questions/3162399
 * /gwt-confirmation-dialog-box/3163042#3163042
 * 
 */
public class Dialog {
	private static ConfirmBoxView view = null;
	private static ConfirmBoxPresenter presenter = null;

	public static ConfirmBoxPresenter confirm(String header, String dialogText,
			String cancelButtonText, String affirmativeButtonText,
			ConfirmDialogCallback callback) {
		view = new ConfirmBoxView();
		presenter = new ConfirmBoxPresenter(view, header, dialogText,
				cancelButtonText, affirmativeButtonText, callback);

		presenter.init();

		return presenter;
	}

	public static ConfirmBoxPresenter confirm(String header, String dialogText,
			ConfirmDialogCallback callback) {
		return Dialog.confirm(header, dialogText, "Cancel", "OK", callback);
	}

	public static ConfirmBoxPresenter alert(String header, String dialogText,
			String affirmativeButtonText, AlertDialogCallback callback) {
		view = new ConfirmBoxView();
		presenter = new ConfirmBoxPresenter(view, header, dialogText,
				affirmativeButtonText, callback);

		presenter.init();

		return presenter;
	}

	public static ConfirmBoxPresenter alert(String header, String dialogText,
			AlertDialogCallback callback) {
		return Dialog.alert(header, dialogText, "OK", callback);
	}

	protected Dialog() {
		//
	}
}
