/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import DataBaseModel.SensorType;

/**
 *
 * @author Ismael
 */
public class SensorTypeDB {

    /**
     * Singleton.
     */
    private static SensorTypeDB __singleton = new SensorTypeDB();

    /*
     * Table columns
     */
    private static final int SENSORTYPEID=1;
    private static final int SENSORTYPE=2;
    private static final int MADEBY=3;

    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private SensorTypeDB() {
     }

     /**
      * Method to retrieve a SensorType by its ID.
      * @param sensorTypeId SensorType ID
      * @return SensorModel A SensorType object with all the information retrieved from the DB
      */
     public static SensorType getSensorTypeById(Integer sensorTypeId) {

        SensorType sensorType = null;

        if (sensorTypeId != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".SensorType "
                    + "WHERE SensorType.sensorTypeId = " + sensorTypeId);

            // Create a sensor with the query results
            if (DataBase.next())

                sensorType = new SensorType(DataBase.getInt(SENSORTYPEID),
                        DataBase.getString(SENSORTYPE),
                        DataBase.getString(MADEBY));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return sensorType;
     }
}
