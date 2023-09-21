package test.org.fugerit.java.query.export.tool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBase {

	protected final static Logger logger = LoggerFactory.getLogger( TestBase.class );
	
	 /**
     * Database initialization for testing i.e.
     * <ul>
     * <li>Creating Table</li>
     * <li>Inserting record</li>
     * </ul>
     * 
     * @throws SQLException
     */
    protected static void initDatabase() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.execute("CREATE TABLE test_export (id INT NOT NULL, name VARCHAR(50) NOT NULL,"
                    + "email VARCHAR(50) NOT NULL, PRIMARY KEY (id))");
            connection.commit();
            statement.executeUpdate("INSERT INTO test_export VALUES (1001,'FieldA1', 'FieldB1')");
            statement.executeUpdate("INSERT INTO test_export VALUES (1002,'FieldA2', 'FieldB2')");
            statement.executeUpdate("INSERT INTO test_export VALUES (1003,'FieldA3', 'FieldB3')");
            connection.commit();
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
	
    @AfterClass
    public static void destroy() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.executeUpdate("DROP TABLE test_export");
            connection.commit();
        }
    }    
	
}
