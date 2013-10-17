package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.arizona.sirls.client.view.to_ontologies.OperationType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyMatch;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecord;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecordType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologySubmission;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryLists;

public interface ToOntologiesServiceAsync {

	void moveTermCategoryPair(String uploadID, String termCategoryPairID,
			boolean isRemove, AsyncCallback<Void> callback);

	void getTermCategoryLists(String uploadID,
			AsyncCallback<TermCategoryLists> callback);

	void getOntologyRecords(String uploadID, String term, String category,
			AsyncCallback<ArrayList<OntologyRecord>> callback);

	void updateSelectedOntologyRecord(String uploadID, String term,
			String category, String recordID, OntologyRecordType recordType,
			AsyncCallback<Void> callback);

	void getMatchDetail(String matchID, AsyncCallback<OntologyMatch> callback);

	void getSubmissionDetail(String submissionID,
			AsyncCallback<OntologySubmission> callback);

	void deleteSubmission(OntologySubmission submission, String uploadID,
			AsyncCallback<Void> callback);

	void submitSubmission(OntologySubmission submission, String uploadID,
			OperationType type, AsyncCallback<Void> callback);

	void getDefaultDataForNewSubmission(String uploadID, String term,
			String category, AsyncCallback<OntologySubmission> callback);

	void clearSelection(String glossaryType, String term, String category,
			AsyncCallback<Void> callback);

	void refreshOntologyStatus(String uploadID, AsyncCallback<Void> callback);

}
