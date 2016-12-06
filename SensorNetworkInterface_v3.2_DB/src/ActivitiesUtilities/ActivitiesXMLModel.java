/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActivitiesUtilities;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Structure to store the activities read from the XML file
 * @author id11ab
 */   
  public class ActivitiesXMLModel{
      
         //The key corresponds to the activity name
         // The ActivityModel object stores all the information related with the activity
         private LinkedHashMap<String, ActivityProperties> _activities;

         /**
          * Constructor
          */
         public ActivitiesXMLModel(){
             _activities = new LinkedHashMap<String, ActivityProperties>();
         }

         /**
          * Constructor
          */
         public ActivitiesXMLModel(ActivitiesXMLModel m){
             _activities = m.getActivities();
         }

         /**
          * Add a new activity to the structure
          * @param activity Activity's name
          * @param activityProperties LinkedList with all the sensors involved in the activity
          */
         public void addActivity(String activity, ActivityProperties activityProperties) {
             _activities.put(activity, activityProperties);
         }

         /**
          * Ask for the number of sensor in the selected activity
          * @param activity Activity name
          * @return Number of sensors
          */
         public int getNumberSensorsActivity(String activity){
             return _activities.get(activity).getNumberSensors();
         }
         
         /**
          * Ask for the sensor's properties in some activity
          * @param activity Activity name
          * @param sensor Sensor name
          * @return Sensor's properties
          */
         public SensorProperties getSensorsPropiertiesActivity(String activity, String sensor){
             return _activities.get(activity).getSensorProperties(sensor);
         }
         
         /**
          * Return the activities HashMap
          * @return LinkedHashMap<String, ActivityProperties>
          */
         public LinkedHashMap<String, ActivityProperties> getActivities(){
             return _activities;
         }
         

         /**
          * Obtain the ActivityModel object from the activity's name
          * @param activity Activity's name
          * @return ActivityModel
          */
          public ActivityProperties getActivityProperties(String activity) {
            return _activities.get(activity);
          }

          /**
          * Delete the selected activity
          * @param activity Activity's name
          * @return ActivityProperties
          */
          public ActivityProperties removeActivity(String activity) {
            return _activities.remove(activity);
          }

          /**
          * Reset the list of sensors
          */
         public void resetSensorList(){
             Iterator it = _activities.keySet().iterator();

             while(it.hasNext()){
                 String activity = (String) it.next();
                 _activities.get(activity).resetSensorList();
             }
         }
        
    }
