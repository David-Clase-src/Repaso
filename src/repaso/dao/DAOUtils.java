package repaso.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Utilities for better mapping ResultSets to objects
 */
public final class DAOUtils {
	public static <T> List<T> mapResultSet(ResultSet rs, Function<ResultSet, T> mapper) throws SQLException {
		List<T> list = new ArrayList<T>();
		
		while(rs.next()) {
			T obj = mapper.apply(rs);
			if(obj == null) break;
			else list.add(obj);
		}
		
		return list;
	}
}
