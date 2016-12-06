/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import DataBaseModel.SensorAccessPoint;

/**
 *
 * @author Ismael
 */
public class SensorsAccessPointDB {

    /**
     * Singleton.
     */
    private static SensorsAccessPointDB __singleton = new SensorsAccessPointDB();

    /*
     * Table columns
     */
    private static final int SENSORACCESSPOINTID=1;
    private static final int LOCATIONID=2;
    private static final int MACADDRESS=3;


    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private SensorsAccessPointDB() {
     }

     /**
      * Method to retrieve a SensorAccessPoint object base on a MAC Address
      * @param sensorId Sensor ID
      * @return SensorModel A sensorAccessPoint object with all the information retrieved from the DB
      */
     public static SensorAccessPoint getSensorAccessPointWhereMAC(String MAC){

        SensorAccessPoint sap = null;

        if (!MAC.equals("")) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".SensorAccessPoint WHERE MACAddress = " + MAC);

            // Create a sensor with the query results
            if (DataBase.next())

                sap = new SensorAccessPoint(DataBase.getInt(SENSORACCESSPOINTID),
                        DataBase.getInt(LOCATIONID),
                        DataBase.getString(MACADDRESS));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return sap;
     }


          /**
      * Method to retrieve a sensor by its ID
      * @param sensorId Sensor ID
      * @return SensorModel A sensor with all the information retrieved from the DB
      */
     public static SensorAccessPoint getSensorAccessPointMACById(Integer sensorAccessPointId){

        SensorAccessPoint sap = null;

        if (sensorAccessPointId != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".SensorAccessPoint "
                    + "WHERE SensorAccessPointId = " + sensorAccessPointId);

            // Create a sensor with the query results
            if (DataBase.next())

                sap = new SensorAccessPoint(DataBase.getInt(SENSORACCESSPOINTID),
                        DataBase.getInt(LOCATIONID),
                        DataBase.getString(MACADDRESS));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return sap;
     }
}