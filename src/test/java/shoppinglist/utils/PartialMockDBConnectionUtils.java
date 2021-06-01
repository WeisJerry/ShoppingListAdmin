package shoppinglist.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Allow overriding certain internal properties and methods for 
 * the purpose of unit testing.
 * @author weis_
 *
 */
public class PartialMockDBConnectionUtils extends DBConnectionUtils {

	protected String dbError = "";
	protected boolean forceException = false;
	
	protected List<String> queries = new ArrayList<String>();

	
	public String getDbError() {
		return dbError;
	}
	
	public void forceException(boolean force) {
		forceException = force;
	}
	
	public int getQueryCount() {
		return queries.size();
	}
	
	public String getQueryString(int index) {
		String ret = "";
		if (index >=0 && index<queries.size()) {
			ret = queries.get(index);
		}
		return ret;
	}
	
	public List<String> getQueries() {
		return queries;
	}
	
	public void clearQueries() {
		queries.clear();
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return new MockConnection();
	}
	
	@Override
	public void showDBError(String errMsg) {
		dbError = errMsg;
	}
	
	@Override
	public ResultSet executeQuery(String query) throws SQLException {
		if (forceException) {
			throw new SQLException();
		}
		queries.add(query);
		return super.executeQuery(query);
	}
	
	@Override
	public void executeUpdate(String query) throws SQLException {
		if (forceException) {
			throw new SQLException();
		}
		queries.add(query);
	}
}
