/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import DataBaseModel.Sensors;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Ismael
 */
public class SensorsDB {

    /**
     * Singleton.
     */
    private static SensorsDB __singleton = new SensorsDB();

    /*
     * Table columns
     */
    private static final int SENSORID=1;
    private static final int VALUE=2;
    private static final int LOCATIONID=3;
    private static final int NAME=4;
    private static final int SENSORACCESSPOINTID=5;
    private static final int SENSORRULE=6;
    private static final int CHANNELDESCRIPTOR=7;
    private static final int SENSORTYPEID=8;
    private static final int LASTUPDATE=9;
    private static final int LASTTIMEACTIVE=10;
    private static final int LASTACTIVEVALUE=11;
    private static final int STATUS=12;


    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private SensorsDB() {
     }

     /**
      * Method to retrieve a sensor by its ID
      * @param sensorId Sensor ID
      * @return SensorModel A sensor with all the information retrieved from the DB
      */
     public static Sensors getSensorById(Integer sensorId){

        Sensors sensor = null;

        if (sensorId != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + 
                    DataBase.getDataBaseName() + ".Sensors WHERE sensorId = " + sensorId);

            // Create a sensor with the query results
            if (DataBase.next())

                sensor = new Sensors(DataBase.getInt(SENSORID),
                        DataBase.getFloat(VALUE),
                        DataBase.getInt(LOCATIONID),
                        DataBase.getString(NAME),
                        DataBase.getInt(SENSORACCESSPOINTID),
                        DataBase.getString(SENSORRULE),
                        DataBase.getString(CHANNELDESCRIPTOR),
                        DataBase.getInt(SENSORTYPEID),
                        DataBase.getTimestamp(LASTUPDATE),
                        DataBase.getTimestamp(LASTTIMEACTIVE),
                        DataBase.getFloat(LASTACTIVEVALUE),
                        DataBase.getString(STATUS));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return sensor;
     }

     /**
      * Method to retrieve a sensor ID based on its name
      * @param sensorName Sensor name
      * @return int Sensor ID
      */
     public static int getSensorIdByName(String sensorName){

        int sensorId = 9999;

        if (sensorName != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT sensorId FROM " +
                    DataBase.getDataBaseName() + ".Sensors WHERE name = '" + sensorName + "'");

            // Create a sensor with the query results
            if (DataBase.next())

                sensorId = DataBase.getInt(SENSORID);

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return sensorId;
     }


     /**
      * Method to retrieve a set of sensors
      * @return Collection A set of all sensors retrieved from the DB
      */
     public static Collection<Sensors> getSensors(){

        LinkedList<Sensors> sensors = new LinkedList<Sensors>();
        Sensors sensor = null;


        // Execute query
        DataBase.sqlQuerySelect("SELECT Sensors.*, SensorType.`sensorType`, Locations.`name`, SensorType.`madeBy`\n"
                + "FROM " + DataBase.getDataBaseName() + ".Sensors \n"
                + "INNER JOIN SensorType ON SensorType.`sensorTypeId` = Sensors.`sensorTypeId`\n"
                + "INNER JOIN Locations ON Locations.`locationId` =  Sensors.`locationId`\n"
                + "WHERE Sensors.sensorId < 100 ORDER BY sensorId ASC");

        // Create a sensor with the query results
        while(DataBase.next()){

            sensor = new Sensors(DataBase.getInt(SENSORID),
                    DataBase.getFloat(VALUE),
                    DataBase.getInt(LOCATIONID),
                    DataBase.getString(NAME),
                    DataBase.getInt(SENSORACCESSPOINTID),
                    DataBase.getString(SENSORRULE),
                    DataBase.getString(CHANNELDESCRIPTOR),
                    DataBase.getInt(SENSORTYPEID),
                    DataBase.getTimestamp(LASTUPDATE),
                    DataBase.getTimestamp(LASTTIMEACTIVE),
                    DataBase.getFloat(LASTACTIVEVALUE),
                    DataBase.getString(STATUS));

           sensors.add(sensor);
        }

        //Close ResultSet
        DataBase.closeResultSet();

        return sensors;
     }


      /**
      * Method to retrieve all the user valid locations
      * @return Collection The set of valid user valid location at UH Robot House
      */
     public static Collection<Sensors> getChildrenNodesSensorsByLocation(Integer locationId){

        LinkedList<Sensors> sensors = new LinkedList<Sensors>();
        Sensors sensor = null;

        //Select all the sensor located in a room based on the parent location id
         DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".Sensors WHERE Sensors.locationId = " + locationId +
                " OR Sensors.locationId IN (SELECT distinct t2.locationId FROM " + DataBase.getDataBaseName() + ".Locations AS t1" +
                                         " LEFT JOIN " + DataBase.getDataBaseName() + ".Locations AS t2 ON t1.locationId = t2.where" +
                                         " WHERE t1.locationId = "+ locationId + " AND t2.locationId IS NOT NULL)" +
                " OR Sensors.locationId IN (SELECT distinct t3.locationId FROM " + DataBase.getDataBaseName() + ".Locations AS t1" +
                                         " LEFT JOIN " + DataBase.getDataBaseName() + ".Locations AS t2 ON t1.locationId = t2.where" +
                                         " LEFT JOIN " + DataBase.getDataBaseName() + ".Locations AS t3 ON t2.locationId = t3.where" +
                                         " WHERE t1.locationId = "+ locationId + " AND t3.locationId IS NOT NULL)");

         // Add locationId to the children nodes vector
        while (DataBase.next()){
            sensor = new Sensors(DataBase.getInt(SENSORID),
                    DataBase.getFloat(VALUE),
                    DataBase.getInt(LOCATIONID),
                    DataBase.getString(NAME),
                    DataBase.getInt(SENSORACCESSPOINTID),
                    DataBase.getString(SENSORRULE),
                    DataBase.getString(CHANNELDESCRIPTOR),
                    DataBase.getInt(SENSORTYPEID),
                    DataBase.getTimestamp(LASTUPDATE),
                    DataBase.getTimestamp(LASTTIMEACTIVE),
                    DataBase.getFloat(LASTACTIVEVALUE),
                    DataBase.getString(STATUS));
            sensors.add(sensor);
        }

//        //Second Level Sensors query
//        DataBase.sqlQuerySelect("SELECT distinct t2.locationId FROM " + DataBase.getDataBaseName() + ".locations AS t1 "
//                + "LEFT JOIN " + DataBase.getDataBaseName() + ".locations AS t2 ON t1.locationId = t2.where "
//                + "WHERE t1.locationId = " + locationId + " AND t2.locationId IS NOT NULL");
//
//        // Add locationId to the children nodes vector
//        while (DataBase.next()){
//            locations.add(DataBase.getInt(LOCATIONID));
//        }
//
//        //Third Level Sensors query
//        DataBase.sqlQuerySelect("SELECT distinct t3.locationId FROM " + DataBase.getDataBaseName() + ".locations AS t1 "
//                + "LEFT JOIN " + DataBase.getDataBaseName() + ".locations AS t2 ON t1.locationId = t2.where "
//                + "LEFT JOIN " + DataBase.getDataBaseName() + ".locations AS t3 ON t2.locationId = t3.where "
//                + "WHERE t1.locationId = " + locationId + " AND t3.locationId IS NOT NULL");
//
//        // Add locationId to the children nodes vector
//        while (DataBase.next()){
//            locations.add(DataBase.getInt(LOCATIONID));
//        }

        //Close ResultSet
        DataBase.closeResultSet();

        return sensors;
     }


}