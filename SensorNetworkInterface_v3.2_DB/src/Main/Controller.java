/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import ActivitiesUtilities.ActivitiesXMLModel;
import ActivitiesUtilities.SensorProperties;
import ActivityRecognition.ActivityRecognition;
import DataBase.DataBase;
import DataBase.LocationsDB;
import DataBase.SensorsDB;
import DataBaseModel.Sensors;
import View.MapWindows;
import ActivitiesUtilities.ActivitiesXMLReader;
import DataBase.SensorLogDB;
import DataBase.SessionControlDB;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Ismael
 */
public class Controller {
    
    //Application view
    private static MapWindows _view;
    
    //Activities Data
    private static ActivitiesXMLModel _activities = new ActivitiesXMLModel();
    
    //Activity recognition module
    private static ActivityRecognition _actRecognition;
    
    //Data checker
    private static DataBaseChecker _dbChecker;
    
    // Configuration file
    private static Properties _configFile;

    private static String _logsFolder;

    //General variables
    private static String _userName;
    private static Date _time;
    private static Boolean _startRecording = false;
    private static String _logsData = "";
    private static Map<Date, Set<String>> _latestActivities = new TreeMap<Date, Set<String>>();
    private static String _numberFormat = "";

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
    public Controller() throws IOException{

            // Configuration file
            _configFile = new Properties();
            _configFile.load(new FileInputStream("config.properties"));

            //Initialize general variables
            _logsFolder = _configFile.getProperty("LOGS_FOLDER");
            _numberFormat = _configFile.getProperty("NUMBER_FORMAT");

            //DB connection ---------------------------------------------------------
            DataBase.connect();
            _userName = SessionControlDB.getUserSession();

            //Activity Reader --------------------------------------------------------
            ActivitiesXMLReader _activitiesReader = new ActivitiesXMLReader();
            _activitiesReader.readXML(_configFile.getProperty("ACTIVITIES_XML_FILE"), _activities);

            //View creation ---------------------------------------------------------------------
            /** Send the sensors data to the view in order to create a table with all the sensors */ 
            _view = new MapWindows(this,_configFile.getProperty("MODULE_COLUMN"), _configFile.getProperty("CHANNEL_COLUMN"),
                    _configFile.getProperty("ID_COLUMN"), _configFile.getProperty("NAME_COLUMN"),
                    _configFile.getProperty("TYPE_COLUMN"), _configFile.getProperty("VALUE_COLUMN"),
                    _configFile.getProperty("STATUS_COLUMN"), _userName);
            /** The view create the sensor table based on the read data */
            _view.initializeTable(LocationsDB.getValidLocations(), SensorsDB.getSensors());

            // Activity Recognition Thread ---------------------------------------------------------
            _actRecognition = new ActivityRecognition(this, _activities, _configFile.getProperty("NUMBER_FORMAT"));

            //DB Checker  Thread ---------------------------------------------------------
            _dbChecker = new DataBaseChecker(this);
            _dbChecker.start();

    }
    

    /**
     * Set the variable _startRecording to true. Set the current date
     */
    public void startRecording(){
        _startRecording = true;
        _time = new Date();
        System.out.println("Start Recording");
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
     * Show the current activities log
     * @param data Data to be shown
     */
    public void writeLatestActivities(Date date, String activity){
        if(_latestActivities.containsKey(date)){
            _latestActivities.get(date).add(activity);
        }else{
            Set<String> activities = new HashSet<String>();
            activities.add(activity);
            _latestActivities.put(date, activities);
        }
        //Print the activities
        String latestActivities = "";
        for(Map.Entry<Date, Set<String>> entry: _latestActivities.entrySet()){
            for(String activityName: entry.getValue()){
                latestActivities += CommonVar.dateFormatTime.format(entry.getKey().getTime()) + " ==> " +
                        activityName + "\n";
            }
        }
       _view.writeLatestActivities(latestActivities);
   }

    /**
     * Set the variable _startRecording to false
     */
    public void stopRecording(){

        _startRecording = false;
        System.out.println("Stop Recording");

        //Logs (Activities)
        LogsWriter.writeSensorLogsToFile(_userName,_time,_logsFolder + "ActivitiesLogs_SQL_" + _userName + ".csv",
                SensorLogDB.getUserActivitiesLog(_time));
        
        //Logs (Sensors)
        LogsWriter.writeSensorLogsToFile(_userName,_time,_logsFolder + "SensorLogs_SQL_" + _userName + ".csv",
                SensorLogDB.getSensorLog(_time));

        //Internal application logs
        LogsWriter.writeLogsToFile(_userName, _time, _logsFolder + "Logs_Sensors_" + _userName + ".txt", _logsData);
        
    }

    /**
     *
     * @return
     */
    public Boolean getStatusRecording(){
        return _startRecording;
    }
    
    /**
    * Set the value of the sensor
    * @param room Room (tab) in which the sensor can be found
    * @param sensorDetails Sensor's details
    * @return True If the status has changed
    */
    public void updateSensorInterface(String room, Sensors sensors, String status, Boolean update){
       _view.updateSensorData(room, sensors, status, update);
    }
   
     /**
     * Send the sensors changes to the activity recognition module
     * @param sensor Sensor name
     * @param time Sensor time
     * @param status Sensor status
     */
    public void recongnition(String sensor, Date time, String status){
        if(_startRecording){
            //Activated sensors used to recognize the performed activity
             _actRecognition.insertSensor(sensor, new SensorProperties(time, status, false, 0.0));
             _actRecognition.run();
        }
    }
  
   
     /**
     * Close the database connections
     */
    public void close(){
        try {
            _dbChecker.wait(1000);
            DataBase.close();         
        } catch (InterruptedException ex) {
            System.out.println("Error while closing the system");
        }finally{
            System.exit(0);
        }
    }

}
