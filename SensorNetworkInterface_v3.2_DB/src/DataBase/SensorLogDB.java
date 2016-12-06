/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import Main.CommonVar;
import DataBaseModel.SensorLog;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author Ismael
 */
public class SensorLogDB {

    /**
     * Singleton.
     */
    private static SensorLogDB __singleton = new SensorLogDB();

    /*
     * Table columns
     */
    private static final int TIMESTAMP=1;
    private static final int SENSORID=2;
    private static final int ROOM=3;
    private static final int CHANNEL=4;
    private static final int VALUE=5;
    private static final int STATUS=6;

    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private SensorLogDB() {
     }

     /**
      * Method to retrieve a sensor status by its Id and timestamp
      * @param sensorId Sensor ID
      * @param timestamp Time for the last sensor update
      * @return SensorLog A sensor with all the information retrieved from the DB
      */
     public static SensorLog getSensorStatus(Integer sensorId, Date timestamp){

        SensorLog sensorLog = null;

        if (sensorId != null && timestamp != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT SensorLog.* FROM "
                    + DataBase.getDataBaseName() + ".Sensors, " + DataBase.getDataBaseName() + ".SensorLog "
                    + "WHERE SensorLog.timestamp = '" + CommonVar.dateFormatDB.format(timestamp) + "' "
                    + " AND SensorLog.sensorId = " + sensorId
                    + " AND SensorLog.sensorId = Sensors.sensorId");

            // Create a sensor with the query results
            if (DataBase.next())

                sensorLog = new SensorLog(DataBase.getTimestamp(TIMESTAMP),
                        DataBase.getInt(SENSORID),
                        DataBase.getString(ROOM),
                        DataBase.getString(CHANNEL),
                        DataBase.getString(VALUE),
                        DataBase.getString(STATUS));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return sensorLog;
     }

     /**
      * Method to retrieve a list of sensors by the ID
      * @param sensorId Sensor ID
      * @return Collection A list with all the information retrieved from the DB
      */
     public static Collection<SensorLog> getSensorsLogsById(Integer sensorId) {

        LinkedList<SensorLog> sensorLogList = new LinkedList<SensorLog>();
        SensorLog sensorLog = null;

        if (sensorId != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".SensorLog "
                    + "WHERE SensorLog.sensorId = " + sensorId);

            // Create a sensor with the query results
            while(DataBase.next()){

                sensorLog = new SensorLog(DataBase.getTimestamp(TIMESTAMP),
                        DataBase.getInt(SENSORID),
                        DataBase.getString(ROOM),
                        DataBase.getString(CHANNEL),
                        DataBase.getString(VALUE),
                        DataBase.getString(STATUS));
                
                sensorLogList.add(sensorLog);
            }

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return sensorLogList;
     }
     
     /**
      * Method to retrieve the activities recorded in a certain period of time
      * @param time Starting time for the logging
      * @return A collection of Activities recorded on the log table
      */
      public static Collection<SensorLog> getSensorLog(Date time){
          
            LinkedList<SensorLog> activitiesLogList = new LinkedList<SensorLog>();
            SensorLog activityLog = null;
          
            //Logs (Activities)
            DataBase.sqlQuerySelect(
                    "SELECT * FROM " + DataBase.getDataBaseName() + ".SensorLog "
                    + "WHERE SensorLog.timestamp > '" + CommonVar.dateFormatDB.format(time) + "'");

            // Create a sensor with the query results
            while(DataBase.next()){

                activityLog = new SensorLog(DataBase.getTimestamp(TIMESTAMP),
                        DataBase.getInt(SENSORID),
                        DataBase.getString(ROOM),
                        DataBase.getString(CHANNEL),
                        DataBase.getString(VALUE),
                        DataBase.getString(STATUS));

                activitiesLogList.add(activityLog);
            }

            //Close ResultSet
            DataBase.closeResultSet();

            return activitiesLogList;
      }
     
     
     ///////////////// ACTIVITIES FUNCTIONS \\\\\\\\\\\\\\\\\\\\\\\\\\
     
     /**
      * Method to retrieve the activities recorded in a certain period of time
      * @param activity Activity name
      * @param id Activity ID
      * @param time Starting or ending time for the activity
      * @param location Activity location
      * @param status The current status of the activity
      */
     public static void insertUserActivity(String activity, int id, Date time, String location, int value, int status ){

        // Execute query
        DataBase.sqlQueryInsert("INSERT INTO " + DataBase.getDataBaseName() + ".SensorLog "
                + "(timestamp, sensorId, room, channel, value, status) "
                + "VALUES('" + CommonVar.dateFormatDB.format(time) + "',"
                + id + ",'" + location +"','" + activity + "'," + value + "," + status + ")");
        
//        System.out.println("INSERT INTO " + DataBase.getDataBaseName() + ".SensorLog "
//                + "(timestamp, sensorId, room, channel, value, status) "
//                + "VALUES('" + CommonVar.dateFormatDB.format(time) + "',"
//                + id + ",'" + location +"','" + activity + "','0'," + status + ")");

        //Close ResultSet
        DataBase.closeResultSet();

     }
     
     /**
      * Method to retrieve the activities recorded in a certain period of time
      * @param time Starting time for the logging
      * @return A collection of Activities recorded on the log table
      */
      public static Collection<SensorLog> getUserActivitiesLog(Date time){
          
            LinkedList<SensorLog> activitiesLogList = new LinkedList<SensorLog>();
            SensorLog activityLog = null;
            String query = "SELECT * FROM " + DataBase.getDataBaseName() + ".SensorLog "
                    + "WHERE SensorLog.timestamp > '" + CommonVar.dateFormatDB.format(time) + "' "
                    + "AND SensorLog.sensorId >= 900";
            
            //Logs (Activities)
            DataBase.sqlQuerySelect(query);

            // Create a sensor with the query results
            while(DataBase.next()){

                activityLog = new SensorLog(DataBase.getTimestamp(TIMESTAMP),
                        DataBase.getInt(SENSORID),
                        DataBase.getString(ROOM),
                        DataBase.getString(CHANNEL),
                        DataBase.getString(VALUE),
                        DataBase.getString(STATUS));

                activitiesLogList.add(activityLog);
            }

            //Close ResultSet
            DataBase.closeResultSet();

            return activitiesLogList;
      }

}
