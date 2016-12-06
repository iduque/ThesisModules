/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DataBase;

import DataBaseModel.UserPreferences;

/**
 *
 * @author Ismael
 */
public class UserPreferencesDB {

    /**
     * Singleton.
     */
    private static UserPreferencesDB __singleton = new UserPreferencesDB();

    /*
     * Table columns
     */
    private static final int USERID=1;
    private static final int NEWSWEBSITE=2;
    private static final int WEATHERWEBSITE=3;
    private static final int MUSICWEBSITE=4;
    private static final int RECIPEWEBSITE=5;
    private static final int LIGHT=6;
    private static final int SOUND=7;
    private static final int LOCATION=8;
    private static final int APPROACH=9;
    private static final int VOICE=10;
    

    //- Methods -----------------------------------------------------------
    /**
     * Constructor
     * <p>
     * Create a new instance of <tt>SensorsTable</tt>
     */
     private UserPreferencesDB() {
     }

     /**
      * Method to retrieve preferences of a user by his ID
      * @param userId ID
      * @param prefernceID Preference ID (A user could have more than one row)
      * @return UserPreferences Object
      */
     public static UserPreferences getPreferencesByUserId(Integer userId, Integer preferenceId){

        UserPreferences userPreferences = null;

        if (userId != null && preferenceId != null) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT * FROM " + DataBase.getDataBaseName() + ".userPreferences "
                    + "WHERE userPreferences.userId = " + userId + " AND userPreference.preferenceId = " + preferenceId);

            // Create a sensor with the query results
            if (DataBase.next())

                userPreferences = new UserPreferences(
                        DataBase.getInt(USERID),
                        DataBase.getString(NEWSWEBSITE),
                        DataBase.getString(WEATHERWEBSITE),
                        DataBase.getString(MUSICWEBSITE),
                        DataBase.getString(RECIPEWEBSITE),
                        DataBase.getString(LIGHT),
                        DataBase.getString(SOUND),
                        DataBase.getString(LOCATION),
                        DataBase.getString(APPROACH),
                        DataBase.getInt(VOICE));

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return userPreferences;
     }

          /**
      * Insert a new row with the user preferences
      * @param up UserPreferenes object
      */
     public static void insertUserPreferences(UserPreferences up){
         
         // Execute query
        DataBase.sqlQueryInsert("INSERT INTO " + DataBase.getDataBaseName() + ".userPreferences "
                + "(userId, newsWebsite, weatherWebsite, musicWebsite,"
                + " recipeWebsite, light, sound, location, approach, voice) "
                + "VALUES(" + up.getUserId() + ", '" + up.getNewsWebsite() + "',"
                + "'" + up.getWeatherWebsite() + "','" + up.getMusicWebsite() + "','" + up.getRecipeWebsite() + "',"
                + "'" + up.getLight() + "','" + up.getSound() + "','" + up.getLocation() + "',"
                + "'" + up.getApproach() + "'," + up.getVoice() + ")");

        //Close ResultSet
        DataBase.closeResultSet();

     }

         /**
      * Insert a new row with the user preferences
      * @param up UserPreferenes object
      */
     public static void updateUserPreferences(UserPreferences up){

         // Execute query
        DataBase.sqlQueryInsert("UPDATE " + DataBase.getDataBaseName() + ".userPreferences "
                + "SET newsWebsite= '" + up.getNewsWebsite() + "',"
                + " weatherWebsite= '" + up.getWeatherWebsite() + "',"
                + " musicWebsite = '" + up.getMusicWebsite() + "',"
                + " recipeWebsite = '" + up.getRecipeWebsite() + "',"
                + " light = '" + up.getLight() + "', sound='" + up.getSound() + "',"
                + " location = '" + up.getLocation() + "', approach = '" + up.getApproach() + "',"
                + " voice = '" + up.getVoice() + "' WHERE userId = " + up.getUserId());


        //Close ResultSet
        DataBase.closeResultSet();

     }

     /**
      * Method to retrieve preferences of a user by his ID
      * @param userId ID
      * @param prefernceID Preference ID (A user could have more than one row)
      * @return UserPreferences Object
      */
     public static int getPreferencesIdByUser(Integer userId){

        int userPreferencesId = 0;

        if (userId != null ) {

            // Execute query
            DataBase.sqlQuerySelect("SELECT COUNT(*) FROM " + DataBase.getDataBaseName() + ".userPreferences "
                    + "WHERE userPreferences.userId = " + userId );

            // Create a sensor with the query results
            if (DataBase.next())

                userPreferencesId = DataBase.getInt(1);

            //Close ResultSet
            DataBase.closeResultSet();
        }
        return userPreferencesId;
     }
}