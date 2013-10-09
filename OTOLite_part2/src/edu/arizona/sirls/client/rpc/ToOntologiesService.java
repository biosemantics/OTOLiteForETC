package edu.arizona.sirls.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.arizona.sirls.client.view.to_ontologies.OperationType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyMatch;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecord;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecordType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologySubmission;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryLists;

@RemoteServiceRelativePath("toOntologies")
public interface ToOntologiesService extends RemoteService {
	TermCategoryLists getTermCategoryLists(String uploadID) throws Exception;

	/**
	 * move pair between regular list and removed list
	 * 
	 * @param uploadID
	 * @param termCategoryPairID
	 * @param isRemove
	 * @throws Exception
	 */
	void moveTermCategoryPair(String uploadID, String termCategoryPairID,
			boolean isRemove) throws Exception;

	/**
	 * select an ontology record as the mapped record
	 * 
	 * @param uploadID
	 * @param term
	 * @param category
	 * @param recordID
	 * @param recordType
	 * @throws Exception
	 */
	void updateSelectedOntologyRecord(String uploadID, String term,
			String category, String recordID, OntologyRecordType recordType)
			throws Exception;

	/**
	 * get the ontology records of a given pair
	 * 
	 * @param uploadID
	 * @param term
	 * @param category
	 * @return
	 * @throws Exception
	 */
	ArrayList<OntologyRecord> getOntologyRecords(String uploadID, String term,
			String category) throws Exception;

	OntologyMatch getMatchDetail(String matchID) throws Exception;

	void clearSelection(String glossaryType, String term, String category)
			throws Exception;

	OntologySubmission getSubmissionDetail(String submissionID)
			throws Exception;

	void deleteSubmission(OntologySubmission submission, String uploadID)
			throws Exception;

	void submitSubmission(OntologySubmission submission, String uploadID,
			OperationType type) throws Exception;

	OntologySubmission getDefaultDataForNewSubmission(String uploadID,
			String term, String category) throws Exception;
}
