/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import ActivitiesModel.ActivitiesXMLModel;
import ActivitiesModel.SensorProperties;
import ActivityRecognition.ActivityRecognition;
import DataBase.DataBase;
import LogsWriter.LogsWriter;
import SensorsModel.SensorsXMLModel;
import SensorsModules.GeoSystemHandler;
import View.MapWindows;
import XMLReaders.SensorsXMLReader;
import SensorsModules.ZigBeeHandler;
import XMLReaders.ActivitiesXMLReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 *
 * @author Ismael
 */
public class Controller {
    
    //Application view
    private final MapWindows _view;
    
    //Sensor Data
    private final SensorsXMLModel _data;
    
    //Activities Data
    private final ActivitiesXMLModel _activities;
    
    //Activity recognition module
    private final ActivityRecognition _actRecognition;
    
    //UDP data receiver
    private ZigBeeHandler _zigBee;
    //LogDB
    private final DataBase _logDB = new DataBase();

    //GeoSystem data reciver
    private GeoSystemHandler _geoSystem;
    //GeoSystem Database
    private final DataBase _geoSystemDB = new DataBase();
    
    // Configuration file
    private final Properties _configFile;

    private final String _logsFolder;

    //General variables
    private static String _userName;
    private static Time _time;
    private static Boolean _startRecording = false;
    private static String _logsData = "";
    /**
     * Obtain the data from the configuration files and create the necessary thread
     * to show the sensor's values through the interface
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public Controller() throws FileNotFoundException, IOException, ClassNotFoundException, 
            InstantiationException, IllegalAccessException, SQLException{

            // Configuration file
            _configFile = new Properties();
            _configFile.load(new FileInputStream("config.properties"));

            //Logs Folder
            _logsFolder = _configFile.getProperty("LOGS_FOLDER");

            //Data reader (Sensors) ---------------------------------------------------------
            SensorsXMLReader _sensorsReader = new SensorsXMLReader();
            _data = _sensorsReader.readXML(_configFile.getProperty("SENSORS_XML_FILE"));

            //Activity Reader --------------------------------------------------------
            ActivitiesXMLReader _activitiesReader = new ActivitiesXMLReader();
            _activitiesReader.readXML(_configFile.getProperty("ACTIVITIES_XML_FILE"));
            _activities =  _activitiesReader.getActivitiesData();
            
            //Set the variables User and Date
            _userName = _activitiesReader.getUserName();
            _time = new Time(System.currentTimeMillis());
            
            //View creation ---------------------------------------------------------------------
            /** Send the sensors data to the view in order to create a table with all the sensors */ 
            _view = new MapWindows(this);
            _view.setData(_configFile.getProperty("MODULE_COLUMN"), _configFile.getProperty("CHANNEL_COLUMN"),
                    _configFile.getProperty("ID_COLUMN"), _configFile.getProperty("NAME_COLUMN"),
                    _configFile.getProperty("TYPE_COLUMN"), _configFile.getProperty("VALUE_COLUMN"),
                    _configFile.getProperty("STATUS_COLUMN"));
            /** The view create the sensor table based on the read data */ 
            _view.initializeTable(_data);


            // Activity Recognition Thread ---------------------------------------------------------
            _actRecognition = new ActivityRecognition(this, _activities, _configFile.getProperty("NUMBER_FORMAT"));
            

            //ZigBee Module Thread ---------------------------------------------------------
            if (_configFile.getProperty("ZIGBEE_AVAILABLE").equals("true")){
                    
                //ZigBee DB connection
                _logDB.dbConnect("jdbc:mysql://" +
                    _configFile.getProperty("MYSQL_LOG_SERVER") + "/" + _configFile.getProperty("MYSQL_LOG_DB"),
                    _configFile.getProperty("MYSQL_LOG_USER"), _configFile.getProperty("MYSQL_LOG_PASSWORD"));

                //UDP data receiver (Thread) ----------------------------------------------
                _zigBee = new ZigBeeHandler(this, _data, Integer.parseInt(_configFile.getProperty("PORT")),
                    _configFile.getProperty("MYSQL_LOG_TABLE"), _configFile.getProperty("MYSQL_LOG_COLUMNS"),
                    _configFile.getProperty("NUMBER_FORMAT"));
            }

