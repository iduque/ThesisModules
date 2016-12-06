/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SensorsModel;

import java.util.LinkedHashMap;

/**
 * Substructure. It is used by SensorsXMLModel class
 * Represent all the sensor associated to one MAC
 * @author id11ab
 */
public class Module{
    
     String _module; //ZigBee or GeoSystem
     LinkedHashMap <String,Details> _sensors; //List of sensors

     /**
      * Constructor
      */
     public Module(){
         _module = "";
         _sensors = new LinkedHashMap <String,Details>();
     }

     /**
      * Constructor from data
      * @param m Module name
      * @param s List of different sensors in the module
      */
     public Module(String m, LinkedHashMap<String, Details> s){
         _module = m;

         _sensors = s;
     }

     /**
      * Get the details of certain sensor
      * @param sensor Sensor name
      * @return Details object with the sensor's details
      */
     public Details getDetails(String sensor) { return _sensors.get(sensor); }

     /**
      * Get a Module name
      * @return String with the name of this module
      */
     public String getModuleName() { return _module; }

     /**
      * Get the list of sensors contained into this module
      * @return LinkedHashMap<String,Details> with each sensor and its details
      */
     public LinkedHashMap<String,Details> getSensors() {return _sensors; }

     /**
      * Look for a sensor inside this module
      * @param sensor Sensor ID
      * @return True if it exists, False if it does not exist.
      */
     public boolean existSensor(String sensor){

         if(_sensors.containsKey(sensor)) return true;
         else return false;
         
     }

 }
