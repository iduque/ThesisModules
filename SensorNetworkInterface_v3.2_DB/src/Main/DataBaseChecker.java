package Main;

import DataBase.LocationsDB;
import DataBase.SensorTypeDB;
import DataBase.SensorsDB;
import DataBaseModel.Sensors;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DataBaseChecker extends Thread{

    Controller _controller;

    private LinkedHashMap<Integer,String> _sensorsStatus = new LinkedHashMap<Integer, String>();
    private LinkedHashMap<Integer,Float> _sensorsValues = new LinkedHashMap<Integer, Float>();
//    private LinkedList<Object> _auxHotWater = new LinkedList<Object>();
//    private LinkedList<Object> _auxColdWater = new LinkedList<Object>();
//    private Boolean _statusHot = false;
//    private Boolean _statusCold = false;
    
   public DataBaseChecker(Controller controller) {
      
      _controller = controller;

   }

   public DataBaseChecker() { }

    @Override
   public void run() {

    try{

        // Now loop forever, waiting to receive packets and printing them.
        while (true){

            Collection<Sensors> sensorsList = SensorsDB.getSensors();
            Iterator it = sensorsList.iterator();

            if (_sensorsStatus.isEmpty()){

                while(it.hasNext()){
                    Sensors s = (Sensors) it.next();
                    
                    String room = LocationsDB.getParentLocation(s.getLocationId());

                    String status = getStatusFromValue(s);
                    _sensorsStatus.put(s.getSensorId(), status);
                    _sensorsValues.put(s.getSensorId(), s.getValue());

                    //UH Robot House Id is 0 (Every sensors belong to UH Robot House)
                    //We avoid to represent this information twice (Room and Robot House)
                    if (!LocationsDB.getLocationByName(room).getLocationId().equals(0))
                        _controller.updateSensorInterface(room, s, status, true);
                }

            }else{

                while(it.hasNext()){
                    Sensors s = (Sensors) it.next();

                    String room = LocationsDB.getParentLocation(s.getLocationId());

                    //UH Robot House Id is 0 (Every sensors belong to UH Robot House)
                    //We avoid to represent this information twice (Room and Robot House)
                   if (!LocationsDB.getLocationByName(room).getLocationId().equals(0)){

                        if(!s.getValue().equals(_sensorsValues.get(s.getSensorId()))){

                            //Set new sensor value - prueba[1] = value
                            String newStatus = getStatusFromValue(s);

                            _sensorsValues.put(s.getSensorId(), s.getValue());
                            
                            //True if the status is different and the system started to record the infomtion
                            if(!newStatus.equals(_sensorsStatus.get(s.getSensorId()))){
//
////    System.out.println("Sensor: " + s.getName() + " Value: " + s.getValue() +
////            " Date: " + CommonVar.dateFormatDB.format(new Date(s.getLastUpdate().getTime())));

                                _sensorsStatus.put(s.getSensorId(), newStatus);
                                _controller.updateSensorInterface(room, s, newStatus, true);

                                //Call the activity recognizer with the new sensor triggered
                                _controller.recongnition(s.getName(),
                                        new Date(s.getLastUpdate().getTime()),
                                        newStatus);
                                
                            }else _controller.updateSensorInterface(room, s, "", false);
                       }
                    }

                }//while

            } //if-else

           /**
            * Stop the thread for 1 second
            */
           Thread.sleep(1000);

        } //while

    } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage() + "\nQuery error. Closing the application...");
        _controller.close();
    }

    }

   
      /**
    * Change the original value sending by the sensor for an understandable one 
    * @param value Sensor's value
    * @param type Sensor's type
    * @param rule Sensor's rule (just in POWER CONSUMPTION MONITOR sensors)
    * @return New value assigned to the sensor
    */
    private String getStatusFromValue(Sensors s) throws ScriptException {

//        int filter_length = 10;
        String type = SensorTypeDB.getSensorTypeById(s.getSensorTypeId()).getSensorType();

        if (type.equals("CONTACT_REED")){

            if (s.getValue() > 0)  return CommonVar.ON;
            else  return CommonVar.OFF;

        }else if(type.equals("CONTACT_PRESSUREMAT")){

            if (s.getValue() > 0) return CommonVar.OFF;
            else return CommonVar.ON;

        }else if(type.equals("TEMPERATURE_MCP9700_HOT")){// && room.equals("Kitchen")){

//                _auxHotWater.addLast(value);
//                if(_auxHotWater.size() > filter_length) _auxHotWater.pop();
//
//                float average = 0.0f;
//
//                for(int i=0; i<_auxHotWater.size(); i++)
//                    average+= (Float) _auxHotWater.get(i);
//
//                average = average/new Float(_auxHotWater.size());
//
//                if(!_statusHot && (value > (1.01 * average))){
//                    _statusHot = true;
//                }else if(_statusHot && value < average ) {
//                    _statusHot = false;
//                }
//
//                if(_statusHot) return CommonVar.ON;
//                else return CommonVar.OFF;

                  return s.getStatus().toLowerCase();

        }else if(type.equals("TEMPERATURE_MCP9700_COLD")){// && room.equals("Kitchen")){

//                _auxColdWater.addLast(valu.e);
//                if(_auxColdWater.size()> filter_length) _auxColdWater.pop();
//
//                float average = 0.0f;
//
//                for(int i=0; i<_auxColdWater.size(); i++)
//                    average+= (Float) _auxColdWater.get(i);
//
//                average = average/new Float(_auxColdWater.size());
//
//                if(!_statusCold && (value < (0.99 * average))){
//                    _statusCold = true;
//                }else if(_statusCold && (value > average)) {
//                    _statusCold= false;
//                }
//
//                if(_statusCold) return CommonVar.ON;
//                else return CommonVar.OFF;

                return s.getStatus().toLowerCase();

        }else if(type.equals("POWER_CONSUMPTION_MONITOR")){

            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            engine.put("Watts", s.getValue());

            if(engine.eval(s.getSensorRule()).toString().equals("true"))
                return CommonVar.ON;
            else return CommonVar.OFF;

        }else return CommonVar.UNDEFINED;

    }
}
