package SensorsModules;

import Controller.CommonVar;
import Controller.Controller;
import SensorsModel.Details;
import SensorsModel.Module;
import SensorsModel.SensorsXMLModel;
import java.sql.ResultSet;
import java.sql.Time;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class GeoSystemHandler implements Runnable{

  private Controller _controller;
  private SensorsXMLModel _data;
  
  private String _query;
  private Thread t;
  private ResultSet rs;
  private String _columns = "";
  private String _table = "";
    
  public GeoSystemHandler(Controller c, SensorsXMLModel data, String query, String table, String columns) {

      _controller = c;
      _query = query;
      _data = data;
      _columns = columns;
      _table = table;
      
      t = new Thread(this,"GeoSystem");
      t.start();
  }

  public GeoSystemHandler() {}

    @Override
    @SuppressWarnings("static-access")
    public void run() {
        
        try {
           /**
             * This loop is executed every 1 second
             */ 
           while(true){
               /**
                * Polling all the data from GeoSystem
                */
               rs = _controller.sqlQuerySelectGeoSystem(_query);

               while (rs.next()) {

                   String sensor = "ID";
                   if(Integer.valueOf(rs.getString(1)) < 10 ) sensor += "0" + rs.getString(1);
                   else sensor += rs.getString(1);

                   if(_data.containsSensor("GeoSystem", sensor)){

  //System.out.println(rs.getString(1) + " " + rs.getString(2) + " " +  rs.getString(3));

                        String room = _data.getRoom("GeoSystem", sensor);
                        Module module = _data.getModule(room, "GeoSystem");
                        Details details = _data.getDetailSensor(room, "GeoSystem", sensor);
                        //Set Status value


                        String status="";
                        if (evaluateRule(details.getRule(), rs.getString(3))) status = CommonVar.ON;
                        else status = CommonVar.OFF;

                      //True if the status is different and the system started to record the infomtion
                      if(_controller.setValueCell(room,details.getId(), rs.getString(3), status)){
                           String time = String.valueOf(System.currentTimeMillis());
                           //SQL query (timestamp,module,id,room,channel,name,value,status)
                           String sqlQuery = "INSERT INTO " + _table + " " + _columns + " VALUES " +
                                            "('" + time + "','"
                                                 + module.getModuleName() + "','"
                                                 + details.getId() + "','"
                                                 + room + "','"
                                                 + sensor + "','"
                                                 + details.getName() + "','"
                                                 + rs.getString(3) + "','"
                                                 + status + "')";

                          //Insert the data into the MySQL local database
                           _controller.sqlQueryInsert(sqlQuery);
                           //Call the activity recognizer with the new sensor triggered
                           _controller.recongintion(details.getName(), new Time(Long.valueOf(time)), status);

                        }
                   }
               }
              
               /**
                * Stop the thread for 1 second
                */              
               t.sleep(1000);
            }

        } catch (Exception e) {
            
            t.interrupt();  System.out.println("Process GeoSystem stopped");
            _controller.closeDBs();
 
        }
    }

    /**
     * Evaluate the rule associated to each GeoSystem appliance with a JavaScript engine
     * @param rule Rule associated
     * @param value Value to be evaluated
     * @return Boolean with the result of the evaluation
     * @throws ScriptException 
     */
    private boolean evaluateRule(String rule, String value ) throws ScriptException{

            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            engine.put("p", value);

            if(engine.eval(rule).toString().equals("true"))
                return true;
            else return false;
    }

    /**
     * Close the Database and the thread
     */
    public void close(){
        t.interrupt();
        System.out.println("Process GeoSystem stopped");
    }
}