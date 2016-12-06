
package DataBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class manages the SQL queries
 * @author Ismael Duque
 */
public class DataBase {

    /*
     * singleton.
     */
    private static DataBase __singleton = new DataBase ();

    // declare a connection by using Connection interface 
    private static Connection _connection = null;

    // declare object of Statement interface that uses for executing sql statements
    private static Statement _statement = null;
    
    // declare a resultset that uses as a table for output data from the table.
    private static ResultSet _rs = null;
    
    
    /**
     * Nombre de la Base de datos a gestionar
     */
    private static String _user, _password, _connectionURL, _databaseName;
    /*
     * Driver para conectar con la Base de datos
     */
    private String _driverDB = "com.mysql.jdbc.Driver";


    /**
     * Class Constructor
     */
    private DataBase() {
        
        Properties _configFile = new Properties();

        try {
            _configFile.load(new FileInputStream("config.properties"));
        } catch (IOException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        _databaseName = _configFile.getProperty("MYSQL_DB");
        _connectionURL = "jdbc:mysql://" +
                 _configFile.getProperty("MYSQL_SERVER") + "/" + _databaseName;
        _user = _configFile.getProperty("MYSQL_USER");
        _password = _configFile.getProperty("MYSQL_PASSWORD");
    
    }

   /**
    * To connect with the data base
    */
    public static void connect(){
        
        try {
            // Load JBBC driver "com.mysql.jdbc.Driver".
            Class.forName(__singleton._driverDB).newInstance();
            /* Create a connection by using getConnection()
            method that takes parameters of string type
            connection url, user name and password to
            connect to database. */
            _connection = DriverManager.getConnection(__singleton._connectionURL, __singleton._user, __singleton._password);
            /* createStatement() is used for create
            statement object that is used for sending sql
            statements to the specified database. */
            _statement = _connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //_statement.execute("use "+ __singleton._databaseName +";");
            /* Log to be shown in the console */
            System.out.println("Connected to " + __singleton._connectionURL);
            
        
        } catch (SQLException ex) {
            System.out.println( "SQLException during database connection");
            System.exit(0);
        } catch (InstantiationException ex) {
            System.out.println( "InstantiationException during database connection");
            System.exit(0);
        } catch (IllegalAccessException ex) {
            System.out.println( "IllegalAccessException during database connection");
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            System.out.println( "ClassNotFoundException during database connection");
            System.exit(0);
        }

    }

    /**
     * Create and execute an Insert SQL query on the database
     * @param query String that contains the query to be executed
     * @throws SQLException
     */
    public static void sqlQueryInsert(String query){
        try {
            _statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Execute a consult upon the database
     * @param query String that contains the query to be executed
     * @return ResultSet object that contains the data produced by the given query
     * @throws SQLException
     */
    public static ResultSet sqlQuerySelect(String query){
        try {
            _rs = _statement.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return _rs;

    }

    /**
     * Method to navigate through the data retrieved from the database.
     * @return <tt>true</tt> when it obtaions new value,
     * <tt>false</tt> in other cases.
     */
    public static boolean next(){
        Boolean value = false;
        try {
            value = _rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    /**
     * Method to look up an integer value.
     * @param column column number
     * @return value.
     */
    public static int getInt(int column){
        int value = -1;
        try {
            value = _rs.getInt(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	return value;
    }

    /**
     * Method to look up a float value.
     * @param column column number
     * @return value.
     */
    public static float getFloat(int column){
        Float value = -999.0f;
        try {
            //value = Float.valueOf(_rs.getString(column));
            value = _rs.getFloat(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    /**
     * Method to look up a string value.
     * @param column column number
     * @return value.
     */
    public static String getString(int column){
        String value = "";
        try {
            value = _rs.getString(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	return value;
    }

    /**
     * Method to look up a Date value.
     * @param column column number
     * @return value.
     */
    public static Date getDate(int column){
        Date value = new Date(System.currentTimeMillis());
        try {
            value = _rs.getDate(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    	return value;

    }

    /**
     * Method to look up a Date value.
     * @param column column number
     * @return value.
     */
    public static boolean getBoolean(int column){
        Boolean value = false;
        try {
            value = _rs.getBoolean(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return value;
    }

     /**
     * Method to look up a Timestamp value.
     * @param column column number
     * @return value.
     */
    public static Timestamp getTimestamp(int column) {
	Timestamp salida = null;

        try {
            salida = _rs.getTimestamp(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	return salida;
    }
    
    /**
     * Return the database name
     * @return String
     */
    public static String getDataBaseName(){
        return _databaseName;
    }

    /**
     * Close the statement and connection created before
     * @throws SQLException
     */
    public static void closeResultSet(){
        if(_rs != null) try {
            _rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Close the statement and connection created before
     * @throws SQLException
     */
    public static void close(){
        try {
            _statement.close();
            _connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();;
        }
    }

}
