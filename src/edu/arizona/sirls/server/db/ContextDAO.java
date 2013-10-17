package edu.arizona.sirls.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.arizona.sirls.shared.beans.term_info.TermContext;

public class ContextDAO extends AbstractDAO {

	private static ContextDAO instance;

	public static ContextDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new ContextDAO();
		}
		return instance;
	}

	public ContextDAO() throws Exception {
		super();
	}

	public ArrayList<TermContext> getTermContext(String term, int uploadID)
			throws SQLException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rset = null;
		ArrayList<TermContext> contexts = new ArrayList<TermContext>();
		try {
			conn = getConnection();

			String sql = "select source, sentence from sentences "
					+ "where uploadID = ? and sentence rlike '^(.*\\s)?" + term
					+ "(\\s.*)?$' limit 10";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uploadID);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				TermContext context = new TermContext(rset.getString(1),
						rset.getString(2));
				contexts.add(context);
			}
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
		}

		return contexts;
	}

}
