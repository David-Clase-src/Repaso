package repaso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static DatabaseConnection instance;
	private static final String URL =  "jdbc:mysql://localhost:3306/sakila";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "mysql";
	
	private Connection connection;
	
	private DatabaseConnection() {
			try {
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static DatabaseConnection getInstance() {
		if(instance==null)
			instance = new DatabaseConnection();
		
		return instance;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
}
