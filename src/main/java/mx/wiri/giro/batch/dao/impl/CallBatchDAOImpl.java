package mx.wiri.giro.batch.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import javax.sql.DataSource;
import mx.wiri.giro.batch.dao.ICallBatchDAO;

public class CallBatchDAOImpl implements ICallBatchDAO
{
	private DataSource dataSource;

	Properties prop = new Properties();
	InputStream input = null;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/*JOE 2018-03-22
	* Permite crear la sesion de bd del proceso batch*/
	public String preHandler(Integer branchid, Integer usertype, String device, String browser, String ip,
							 Timestamp date, Integer transactionid, String request, Integer staffid) throws IOException {

		input = CallBatchDAOImpl.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(input);

		String sql = prop.getProperty("mx.wiri.batch.fn.prehandler");

		Connection conn = null;

		try {
			conn = dataSource.getConnection();

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, branchid);
			ps.setInt(2, usertype);
			ps.setString(3, device);
			ps.setString(4, browser);
			ps.setString(5, ip);
			ps.setTimestamp(6, date);
			ps.setInt(7, transactionid);
			ps.setString(8, request);
			ps.setInt(9, staffid);

			String callId = "";
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				callId = rs.getString(1);
			}

			rs.close();
			ps.close();
			return callId;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}


	/*JOE 2018-03-22
	 * Permite recuperar la sesion de bd del proceso batch*/
	public String getSessionBdByCallId(String call_id) throws IOException {

		input = CallBatchDAOImpl.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(input);

		String sql = prop.getProperty("mx.wiri.batch.fn.getSessionByCall");

		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, call_id);

			String sessId = "";
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				sessId = rs.getString(1);
			}

			rs.close();
			ps.close();
			return sessId;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}


	/*JOE 2018-03-22
	 * Permite importar los créditos de HES a ADA*/
	public String importCreditsBatch(String sessionBd, String callId, Integer productId) throws IOException {

		input = CallBatchDAOImpl.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(input);

		String sql = prop.getProperty("mx.wiri.batch.fn.core");

		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sessionBd);
			ps.setString(2, callId);
			ps.setInt(3, productId);

			String res = "";
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				res = rs.getString(1);
			}

			rs.close();
			ps.close();
			return res;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}


	/*JOE 2018-03-22
	 * Permite registrar la respuesta del proceso batch*/
	public String afterComplete(String call_id, String responseType, String responseDetail, Timestamp date) throws IOException {

		input = CallBatchDAOImpl.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(input);

		String sql = prop.getProperty("mx.wiri.batch.fn.aftercomplete");

		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, call_id);
			ps.setString(2, responseType);
			ps.setString(3, responseDetail);
			ps.setTimestamp(4, date);

			String res = "";
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				res = rs.getString(1);
			}

			rs.close();
			ps.close();
			return res;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
}
