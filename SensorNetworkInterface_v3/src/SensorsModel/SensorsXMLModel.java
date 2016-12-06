/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SensorsModel;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Structure to store the data read from the XML file
 * @author id11ab
 */   
  public class SensorsXMLModel{
      
         //The key corresponds to the Room (i.e. Kitchen)
         // The LinkedHashMap represents the MAC and the object Module
         LinkedHashMap<String, LinkedHashMap<String,Module>> _data;
         
         public SensorsXMLModel(){
             _data = new LinkedHashMap<String, LinkedHashMap<String,Module>>();
         }

         public void addKey(String key, LinkedHashMap<String,Module> modules){
             _data.put(key, modules);
         }
         
         
         /**
          * Return the vector with all the data
          * @return LinkedHashMap<String, LinkedHashMap<String,Module>>
          */
         public LinkedHashMap<String, LinkedHashMap<String,Module>> getData() {
             return _data;
         }

         /**
          * Look for a sensor between the read data
          * @param mac Module's MAC
          * @param sensor Sensor that we are looking for
          * @return True if the sensor is found, False otherwise.
          */
         public Boolean containsSensor(String mac, String sensor){

             Iterator it = _data.keySet().iterator();
             while(it.hasNext()){

                 String key = (String) it.next();
                 LinkedHashMap<String,Module> modules = _data.get(key);

                 if(modules.containsKey(mac))
                    if (modules.get(mac).existSensor(sensor))
                        return true;
             }
             
             return false;
         }

         /**
          * Return the room in which the MAC is found
          * @param mac Module's MAC
          * @param sensor The sensor that we are looking for
          * @return Room name
          */
         public String getRoom(String mac, String sensor){
             
             
             Iterator it = _data.keySet().iterator();
             while(it.hasNext()){
                 
                 String key = (String) it.next();
                 if (_data.get(key).containsKey(mac))
                     if (_data.get(key).get(mac).existSensor(sensor))
                        return key;
             }
             
             return "";
         }
         
         /**
          * Return a Module object associated to the supplied MAC
          * @param mac Module's MAC
          * @return Module
          */
         public Module getModule(String room, String mac){              
             return _data.get(room).get(mac);
         }

         /**
          * Return the detail of one sensor
          * @param room Located room
          * @param mac Associated MAC
          * @param sensor Sensor's name
          * @return Object Details for this sensor
          */
         public Details getDetailSensor(String room, String mac, String sensor){
             return _data.get(room).get(mac).getDetails(sensor);
         }

    }
