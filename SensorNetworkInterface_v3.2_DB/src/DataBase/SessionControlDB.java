/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import java.sql.ResultSet;

/**
 *
 * @author Ismael
 */
public class SessionControlDB {

    /**
     * Singleton.
     */
    private static SessionControlDB __singleton = new SessionControlDB();

    /*
     * Table columns
     */
    private static final int SESSIONID=1;
    private static final int SESSIONDATE=2;
    private static final int SESSIONTIMES=3;
    private static final int SESSIONTIMEOFFSETMIN=4;
    private static final int SESSIONTIMEOFFSETHOURS=5;
    private static final int SESSIONUSER=6;

    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private SessionControlDB() {
     }

     /**
      * Method to retrieve a sensor status by its Id and timestamp
      * @param sensorId Sensor ID
      * @param timestamp Time for the last sensor update
      * @return SensorLog A sensor with all the information retrieved from the DB
      */
     public static String getUserSession(){

        String user = "";

        // Execute query
        ResultSet rs = DataBase.sqlQuerySelect("SELECT Users.nickname FROM " 
                + DataBase.getDataBaseName() + ".Users, " + DataBase.getDataBaseName() + ".SessionControl "
                + "WHERE SessionControl.SessionUser = Users.userId");

        if (DataBase.next())

                user = DataBase.getString(1);

        //Close ResultSet
        DataBase.closeResultSet();

        return user;
     }

}
