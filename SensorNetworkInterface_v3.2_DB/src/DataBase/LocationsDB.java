/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import DataBaseModel.Locations;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Ismael
 */
public class LocationsDB {

    /**
     * Singleton.
     */
    private static LocationsDB __singleton = new LocationsDB();

    /*
     * Table columns
     */
    private static final int LOCATIONID=1;
    private static final int NAME=2;
    private static final int WHERE=3;
    private static final int XCOORD=4;
    private static final int YCOORD=5;
    private static final int VALIDROBOTLOCATION=6;
    private static final int VALIDUSERLOCATION=7;

    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private LocationsDB() {
     }

     /**
      * Method to retrieve a location by its ID
      * @param locationId Location ID
      * @return Locations A location object with all the information retrieved from the DB
      */
     public static Locations getLocationById(Integer locationId){

        Locations locations = null;

        if (locationId != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".Locations WHERE Locations.LocationId = " + locationId);

            // Create a sensor with the query results
            if (DataBase.next())

                locations = new Locations(DataBase.getInt(LOCATIONID),
                        DataBase.getString(NAME),
                        DataBase.getInt(WHERE),
                        DataBase.getInt(XCOORD),
                        DataBase.getInt(YCOORD),
                        DataBase.getInt(VALIDROBOTLOCATION),
                        DataBase.getInt(VALIDUSERLOCATION));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return locations;
     }

          /**
      * Method to retrieve a location object by its name
      * @param locationId Location ID
      * @return Locations A location object with all the information retrieved from the DB
      */
     public static Locations getLocationByName(String name){

        Locations locations = null;

        if (name != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".Locations WHERE Locations.Name = '" + name + "'");

            // Create a sensor with the query results
            if (DataBase.next())

                locations = new Locations(DataBase.getInt(LOCATIONID),
                        DataBase.getString(NAME),
                        DataBase.getInt(WHERE),
                        DataBase.getInt(XCOORD),
                        DataBase.getInt(YCOORD),
                        DataBase.getInt(VALIDROBOTLOCATION),
                        DataBase.getInt(VALIDUSERLOCATION));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return locations;
     }

      /**
      * Method to retrieve all the user valid locations
      * @return Collection The set of valid user valid location at UH Robot House
      */
     public static Collection<Locations> getAllLocations(){

        LinkedList<Locations> locations = new LinkedList<Locations>();
        Locations location = null;

        // Execute query
        DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".Locations ");
                //+ "AND Locations.locationId IN (SELECT Sensors.locationId FROM " + DataBase.getDataBaseName() + ".Sensors)");

        // Create a sensor with the query results
        while (DataBase.next()){

            location = new Locations(DataBase.getInt(LOCATIONID),
                    DataBase.getString(NAME),
                    DataBase.getInt(WHERE),
                    DataBase.getInt(XCOORD),
                    DataBase.getInt(YCOORD),
                    DataBase.getInt(VALIDROBOTLOCATION),
                    DataBase.getInt(VALIDUSERLOCATION));

            locations.add(location);
        }

        //Close ResultSet
        DataBase.closeResultSet();

