package edu.arizona.sirls.client.event.to_ontologies;

import com.google.gwt.event.shared.GwtEvent;

import edu.arizona.sirls.client.view.to_ontologies.OperationType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologySubmission;

public class SubmitSubmissionEvent extends
		GwtEvent<SubmitSubmissionEventHandler> {

	public static Type<SubmitSubmissionEventHandler> TYPE = new Type<SubmitSubmissionEventHandler>();
	private OntologySubmission submission;
	private OperationType submissionType;

	public SubmitSubmissionEvent(OntologySubmission submission,
			OperationType type) {
		this.submission = submission;
		this.setSubmissionType(type);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SubmitSubmissionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SubmitSubmissionEventHandler handler) {
		handler.onSubmit(this);
	}

	public OntologySubmission getSubmission() {
		return submission;
	}

	public void setSubmission(OntologySubmission submission) {
		this.submission = submission;
	}

	public OperationType getSubmissionType() {
		return submissionType;
	}

	public void setSubmissionType(OperationType submissionType) {
		this.submissionType = submissionType;
	}

}
