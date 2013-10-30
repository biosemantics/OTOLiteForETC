package edu.arizona.sirls.server.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

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
			throws SQLException, IOException {

		// get source files path
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		properties.load(loader.getResourceAsStream("config.properties"));
		String srcFilePath = properties.getProperty("src_file_dir");
		srcFilePath = srcFilePath + Integer.toString(uploadID) + "/";

		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rset = null;
		ArrayList<TermContext> contexts = new ArrayList<TermContext>();
		try {
			conn = getConnection();

			// notice: mysql regex space is ' ', not \\s
			String sql = "select source, sentence from sentences "
					+ "where uploadID = ? and sentence rlike '^(.* )?" + term
					+ "( .*)?$' limit 10";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uploadID);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				String srcFileName = rset.getString(1);
				TermContext context = new TermContext(srcFileName,
						rset.getString(2));

				// test if src file accessable
				File test = new File(srcFilePath + srcFileName);
				if (test.exists()) {
					context.setSrcFileAccessable(true);
					context.setSrcFilePath(srcFilePath + srcFileName);
				} else {
					context.setSrcFileAccessable(false);
				}

				contexts.add(context);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeConnection(conn);
			close(pstmt);
			close(rset);
		}

		return contexts;
	}

}