        return locations;
     }

      /**
      * Method to retrieve all the user valid locations
      * @return Collection The set of valid user valid location at UH Robot House
      */
     public static Collection<Locations> getValidLocations(){

        LinkedList<Locations> locations = new LinkedList<Locations>();
        Locations location = null;

        // Execute query
        DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".Locations "
                + "WHERE Locations.where = 0 AND Locations.locationId != 0 ");
                //+ "AND Locations.locationId IN (SELECT Sensors.locationId FROM " + DataBase.getDataBaseName() + ".Sensors)");

        // Create a sensor with the query results
        while (DataBase.next()){

            location = new Locations(DataBase.getInt(LOCATIONID),
                    DataBase.getString(NAME),
                    DataBase.getInt(WHERE),
                    DataBase.getInt(XCOORD),
                    DataBase.getInt(YCOORD),
                    DataBase.getInt(VALIDROBOTLOCATION),
                    DataBase.getInt(VALIDUSERLOCATION));

            locations.add(location);
        }

        //Close ResultSet
        DataBase.closeResultSet();

        return locations;
     }

     /**
      * Method to retrieve the Parent location of a location (Distinct to Global location - Id != 0)
      * @return String Parent location name (UH Robot House is never obtained Id = 0)
      */
     public static String getParentLocation(Integer locationId){
         
         DataBase.sqlQuerySelect("SELECT Locations.name, Locations.where "
                 + "FROM " + DataBase.getDataBaseName() + ".Locations "
                 + "WHERE Locations.locationId = " + locationId);
         
         if (DataBase.next())
                if(DataBase.getInt(2) != 0)
                       getParentLocation(DataBase.getInt(2));
         
         return DataBase.getString(1);
     }
     
    public static int getTopLocation(int location, Collection<Locations> allLocations) {
        for (Locations loc : allLocations) {
            if (loc.getLocationId() == location) {
                if (loc.getWhere() != 0) {
                    return getTopLocation(loc.getWhere(), allLocations);
                } else {
                    return location;
                }
            }
        }
        return location;
    }
           /**
      * Method to retrieve all the user valid locations
      * @return Collection The set of valid user valid location at UH Robot House
      */
     public static Collection<Integer> getChildrenNodesSensorsIdByParentLocation(Integer locationId){

        LinkedList<Integer> locations = new LinkedList<Integer>();

//        //Second Level Sensors query
//        DataBase.sqlQuerySelect("SELECT distinct t2.locationId FROM " + DataBase.getDataBaseName() + ".Locations AS t1 "
//                + "LEFT JOIN " + DataBase.getDataBaseName() + ".Locations AS t2 ON t1.locationId = t2.where "
//                + "WHERE t1.locationId = " + locationId + " AND t2.locationId IS NOT NULL");
//
//        // Add locationId to the children nodes vector
//        while (DataBase.next()){
//            locations.add(DataBase.getInt(LOCATIONID));
//        }
//
//        //Third Level Sensors query
//        DataBase.sqlQuerySelect("SELECT distinct t3.locationId FROM " + DataBase.getDataBaseName() + ".Locations AS t1 "
//                + "LEFT JOIN " + DataBase.getDataBaseName() + ".Locations AS t2 ON t1.locationId = t2.where "
//                + "LEFT JOIN " + DataBase.getDataBaseName() + ".Locations AS t3 ON t2.locationId = t3.where "
//                + "WHERE t1.locationId = " + locationId + " AND t3.locationId IS NOT NULL");
//
//        // Add locationId to the children nodes vector
//        while (DataBase.next()){
//            locations.add(DataBase.getInt(LOCATIONID));
//        }
//
//        //Close ResultSet
//        DataBase.closeResultSet();

        return locations;
     }

//     /**
//      * Method to retrieve a set of sensors
//      * @return Collection A set of all sensors retrieved from the DB
//      */
//     public static Collection<Sensor> getSensors() throws ClassNotFoundException, InstantiationException,
//             IllegalAccessException, SQLException {
//
//        LinkedList<Sensor> sensors = new LinkedList<Sensor>();
//        Sensor sensor = null;
//
//
//        // Execute query
//        DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".Sensors");
//
//        // Create a sensor with the query results
//        while(DataBase.next()){
//
//            sensor = new Sensor(DataBase.getInt(SENSORID),
//                    DataBase.getFloat(VALUE),
//                    DataBase.getInt(LOCATIONID),
//                    DataBase.getString(NAME),
//                    DataBase.getInt(SENSORACCESSPOINTID),
//                    DataBase.getString(SENSORRULE),
//                    DataBase.getString(CHANNELDESCRIPTOR),
//                    DataBase.getInt(SENSORTYPEID),
//                    DataBase.getTime(LASTUPDATE));
//
//           sensors.add(sensor);
//        }
//
//        //Close ResultSet
//        DataBase.closeResultSet();
//
//        return sensors;
//     }

}


//     /**
//      * Método para anadir un nuevo diente
//      * <p>
//      * Precondicion: el código del diente y el código del paciente deber ser
//      * unicos y distintos de null
//      * @param diente Diente a anadir
//      */
//     public static void addSensor(Sensor sensor) {
//
//        Integer sensorId;
//        String value;
//
//        sensorId = sensor.getSensorId();
//        value = sensor.getValue();
//
//        if (sensorId != null) {
//
//            WrapperDB.conectar();
//
//            WrapperDB.ejecutaUpdate("INSERT INTO Sensors (sensorId, value) VALUES (" +
//                                        sensorId + ",'" + value+ "')");
//            WrapperDB.desconectar();
//        }
//     }