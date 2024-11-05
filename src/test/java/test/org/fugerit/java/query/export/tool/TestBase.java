package test.org.fugerit.java.query.export.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBase {

	protected final static Logger logger = LoggerFactory.getLogger( TestBase.class );
	
	private static boolean init = false;
	
	 /**
     * Database initialization for testing i.e.
     * <ul>
     * <li>Creating Table</li>
     * <li>Inserting record</li>
     * </ul>
     * 
     * @throws SQLException
     */
	@BeforeClass
    public synchronized static void initDatabase() throws SQLException {
		if ( !init ) {
	        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
	            statement.execute("CREATE TABLE test_export (id INT NOT NULL, name VARCHAR(50) NOT NULL,"
	                    + "email VARCHAR(50) NOT NULL, testdate DATE, PRIMARY KEY (id))");
	            connection.commit();
	            statement.executeUpdate("INSERT INTO test_export VALUES (1001,'FieldA1', 'FieldB1', '2024-11-02')");
	            statement.executeUpdate("INSERT INTO test_export VALUES (1002,'FieldA2', 'FieldB2', '2024-11-03')");
	            statement.executeUpdate("INSERT INTO test_export VALUES (1003,'FieldA3', 'FieldB3', '2024-11-04')");
	            connection.commit();
	            init = true;
	        }			
		}
    }
 
    /**
     * Create a connection
     * 
     * @return connection object
     * @throws SQLException
     */
    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:base_db", "testdb", "testdb");
    }
	
}
