/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActivitiesModel;

import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Substructure. It is used by SensorsXMLModel class
 * Represent all the sensor associated to one MAC
 * @author id11ab
 */
public class ActivityProperties{
    
     private Double _threshold; //Allow us to define an activity as recognized
     private String _location; //Where the activity is performed
     private Long _duration; //The activity's duration in miliseconds (User´s preferences)
     private Time _endTime; //Time in which the activity finished
     private Time _startTime; //Time in which the activity started to be recognized
     private LinkedHashMap<String, ContextProperties> _contexts; //Previous activity
     private LinkedList<Time> _times; //Time in which the activity is performed (User´s preferences)
     private LinkedHashMap<String,SensorProperties> _sensors; //List of sensors

     /**
      * Constructor
      */
     public ActivityProperties(){
        _threshold = 0.0; 
        _location = "";
        _duration = new Long("0");
        _endTime =  new Time(0);
        _startTime =  new Time(0);
        _contexts = new LinkedHashMap <String,ContextProperties>();
        _sensors = new LinkedHashMap <String,SensorProperties>();
     }

     /**
      * Contructor
      * @param threshold Activity Recognition threshold
      * @param context Previous activity
      * @param time Time in which the activity is performed
      * @param location Where the activity is performed
      * @param sensors List of sensors
      */
     public ActivityProperties(Long duration, Double threshold, LinkedHashMap <String,ContextProperties> context,
             LinkedList<Time> time, String location,LinkedHashMap<String, SensorProperties> sensors){
        
        _threshold = threshold;
        _location = location;
        _duration = duration;
        _endTime =  new Time(0);
        _startTime = new Time(0);
        _contexts = context;
        _sensors = sensors;
     }

     protected ActivityProperties(ActivityProperties ap){
        _threshold = ap._threshold;
        _location = ap._location;
        _duration = ap._duration;
        _endTime =  ap._endTime;
        _startTime =  ap._startTime;
        _contexts = ap._contexts;
        _sensors = ap._sensors;
     }

     public ActivityProperties copy(){
         return new ActivityProperties(this);
     }
    
     /**
      * Get the threshold for the activity recognition
      * @return The activity threshold
      */
     public Double getThreshold() { return _threshold; }

     /**
      * Set the threshold for the activity recognition
      * @param threshold The activity threshold
      */
     public void setThreshold(Double threshold) { _threshold = threshold; }

          /**
      * Get the location
      * @return The activity location
      */
     public String getLocation() { return _location; }


     /**
      * Set the location
      * @param location The activity location
      */
     public void setLocation(String location) { _location = location; }

      /**
      * Get the activity duration defined
      * @return The activity duration
      */
     public Long getDuration() { return _duration; }

     /**
      * Set the activity duration
      * @param duration The activity duration
      */
     public void setDuration(Long duration) { _duration = duration; }

      /**
      * Get the activity end time
      * @return Time object
      */
     public Time getEndTime() { return _endTime; }

     /**
      * Set the activity end time
      * @param time Time object
      */
     public void setEndTime(Time time) { _endTime = time; }

     /**
      * Get the activity start time
      * @return Time object
      */
     public Time getStartTime() { return _startTime; }

     /**
      * Set the activity start time
      * @param time Time object
      */
     public void setStartTime(Time time) { _startTime = time; }

     /**
      * Get the context
      * @return The activity context
      */
     public LinkedHashMap <String,ContextProperties> getContext() { return _contexts; }
     
     /**
      * Add one context to the list of activity's contexts
      * @param context The previous activity name
      * @param properties The context status and time required
      */
     public void addContext(String context, ContextProperties properties) {
         _contexts.put(context, properties);
     }
     
     /**
      * Set the context
      * @param context The activity context
      */
     public void setContext(LinkedHashMap <String,ContextProperties> context) { _contexts = context; }

     /**
      * Add one sensor to the list of sensors
      * @param sensor The sensor name
      * @param sensorProp The sensor properties
      */
     public void addSensor(String sensor, SensorProperties sensorProp) {
         _sensors.put(sensor, sensorProp);
     }

     /**
      * Number of sensors associated to each activity
      * @return Number of sensors
      */
     public int getNumberSensors(){return _sensors.size();}

     /**
      * Look for a sensor associated to this activity
      * @param sensor Sensor name
      * @return True if it exists, False if it does not exist.
      */
     public boolean existSensor(String name){

         if(_sensors.containsKey(name)) return true;
         else return false;
         
     }

     /**
      * Delete the selected sensor
      * @param sensor Sensor name
      */
     public  SensorProperties deleteSensor(String name){
         return _sensors.remove(name);
     }
     
     /**
      * Return the sensor's properties from the selected sensor
      * @param name Sensor name
      * @return The object SensorProperties associated
      */
     public SensorProperties getSensorProperties(String name){
         return _sensors.get(name);
     }

     /**
      * Return the sensors list
      * @return Return the sensors list
      */
     public LinkedHashMap <String,SensorProperties> getSensorsList(){
         return _sensors;
     }

     /**
      * Reset the list of sensors
      */
     public void resetSensorList(){
        _sensors = null;
     }

 }
