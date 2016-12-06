/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import DataBaseModel.SensorLog;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author Ismael
 */
public class LogsWriter {

    public static void writeSensorLogsToFile(String user, Date time, String path, Collection<SensorLog> logs){
        PrintWriter pw = null;

        try {
            File file = new File(path);
            if(file.exists())
                pw = new PrintWriter(new StringBuffer(path).insert(path.length()-4, "-" + System.currentTimeMillis()).toString());
            else pw = new PrintWriter(path);

            pw.println("Participant Name: " + user);
            pw.println("Starting Time: " + CommonVar.dateFormatDB.format(time) );
            pw.println();
            
            Iterator it = logs.iterator();
            while(it.hasNext()){
                SensorLog o = (SensorLog) it.next();
                pw.println(CommonVar.dateFormatDB.format(o.getTimestamp()) + ";" + o.getSensorId() + ";" 
                        + o.getChannel()+ ";" + o.getValue() + ";" + o.getStatus());
            }

            pw.println();
            pw.println("Ending Time: " + CommonVar.dateFormatDB.format(new Date(System.currentTimeMillis())) );

        } catch (Exception ex) {
           ex.printStackTrace();
           System.out.println("I/O error. Logs Writer method");
        } finally {
            pw.close();
        }
    }
    
    public static void writeLogsToFile(String user, Date time, String path, String logs){
        PrintWriter pw = null;

        try {
            File file = new File(path);
            if(file.exists())
                pw = new PrintWriter(new StringBuffer(path).insert(path.length()-4, "-" + System.currentTimeMillis()).toString());
            else pw = new PrintWriter(path);
            
            pw.println("Participant Name: " + user);
            pw.println("Starting Time: " + CommonVar.dateFormatDB.format(time) );
            pw.println();

            pw.println(logs);
            
            pw.println();
            pw.println("Ending Time: " + CommonVar.dateFormatDB.format(new Date()) );
            
        } catch (Exception ex) {
           System.out.println("I/O error. Logs Writer method");
        } finally {
            pw.close();
        }
    }

}
