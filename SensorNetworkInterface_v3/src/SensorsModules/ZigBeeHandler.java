package SensorsModules;

import Controller.CommonVar;
import Controller.Controller;
import SensorsModel.Details;
import SensorsModel.Module;
import SensorsModel.SensorsXMLModel;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class ZigBeeHandler implements Runnable{
  
    Controller _controller;
    SensorsXMLModel _data;

    private  LinkedList<Object> _auxHotWater = new LinkedList<Object>();
    private  LinkedList<Object> _auxColdWater = new LinkedList<Object>();
    Boolean _statusHot = false;
    Boolean _statusCold = false;

    private int _port;
    private String _columns = "";
    private String _table = "";
    private String NUMBER_FORMAT;

    Thread t;
    
   public ZigBeeHandler(Controller controller, SensorsXMLModel data, int p,
          String table, String columns, String numberFormat) {
      
      _controller = controller;
      _data=data;
      _port = p;
      _columns = columns;
      _table = table;
      NUMBER_FORMAT = numberFormat;


      t = new Thread(this,"ZigBee");
      t.start();
   }

    public ZigBeeHandler() { }

   public void run() {
        try{
            // Create a socket to listen on the port.
            DatagramSocket dsocket = new DatagramSocket(_port);

            // Create a buffer to read datagrams into. If a
            // packet is larger than this buffer, the
            // excess will simply be discarded!
            byte[] buffer = new byte[2048];

            // Create a packet to receive data into the buffer
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Now loop forever, waiting to receive packets and printing them.
            while (true){
                // Wait to receive a datagram
                dsocket.receive(packet);

                // Convert the contents to a string, and display them
                String msg = new String(buffer, 0, packet.getLength());
                //recived[0]=timestamp - recived[1] = MAC - recived[2]=channel (AD01,DI01,...) - recived[3]=value
                String[] received = msg.split(" ");
                
//                System.out.println(received[0] +" "+ " "+ received[1] + " " + received[2] + " " + received[3]);

                if(_data.containsSensor(received[1], received[2])
                        && !received[3].equals(CommonVar.NULL)){

                    String room = _data.getRoom(received[1],received[2]);
                    Module module = _data.getModule(room, received[1]);
                    Details detailSensor = _data.getDetailSensor(room,received[1],received[2]);

                    //Set new sensor value - prueba[1] = value
                    String status = adaptValue(received[3],detailSensor.getType());
                    String value = received[3];

                    //Set Value column (Special features for analog sensors)
                    if((received[2].equals("AD0") || received[2].equals("AD1"))){

                            DecimalFormat df = new DecimalFormat(NUMBER_FORMAT);
                            value = df.format((Float.valueOf(received[3]) - 0.5) * 100.0).toString();
                    }

                    //True if the status is different and the system started to record the infomtion
                    if(_controller.setValueCell(room,detailSensor.getId(), value, status)){

                        String time = String.valueOf(System.currentTimeMillis());

                       //SQL query (timestamp,module,id,room,channel,value,status)
                       String sqlQuery = "INSERT INTO " + _table + " " + _columns + " VALUES " +
                                        "('" + time + "','"
                                             + module.getModuleName() + "','"
                                             + detailSensor.getId() + "','"
                                             + room + "','"
                                             + received[2] + "','"
                                             + detailSensor.getName() + "','"
                                             + value + "','"
                                             + status + "')";

                      //Insert the data into the MySQL local database           
                       _controller.sqlQueryInsert(sqlQuery);
                       //Call the activity recognizer with the new sensor triggered
                       _controller.recongintion(detailSensor.getName(), new Time(Long.valueOf(time)), status);

                    }
               }

               // Reset the length of the packet before reusing it.
               packet.setLength(buffer.length);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\nZigBee query error. Closing the application...");

             t.interrupt();  System.out.println("Process ZigBee stopped");
            _controller.closeDBs();
        }

    }

   
      /**
    * Change the original value sending by the sensor for an understandable one 
    * @param value Sensor's value
    * @param type Sensor's type
    * @return New value assigned to the sensor
    */
    private String adaptValue(String value, String type) {

        int filter_length = 5;

        if (!value.equals(CommonVar.NULL)){

            if (type.equals("CONTACT_REED")){
                
                if (value.equals("1"))  return CommonVar.ON;
                else  return CommonVar.OFF;
                
            }else if(type.equals("CONTACT_PRESSUREMAT")){
                
                if (value.equals("1")) return CommonVar.OFF;
                else return CommonVar.ON;
                
            }else if(type.equals("TEMPERATURE_MCP9700_HOT")){

                    Float temp = Float.valueOf(value);
                    
                    _auxHotWater.addLast(temp);
                    if(_auxHotWater.size() == filter_length) _auxHotWater.pop();
                    
                    float average = 0;

                    for(int i=0; i<_auxHotWater.size(); i++)
                        average+= (Float) _auxHotWater.get(i);

                    average = average/new Float(_auxHotWater.size());

                    if(!_statusHot && (temp > (1.01 *average))){
                        _statusHot = true;
                    }else if(_statusHot && (temp < average)) {
                        _statusHot = false;
                    }
  // System.out.println("Current: " +  temp + " Average:  " + average + " StatusHot: " + _statusHot);

                    if(_statusHot) return CommonVar.ON;
                    else return CommonVar.OFF;

            }else if(type.equals("TEMPERATURE_MCP9700_COLD")){

                    Float temp = Float.valueOf(value);

                    _auxColdWater.addLast(temp);
                    if(_auxColdWater.size()>filter_length) _auxColdWater.pop();

                    float average = 0;

                    for(int i=0; i<_auxColdWater.size(); i++)
                        average+= (Float) _auxColdWater.get(i);

                    average = average/new Float(_auxColdWater.size());
                    
                    if(!_statusCold && (temp < (0.99 * average))){
                        _statusCold = true;
                    }else if(_statusCold && (temp > average)) {
                        _statusCold= false; 
                    }
//System.out.println("Current: " +  temp + " Average:  " + average + " StatusCold: " + _statusCold);

                    if(_statusCold) return CommonVar.ON;
                    else return CommonVar.OFF;

            }else  return CommonVar.UNDEFINED;
            
        }else return CommonVar.UNDEFINED;

    }

    /**
     * Close the Database and the thread
     */
    public void close(){
        t.interrupt();
        System.out.println("Process ZigBee stopped");
    }
}
