package edu.arizona.sirls.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.arizona.sirls.shared.beans.hierarchy.Structure;
import edu.arizona.sirls.shared.beans.hierarchy.StructureNodeData;

public class HierarchyDAO extends AbstractDAO {
	public static HierarchyDAO instance;

	public static HierarchyDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new HierarchyDAO();
		}

		return instance;
	}

	public HierarchyDAO() throws Exception {
		super();
	}

	/**
	 * recursively get node and its children
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	private StructureNodeData getNode(int id, int uploadID) throws SQLException {
		StructureNodeData node = new StructureNodeData();
		Connection conn = null;
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rset = null, rset2 = null;
		try {
			conn = getConnection();
			String sql = "select a.termID, a.pID, b.term from "
					+ "(select termID, pID from trees where id = ?) a  "
					+ "left join structures b  " + "on a.termID = b.ID";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				node.setTermID(rset.getString("termID"));
				node.setTermName(rset.getString("term"));
				node.setpID(rset.getString("pID"));

				ArrayList<StructureNodeData> children = new ArrayList<StructureNodeData>();
				// get children
				sql = "select ID from trees where uploadID = ? and pID = ?";
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setInt(1, uploadID);
				pstmt2.setInt(2, id);
				rset2 = pstmt2.executeQuery();
				while (rset2.next()) {
					children.add(getNode(rset2.getInt(1), uploadID));
				}
				node.setChildren(children);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rset);
			close(pstmt);
			closeConnection(conn);
		}
		return node;
	}

	/**
	 * recursively save node and its children
	 * 
	 * @param uploadId
	 * @param conn
	 * @param nodeData
	 * @throws SQLException
	 */
	public void saveNode(String uploadID, Connection conn,
			StructureNodeData nodeData, int pID) throws SQLException {
		Statement stmt = null;
		ResultSet rset = null;
		try {
			int generatedID = 0;
			stmt = conn.createStatement();
			String sql = "insert into trees (uploadId, termID, pID) values "
					+ "(" + uploadID + ", " + nodeData.getTermID() + ", "
					+ Integer.toString(pID) + ")";
			stmt.executeUpdate(sql);
			sql = "SELECT LAST_INSERT_ID()";
			rset = stmt.executeQuery(sql);
			if (rset.next()) {
				generatedID = rset.getInt(1);

				if (nodeData.getChildren() != null
						&& nodeData.getChildren().size() > 0) {
					for (StructureNodeData child : nodeData.getChildren()) {
						saveNode(uploadID, conn, child, generatedID);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rset);
			close(stmt);
		}
	}

	/**
	 * use add structure to the upload
	 * 
	 * @param uploadID
	 * @param termName
	 * @return
	 * @throws SQLException
	 */
	public Structure addStructure(String uploadID, String termName)
			throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		Structure structure = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);

			// delete all records of existing tree
			String sql = "insert into structures (uploadID, term, userCreated) "
					+ "values (" + uploadID + ", '" + termName + "', true)";
			stmt.executeUpdate(sql);

			sql = "SELECT LAST_INSERT_ID()";
			rset = stmt.executeQuery(sql);
			if (rset.next()) {
				structure = new Structure(Integer.toString(rset.getInt(1)),
						"0", termName);
			}

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();

			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException exe) {
					exe.printStackTrace();
					throw exe;
				}
			}
			throw e;
		} finally {
			close(rset);
			close(stmt);
			closeConnection(conn);
		}

		return structure;
	}

	/**
	 * save the entire tree
	 * 
	 * @param uploadID
	 * @param nodeDataList
	 * @throws SQLException
	 */
	public void saveTree(String uploadID,
			ArrayList<StructureNodeData> nodeDataList) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);

			// delete all records of existing tree
			String sql = "delete from trees where uploadID = " + uploadID;
			stmt.executeUpdate(sql);

			// save node list
			for (StructureNodeData nodeData : nodeDataList) {
				saveNode(uploadID, conn, nodeData, 0);
			}

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();

			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException exe) {
					exe.printStackTrace();
					throw exe;
				}
			}
			throw e;
		} finally {
			close(rset);
			close(stmt);
			closeConnection(conn);
		}
	}

	/**
	 * get top level nodes list of the tree
	 * 
	 * @param uploadID
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<StructureNodeData> getNodeList(int uploadID)
			throws SQLException {
		ArrayList<StructureNodeData> nodes = new ArrayList<StructureNodeData>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			String sql = "select ID from trees where uploadID = ? and (pID is null or pId = 0)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uploadID);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				nodes.add(getNode(rset.getInt("ID"), uploadID));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rset);
			close(pstmt);
			closeConnection(conn);
		}
		return nodes;
	}

	/**
	 * get the structures list for the left side of hierarchy page
	 * 
	 * only display those terms not in the tree yet
	 * 
	 * @param uploadID
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Structure> getStructuresList(int uploadID)
			throws SQLException {
		ArrayList<Structure> structures = new ArrayList<Structure>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection();
			String sql = "select ID, term from structures "
					+ "where uploadID = ? and ID not in "
					+ "(select distinct termID from trees where uploadID = ?) "
					+ "order by term";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uploadID);
			pstmt.setInt(2, uploadID);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				structures.add(new Structure(rset.getString("ID"), "0", rset
						.getString("term")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rset);
			close(pstmt);
			closeConnection(conn);
		}
		return structures;
	}

}
