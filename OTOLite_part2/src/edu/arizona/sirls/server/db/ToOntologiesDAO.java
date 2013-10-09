package edu.arizona.sirls.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.arizona.sirls.shared.beans.UploadInfo;
import edu.arizona.sirls.shared.beans.to_ontologies.MappingStatus;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyMatch;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecord;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologyRecordType;
import edu.arizona.sirls.shared.beans.to_ontologies.OntologySubmission;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryLists;
import edu.arizona.sirls.shared.beans.to_ontologies.TermCategoryPair;

public class ToOntologiesDAO extends AbstractDAO {
	private static ToOntologiesDAO instance;

	public static ToOntologiesDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new ToOntologiesDAO();
		}
		return instance;
	}

	/**
	 * default constructor
	 * 
	 * @throws Exception
	 */
	public ToOntologiesDAO() throws Exception {
		super();
	}

	// in db, couldn't save enum type, therefore translate it to integer values
	private int translateOntologyRecordType(OntologyRecordType type) {
		if (type.equals(OntologyRecordType.MATCH)) {
			return 1;
		} else {
			return 2;
		}
	}

	private OntologyRecordType translateToOntologyRecordType(int i) {
		if (i == 1) {
			return OntologyRecordType.MATCH;
		} else {
			return OntologyRecordType.SUBMISSION;
		}
	}

	public OntologyMatch getOntologyMatchByID(int ID) throws SQLException {
		OntologyMatch match = new OntologyMatch();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "select * from ontology_matches where Id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ID);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				match = new OntologyMatch(rset.getString("term"),
						rset.getString("category"));
				match.setMatchingInfo(rset.getString("ontologyID"),
						rset.getString("permanentID"),
						rset.getString("parentTerm"),
						rset.getString("definition"));
			}
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
		}

		return match;
	}

	public OntologySubmission getOntologySubmissionByID(int ID)
			throws SQLException {
		OntologySubmission submission = new OntologySubmission();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "select * from ontology_submissions where ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ID);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				submission.setTerm(rset.getString("term"));
				submission.setCategory(rset.getString("category"));
				submission.setSubmissionID(rset.getString("ID"));
				submission.setSubmittedBy(rset.getString("submittedBy"));
				submission.setLocalID(rset.getString("localID"));
				submission.setTmpID(rset.getString("tmpID"));
				submission.setPermanentID(rset.getString("permanentID"));
				submission.setSuperClass(rset.getString("superClassID"));
				submission.setDefinition(rset.getString("definition"));
				submission.setOntologyID(rset.getString("ontologyID"));
				submission.setSource(rset.getString("source"));
				submission.setSampleSentence(rset.getString("sampleSentence"));
				submission.setSynonyms(rset.getString("synonyms"));
			}
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
		}

		return submission;
	}

	public OntologySubmission getDefaultDataForNewSubmission(int uploadID,
			String term, String category) throws Exception {
		OntologySubmission submission = new OntologySubmission();
		submission.setTerm(term);
		submission.setCategory(category);

		UploadInfo info = GeneralDAO.getInstance().getUploadInfo(uploadID);
		submission.setSource(info.getSource());

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			conn = getConnection();

			// get synonyms
			String sql = "select synonyms from term_category_pair where uploadID = ? and term = ? and category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uploadID);
			pstmt.setString(2, term);
			pstmt.setString(3, category);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				submission.setSynonyms(rset.getString("synonyms"));
			}

			// get sample sentence
			sql = "select sentence from sentences where uploadID = ? "
					+ "and sentence rlike '^(.*\\s)?" + term + "(\\s.*)?$'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uploadID);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				// get the first sentence as sample sentence
				submission.setSampleSentence(rset.getString(1));
			}
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
		}

		return submission;
	}

	public ArrayList<OntologySubmission> getPendingOntologySubmissions()
			throws SQLException {
		ArrayList<OntologySubmission> submissions = new ArrayList<OntologySubmission>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "select * from ontology_submissions where permanentID is NULL or permanentID = ''";
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				OntologySubmission submission = new OntologySubmission();
				submission.setTerm(rset.getString("term"));
				submission.setCategory(rset.getString("category"));
				submission.setSubmissionID(rset.getString("ID"));
				submission.setSubmittedBy(rset.getString("submittedBy"));
				submission.setLocalID(rset.getString("localID"));
				submission.setTmpID(rset.getString("tmpID"));
				submission.setPermanentID(rset.getString("permanentID"));
				submission.setSuperClass(rset.getString("superClassID"));
				submission.setDefinition(rset.getString("definition"));
				submission.setOntologyID(rset.getString("ontologyID"));
				submission.setSource(rset.getString("source"));
				submission.setSynonyms(rset.getString("synonyms"));

				submissions.add(submission);
			}
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
		}

		return submissions;
	}

	/**
	 * update selected match record to a <term, category, glossaryType>
	 * 
	 * @param uploadID
	 * @param term
	 * @param category
	 * @param ID
	 * @param type
	 * @param recordID
	 * @throws Exception
	 */
	public void updateSelectedOntologyRecord(int uploadID, String term,
			String category, OntologyRecordType type, int recordID)
			throws Exception {
		int glossaryType = GeneralDAO.getInstance().getGlossaryTypeByUploadID(
				uploadID);
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = getConnection();

			// delete existing record
			String sql = "delete from selected_ontology_records where glossaryType = ? and "
					+ "term = ? and category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, glossaryType);
			pstmt.setString(2, term);
			pstmt.setString(3, category);
			pstmt.executeUpdate();

			// insert new record
			sql = "insert into selected_ontology_records "
					+ "(term, category, glossaryType, recordType, recordID) "
					+ "values (?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, term);
			pstmt.setString(2, category);
			pstmt.setInt(3, glossaryType);
			pstmt.setInt(4, translateOntologyRecordType(type));
			pstmt.setInt(5, recordID);
			pstmt.executeUpdate();
		} finally {
			closeConnection(conn);
			close(pstmt);
		}

	}

	public void clearSelection(int glossaryType, String term, String category)
			throws Exception {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			// delete existing record
			String sql = "delete from selected_ontology_records where glossaryType = ? and "
					+ "term = ? and category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, glossaryType);
			pstmt.setString(2, term);
			pstmt.setString(3, category);
			pstmt.executeUpdate();
		} finally {
			closeConnection(conn);
			close(pstmt);
		}
	}

	/**
	 * get both matches and submissions as ontology record
	 * 
	 * @param uploadID
	 * @param term
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public ArrayList<OntologyRecord> getOntologyRecords(int uploadID,
			String term, String category) throws Exception {
		ArrayList<OntologyRecord> records = new ArrayList<OntologyRecord>();
		int glossaryType = GeneralDAO.getInstance().getGlossaryTypeByUploadID(
				uploadID);
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;

		try {
			conn = getConnection();

			// get selected ontology record
			boolean hasSelected = false;
			int selectedType = 0; // 1-match, 2-submission
			int selectedID = 0;
			String sql = "select * from selected_ontology_records where glossaryType = ? and "
					+ "term = ? and category = ? limit 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, glossaryType);
			pstmt.setString(2, term);
			pstmt.setString(3, category);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				hasSelected = true;
				selectedType = rset.getInt("recordType");
				selectedID = rset.getInt("recordID");
			}

			// matches: glossary global
			sql = "select * from ontology_matches where glossaryType = ? "
					+ "and term = ? and category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, glossaryType);
			pstmt.setString(2, term);
			pstmt.setString(3, category);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				OntologyRecord record = new OntologyRecord(term, category);
				int ID = rset.getInt("ID");
				record.setType(OntologyRecordType.MATCH);
				record.setId(Integer.toString(ID));
				record.setDefinition(rset.getString("definition"));
				record.setOntology(rset.getString("ontologyID") + " ["
						+ rset.getString("permanentID") + "]");
				record.setParent(rset.getString("parentTerm"));

				// get selected
				if (hasSelected && selectedType == 1 && selectedID == ID) {
					record.setSelected(true);
				}

				records.add(record);
			}

			// submissions: entire system global
			sql = "select * from ontology_submissions where term = ? and category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, term);
			pstmt.setString(2, category);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				OntologyRecord record = new OntologyRecord(term, category);
				int ID = rset.getInt("ID");
				record.setType(OntologyRecordType.SUBMISSION);
				record.setId(Integer.toString(ID));
				record.setDefinition(rset.getString("definition"));
				record.setParent(rset.getString("superClassID"));
				record.setOntology(rset.getString("ontologyID")
						+ (rset.getBoolean("accepted") ? " [Accepted]"
								: " [Pending]"));

				// get selected
				if (hasSelected && selectedType == 2 && selectedID == ID) {
					record.setSelected(true);
				}

				records.add(record);
			}
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
		}

		return records;
	}

	public TermCategoryLists getTermCategoryPairsLists(int uploadID)
			throws Exception {
		TermCategoryLists lists = new TermCategoryLists();
		int glossaryType = GeneralDAO.getInstance().getGlossaryTypeByUploadID(
				uploadID);
		PreparedStatement pstmt = null;
		ResultSet rset = null, rset2 = null;
		Connection conn = null;
		try {
			conn = getConnection();

			boolean isGetRemoved = false;
			boolean isGetStructure = true;
			for (int i = 0; i < 4; i++) {
				switch (i) {
				case 0:
					isGetRemoved = false;
					isGetStructure = true;
					break;
				case 1:
					isGetRemoved = false;
					isGetStructure = false;
					break;
				case 2:
					isGetRemoved = true;
					isGetStructure = true;
					break;
				case 3:
					isGetRemoved = true;
					isGetStructure = false;
					break;
				default:
					break;
				}

				String sql = "select ID, term, category, removed from term_category_pair where uploadID = ? ";
				if (isGetStructure) {
					sql += "and category = 'structure' ";
				} else {
					sql += "and category <> 'structure' ";
				}
				sql += "and removed = ? order by term, category";
				ArrayList<TermCategoryPair> tcList = new ArrayList<TermCategoryPair>();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, uploadID);
				pstmt.setBoolean(2, isGetRemoved);
				rset = pstmt.executeQuery();
				while (rset.next()) {
					int pairID = rset.getInt("ID");
					String term = rset.getString("term");
					String category = rset.getString("category");
					TermCategoryPair pair = new TermCategoryPair(
							Integer.toString(pairID), term, category);
					pair.setRemoved(rset.getBoolean("removed"));
					pair.setIsStructure(isGetStructure);

					// get mapping status
					sql = "select * from selected_ontology_records "
							+ "where term = ? and category = ? and glossaryType = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, term);
					pstmt.setString(2, category);
					pstmt.setInt(3, glossaryType);
					rset2 = pstmt.executeQuery();
					if (rset2.next()) {
						if (translateToOntologyRecordType(
								rset2.getInt("recordType")).equals(
								OntologyRecordType.MATCH)) {
							pair.setStatus(MappingStatus.MAPPED_TO_MATCH);
						} else {
							pair.setStatus(MappingStatus.MAPPED_TO_SUBMISSION);
						}
					} else {
						pair.setStatus(MappingStatus.NOT_MAPPED);
					}

					// add the pair to the list
					tcList.add(pair);
				}

				switch (i) {
				case 0:
					lists.setRegularStructures(tcList);
					break;
				case 1:
					lists.setRegularCharacters(tcList);
					break;
				case 2:
					lists.setRemovedStructures(tcList);
					break;
				case 3:
					lists.setRemovedCharacters(tcList);
					break;
				default:
					break;
				}
			}
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
			close(rset2);
		}
		return lists;
	}

	/**
	 * either remove the term(category) pair from regular list, or move the pair
	 * back to the regular list
	 * 
	 * @param uploadID
	 * @param termCategoryPairID
	 * @param isRemove
	 * @throws SQLException
	 */
	public void moveTermCategoryPair(int uploadID, int termCategoryPairID,
			boolean isRemove) throws SQLException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "update term_category_pair set removed = ? where uploadID = ? "
					+ "and ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, isRemove);
			pstmt.setInt(2, uploadID);
			pstmt.setInt(3, termCategoryPairID);
			pstmt.executeUpdate();
		} finally {
			closeConnection(conn);
			close(pstmt);
		}
	}

	public void updatePermanentIDOfSubmission(int submissionID)
			throws SQLException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "update ontology_submissions set permanentID = ? "
					+ "where ID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, submissionID);
			pstmt.executeUpdate();
		} finally {
			closeConnection(conn);
			close(pstmt);
		}
	}

	/**
	 * delete a submission from db
	 * 
	 * @param submissionID
	 * @throws SQLException
	 */
	public void deleteSubmission(int submissionID) throws SQLException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "delete from ontology_submissions where ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, submissionID);
			pstmt.executeUpdate();

			// delete mapped records to this submission
			sql = "delete from selected_ontology_records where recordType = ? "
					+ "and recordID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,
					translateOntologyRecordType(OntologyRecordType.SUBMISSION));
			pstmt.setInt(2, submissionID);
			pstmt.executeUpdate();
		} finally {
			closeConnection(conn);
			close(pstmt);
		}
	}

	public void updateSubmission(OntologySubmission submission)
			throws SQLException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "update ontology_submissions set ontologyID = ?, "
					+ "superClassID = ?, " + "definition = ?,"
					+ "synonyms = ?, " + "source = ?, sampleSentence = ? "
					+ "where ID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, submission.getOntologyID());
			pstmt.setString(2, submission.getSuperClass());
			pstmt.setString(3, submission.getDefinition());
			pstmt.setString(4, submission.getSynonyms());
			pstmt.setString(5, submission.getSource());
			pstmt.setString(6, submission.getSampleSentence());
			pstmt.setInt(7, Integer.parseInt(submission.getSubmissionID()));

			pstmt.executeUpdate();
		} finally {
			closeConnection(conn);
			close(pstmt);
		}
	}

	public void addSubmission(OntologySubmission submission, int uploadID)
			throws Exception {
		PreparedStatement pstmt = null;
		int submissionID = -1;
		Connection conn = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			String sql = "insert into ontology_submissions"
					+ "(term, category, ontologyID, submittedBy, localID, "
					+ "tmpID, permanentID, superClassID, synonyms, definition, "
					+ "source, sampleSentence) " + "values "
					+ "(?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "? ,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, submission.getTerm());
			pstmt.setString(2, submission.getCategory());
			pstmt.setString(3, submission.getOntologyID());
			pstmt.setString(4, submission.getSubmittedBy());
			pstmt.setString(5, submission.getLocalID());

			pstmt.setString(6, submission.getTmpID());
			pstmt.setString(7, "");
			pstmt.setString(8, submission.getSuperClass());
			pstmt.setString(9, submission.getSynonyms());
			pstmt.setString(10, submission.getDefinition());

			pstmt.setString(11, submission.getSource());
			pstmt.setString(12, submission.getSampleSentence());
			pstmt.executeUpdate();

			sql = "SELECT LAST_INSERT_ID()";
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				submissionID = rset.getInt(1);
			} else {
				throw new Exception(
						"Failed to insert submission record to database. ");
			}

		} finally {
			closeConnection(conn);
			close(pstmt);
		}

		// after submissioin, set the new one to be the default mapping
		if (submissionID > 0) {
			updateSelectedOntologyRecord(uploadID, submission.getTerm(),
					submission.getCategory(), OntologyRecordType.SUBMISSION,
					submissionID);
		}
	}
}
