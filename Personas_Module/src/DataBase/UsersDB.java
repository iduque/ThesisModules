/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import DataBaseModel.Users;

/**
 *
 * @author Ismael
 */
public class UsersDB {

    /**
     * Singleton.
     */
    private static UsersDB __singleton = new UsersDB();

    /*
     * Table columns
     */
    private static final int USERID=1;
    private static final int FIRSTNAME=2;
    private static final int LASTNAME=3;
    private static final int NICKNAME=4;
    private static final int LOCATIONID=5;
    private static final int POSEID=6;
    private static final int CONTEXTID=7;
    private static final int UNIQUEROBOTHOUSEUSERID=8;
    private static final int LANGUAGEID=9;
    private static final int XCOORD=10;
    private static final int YCOORD=11;
    private static final int ORIENTATION=12;
    private static final int PERSONAID=13;
    

    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private UsersDB() {
     }

     /**
      * Method to retrieve a user by his ID
      * @param userId ID
      * @return Users Object
      */
     public static Users getUserById(Integer userId){

        Users users = null;

        if (userId != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".users "
                    + "WHERE users.userId = " + userId );

            // Create a sensor with the query results
            if (DataBase.next())

                users = new Users(
                        DataBase.getInt(USERID),
                        DataBase.getString(FIRSTNAME),
                        DataBase.getString(LASTNAME),
                        DataBase.getString(NICKNAME),
                        DataBase.getInt(PERSONAID));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        
        return users;
     }
     
     /**
      * Method to retrieve a user by his name
      * @param firstName User's first name
      * @param lastName User's last name
      * @return Users Object
      */
     public static Users getUserByName(String firstName, String lastName){

        Users users = null;

       // Execute query
       DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".users "
                    + "WHERE users.firstName = '" + firstName + "' AND users.lastName = '" + lastName + "'" );

        // Create a sensor with the query results
        if (DataBase.next())

            users = new Users(
                    DataBase.getInt(USERID),
                    DataBase.getString(FIRSTNAME),
                    DataBase.getString(LASTNAME),
                    DataBase.getString(NICKNAME),
                    DataBase.getInt(PERSONAID));

        //Close ResultSet
        DataBase.closeResultSet();
        
        return users;
     }

      /**
      * Insert to a new user
      * @param user User object
      */
     public static void insertUser(Users user){
         
         // Execute query
        DataBase.sqlQueryInsert("INSERT INTO " + DataBase.getDataBaseName() + ".users "
                + "(firstName, lastName, nickName, locationId, poseId, contextId, personaId) "
                + "VALUES('" + user.getFirstName() + "','" + user.getLastName() + "',"
                + "'" + user.getNickName() + "', 0, 0, 0, " + user.getPersonaId() + ")");

        //Close ResultSet
        DataBase.closeResultSet();

     }
     
     /**
      * Update the personaId value for the user
      * @param user User object
      */
     public static void updatePersonaIdByUserId(int userId, int personaId){
         
         // Execute query
        DataBase.sqlQueryInsert("UPDATE " + DataBase.getDataBaseName() + ".users "
                + "SET personaId = " + personaId + " WHERE userId = " + userId);

        //Close ResultSet
        DataBase.closeResultSet();

     }
}