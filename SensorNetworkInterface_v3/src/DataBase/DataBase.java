
package DataBase;

import java.sql.*;

/**
 * This class manages the SQL queries
 * @author Ismael Duque
 */
public class DataBase {
    
    // declare a connection by using Connection interface 
    Connection connection = null;

    // declare object of Statement interface that uses for executing sql statements
    Statement statement = null;
    
    // declare a resultset that uses as a table for output data from the table.
    ResultSet rs = null;
    int updateQuery = 0;

    /**
     * Class Constructor
     */
    public DataBase() {}

   /**
    * To connect with the data base
    * @param db_connect_string Database name
    * @param db_userid Database user identifier
    * @param db_password Database user password
    * @return Return a Connection object
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws IllegalAccessException
    * @throws SQLException
    */
    public Connection dbConnect(String db_connect_string,
      String db_userid, String db_password) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, SQLException
    {

                // Load JBBC driver "com.mysql.jdbc.Driver".
                Class.forName("com.mysql.jdbc.Driver").newInstance();

                /* Create a connection by using getConnection()
                   method that takes parameters of string type
                   connection url, user name and password to
                   connect to database. */
                connection = DriverManager.getConnection(
                  db_connect_string, db_userid, db_password);

                /* createStatement() is used for create
                   statement object that is used for sending sql
                   statements to the specified database. */
                statement = connection.createStatement();

                /* Log to be shown in the console */
                System.out.println("Connected to " + db_connect_string);

                return connection;

    }

    /**
     * Create and execute an Insert SQL query on the database
     * @param query String that contains the query to be executed
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
    public int sqlQueryInsert(String query) throws SQLException{

        return statement.executeUpdate(query);
    }

    /**
     * Execute a consult upon the database
     * @param query String that contains the query to be executed
     * @return ResultSet object that contains the data produced by the given query
     * @throws SQLException
     */
    public ResultSet sqlQuerySelect(String query) throws SQLException{
        
        return statement.executeQuery(query);

    }

    /**
     * Close the statement and connection created before
     * @throws SQLException
     */
    public void close() throws SQLException{
          statement.close();
          connection.close();
    }
    
    
}
