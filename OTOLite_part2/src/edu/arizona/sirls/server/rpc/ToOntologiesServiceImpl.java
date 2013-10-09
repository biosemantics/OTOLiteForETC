package edu.arizona.sirls.server.rpc;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.sirls.client.rpc.ToOntologiesService;
import edu.arizona.sirls.client.view.to_ontologies.OperationType;
import edu.arizona.sirls.server.bioportal.TermsToOntologiesClient;
import edu.arizona.sirls.server.db.GeneralDAO;
import edu.arizona.sirls.server.db.ToOntologiesDAO;
import edu.arizona.sirls.shared.beans.UploadInfo;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyMatch;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecord;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecordType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologySubmission;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryLists;

public class ToOntologiesServiceImpl extends RemoteServiceServlet implements
		ToOntologiesService {

	private static final long serialVersionUID = 8235809276166612584L;

	@Override
	public void moveTermCategoryPair(String uploadID,
			String termCategoryPairID, boolean isRemove) throws Exception {
		ToOntologiesDAO.getInstance().moveTermCategoryPair(
				Integer.parseInt(uploadID),
				Integer.parseInt(termCategoryPairID), isRemove);
	}

	@Override
	public TermCategoryLists getTermCategoryLists(String uploadID)
			throws Exception {
		return ToOntologiesDAO.getInstance().getTermCategoryPairsLists(
				Integer.parseInt(uploadID));
	}

	@Override
	public ArrayList<OntologyRecord> getOntologyRecords(String uploadID,
			String term, String category) throws Exception {
		return ToOntologiesDAO.getInstance().getOntologyRecords(
				Integer.parseInt(uploadID), term, category);
	}

	@Override
	public void updateSelectedOntologyRecord(String uploadID, String term,
			String category, String recordID, OntologyRecordType recordType)
			throws Exception {
		ToOntologiesDAO.getInstance().updateSelectedOntologyRecord(
				Integer.parseInt(uploadID), term, category, recordType,
				Integer.parseInt(recordID));
	}

	@Override
	public OntologyMatch getMatchDetail(String matchID) throws Exception {
		return ToOntologiesDAO.getInstance().getOntologyMatchByID(
				Integer.parseInt(matchID));
	}

	@Override
	public OntologySubmission getSubmissionDetail(String submissionID)
			throws Exception {
		return ToOntologiesDAO.getInstance().getOntologySubmissionByID(
				Integer.parseInt(submissionID));
	}

	@Override
	public void deleteSubmission(OntologySubmission submission, String uploadID)
			throws Exception {
		UploadInfo info = GeneralDAO.getInstance().getUploadInfo(
				Integer.parseInt(uploadID));
		TermsToOntologiesClient sendToOntologyClient = new TermsToOntologiesClient(
				info.getBioportalUserID(), info.getBioportalApiKey());
		sendToOntologyClient.deleteTerm(submission);
		ToOntologiesDAO.getInstance().deleteSubmission(
				Integer.parseInt(submission.getSubmissionID()));
	}

	@Override
	public void submitSubmission(OntologySubmission submission,
			String uploadID, OperationType type) throws Exception {
		UploadInfo info = GeneralDAO.getInstance().getUploadInfo(
				Integer.parseInt(uploadID));
		TermsToOntologiesClient sendToOntologyClient = new TermsToOntologiesClient(
				info.getBioportalUserID(), info.getBioportalApiKey());
		if (type.equals(OperationType.NEW_SUBMISSION)) {
			String tmpID = sendToOntologyClient.submitTerm(submission);
			submission.setTmpID(tmpID);
			ToOntologiesDAO.getInstance().addSubmission(submission,
					Integer.parseInt(uploadID));
		} else {
			sendToOntologyClient.updateTerm(submission);
			ToOntologiesDAO.getInstance().updateSubmission(submission);
		}
	}

	@Override
	public OntologySubmission getDefaultDataForNewSubmission(String uploadID,
			String term, String category) throws Exception {
		return ToOntologiesDAO.getInstance().getDefaultDataForNewSubmission(
				Integer.parseInt(uploadID), term, category);
	}

	@Override
	public void clearSelection(String glossaryType, String term, String category)
			throws Exception {
		ToOntologiesDAO.getInstance().clearSelection(
				Integer.parseInt(glossaryType), term, category);

	}

}