            //GeoSystem Module Thread  -------------------------------------------------------
            if (_configFile.getProperty("GEO_AVAILABLE").equals("true")){
                //GeoSystem DB connection
                _geoSystemDB.dbConnect("jdbc:mysql://" +
                    _configFile.getProperty("MYSQL_GEO_SERVER") + "/" + _configFile.getProperty("MYSQL_GEO_DB"),
                    _configFile.getProperty("MYSQL_GEO_USER"), _configFile.getProperty("MYSQL_GEO_PASSWORD"));
                
                //GeoSystem data receiver (Thread) --------------------------------------------------
                _geoSystem = new GeoSystemHandler(this, _data, _configFile.getProperty("MYSQL_GEO_QUERY"),
                    _configFile.getProperty("MYSQL_LOG_TABLE"),_configFile.getProperty("MYSQL_LOG_COLUMNS"));
            }

    }
    
   /**
     * Return the user name
     * @return String
     */
    public void setUserName(String userName){
        _userName = userName;
    }
    
    /**
     * Return the current date
     * @return Date - Format "dd.MM.yy"
     */
    public String getFormattedDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy hh:mm:sss");
        return dateFormat.format(new Date(_time.getTime()));
    }

    /**
     * Set the variable _startRecording to true. Set the current date
     */
    public void startRecording(){
        _startRecording = true;
        _time = new Time(System.currentTimeMillis());
        System.out.println("Start Recording");
    }

    /**
     * Set the variable _startRecording to false
     */
    public void stopRecording(){
        try {
            _startRecording = false;
            System.out.println("Stop Recording");

            ResultSet rs = _logDB.sqlQuerySelect(
                    "SELECT * FROM logging WHERE" + " timestamp>" + _time.getTime());

            LogsWriter.writeSQLLogsFile(_userName,_time,_logsFolder + "Logs_SQL_" + _userName + ".csv",
                    rs,_configFile.getProperty("MYSQL_LOG_COLUMNS"));

            rs.close();
            
            LogsWriter.writeLogsFile(_userName, _time, _logsFolder + "Logs_Sensors_" + _userName + ".txt", _logsData);
            

        } catch (SQLException ex) {
            System.out.println("SQL Exception. Stop Recording Method");
        }
        
    }

    /**
     *
     * @return
     */
    public Boolean getStatusRecording(){
        return _startRecording;
    }

    /**
     * Show the previous activities log
     * @param data Data to be shown
     */
    public void writeLogsPreviousActivities(String data){
        _logsData += data;
        _view.writeLogsPreviousActivities(_logsData);
   }

    /**
     * Show the current activities log
     * @param data Data to be shown
     */
    public void writeLogsCurrentActivities(String data){
       _view.writeLogsCurrentActivities(data);
   }
    
    /**
    * Set the value of the ZigBee sensors
    * @param tab Room (tab) in which the sensor can be found
    * @param idSensor Sensor's id that we are looking for
    * @param value The new value sent by the sensor
    * @param status The sensor's status depending on the value
    * @return True If the status has changed
    */
   public Boolean setValueCell(String tab, String idSensor, String value, String status){
       if((_view.setValueCell(tab, idSensor, value, status)) && (_startRecording))
           return true;
       else return false;
   }

       /**
     * Send the sensors changes to the activity recognition module
     * @param sensor Sensor name
     * @param time Sensor time
     * @param status Sensor status
     */
    public void recongintion(String sensor, Time time, String status){
        if(_startRecording)
            //Activated sensors used to recognize the performed activity
             _actRecognition.insertSensor(sensor, new SensorProperties(time, status, false, 0.0));

    }
   

   /**
     * Create and execute an Insert SQL query on the database
     * @param query String that contains the query to be executed
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
   public void sqlQueryInsert(String query) throws SQLException{
       _logDB.sqlQueryInsert(query);
   }
   
   /**
     * Execute a consult upon the database
     * @param query String that contains the query to be executed
     * @return ResultSet object that contains the data produced by the given query
     * @throws SQLException
     */
   public ResultSet sqlQuerySelectGeoSystem(String query) throws SQLException{
       return _geoSystemDB.sqlQuerySelect(query);
   }
   
   
    /**
     * Close the current threads
     */
    public void closeThreadsAndDBs(){
        if (_configFile.getProperty("GEO_AVAILABLE").equals("true")) _geoSystem.close();

        if (_configFile.getProperty("ZIGBEE_AVAILABLE").equals("true")) _zigBee.close();
        
        closeDBs(); //Close the DB connections
    }
    
     /**
     * Close the databases
     */
    public void closeDBs(){
        if (_configFile.getProperty("GEO_AVAILABLE").equals("true")){
            try {
                _geoSystemDB.close(); System.out.println("GeoSystem DB closed");
            } catch (SQLException ex) {
                System.out.println("GeoSystem DB closure error");
            }
        }
        
        if (_configFile.getProperty("ZIGBEE_AVAILABLE").equals("true")){
            try {
                _logDB.close(); System.out.println("Log DB closed");
            } catch (SQLException ex) {
                System.out.println("Log DB closure error");
            }
        }

        System.exit(0);
    }

}
