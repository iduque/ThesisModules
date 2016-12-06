/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package readsensorlogs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

/**
 *
 * @author Ismael
 */
public class Main {

    private static int TIMESTAMP = 0;
    private static int SENSORID = 3;
    private static int SENSORVALUE = 7;
    public static final DateFormat dateFormatDB =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        if (args.length != 0 ){

            //create BufferedReader to read csv file
            BufferedReader br = new BufferedReader( new FileReader(args[0]));
            String line = "", sql = "";
            StringTokenizer st = null;
            boolean firstLine = true;
            Time previousTime = null, currentTime = null;
            String sensorId = null ,sensorValue = null;
            
            //DB connection ---------------------------------------------------------
            DataBase.connect();

            //avoid header - 3 lines
            br.readLine();br.readLine();br.readLine();
            
            //start to read the raw data
            while((line = br.readLine())!= null && !line.isEmpty()){
                //break comma separated line using ";"
                st = new StringTokenizer(line, ";");

                if(firstLine){
                    previousTime = currentTime = new Time(Long.valueOf(st.nextToken()));
                    firstLine = false;
                }else{
                    previousTime=currentTime;
                    currentTime = new Time(Long.valueOf(st.nextToken()));
                }
                st.nextToken(); st.nextToken(); //avoid formated date column and type sensor column

                try {
                    Thread.sleep(currentTime.getTime() - previousTime.getTime());
                } catch (InterruptedException ie) {
                    //Handle exception
                }

                sql += "VALUES('" + dateFormatDB.format(System.currentTimeMillis()) + "'," +
                st.nextToken() + ",'" + st.nextToken() + "',";
                st.nextToken(); //avoid channel descriptor column
                
                sql +=  "'" + st.nextToken() + "','" + st.nextToken() + "','" + st.nextToken() + "')";      

                System.out.println(sql);
                        
                // Execute query
                DataBase.sqlQueryInsert("INSERT INTO Accompany.SensorLog (timestamp, sensorId, room, channel, value, status) " + sql);

                //Close ResultSet
                DataBase.closeResultSet();
                
                sql = "";
            }

        }else{
            System.out.println("Example: java -jar ReadSensorLogs.jar [file]");
            System.out.println("Press Enter to finish...");
            System.in.read();
        }
    }

}
