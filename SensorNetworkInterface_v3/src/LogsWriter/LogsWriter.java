/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package LogsWriter;

import Controller.CommonVar;
import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Ismael
 */
public class LogsWriter {

    public static void writeSQLLogsFile(String user, Time time, String path, ResultSet rs, String tableColumns){
        PrintWriter pw = null;
        String columns[] = tableColumns.subSequence(1, tableColumns.length()-1).toString().split(",");

        try {
            File file = new File(path);
            if(file.exists())
                pw = new PrintWriter(new StringBuffer(path).insert(path.length()-4, "-" + System.currentTimeMillis()).toString());
            else pw = new PrintWriter(path);
            
            pw.println("Participant Name: " + user);
            pw.println("Starting Time: " + CommonVar.dateFormatFull.format(new Date(time.getTime())) );
            pw.println();

            while(rs.next()){
                for(int i=0; i<columns.length; i++){
                    if(i == 0){
                        pw.print(rs.getString(columns[i]) + ";");
                        pw.print(CommonVar.dateFormatFull.format(new Date(Long.valueOf(rs.getString(columns[i])))) + ";");
                    }else if (i == (columns.length-1)) pw.println(rs.getString(columns[i]));
                    else pw.print(rs.getString(columns[i]) + ";");
                }
            }
            
            pw.println();
            pw.println("Ending Time: " + CommonVar.dateFormatFull.format(new Date(System.currentTimeMillis())) );
            
        } catch (Exception ex) {
           System.out.println("I/O error. Logs Writer method");
        } finally {
            pw.close();
        }
    }
    
    public static void writeLogsFile(String user, Time time, String path, String logs){
        PrintWriter pw = null;

        try {
            File file = new File(path);
            if(file.exists())
                pw = new PrintWriter(new StringBuffer(path).insert(path.length()-4, "-" + System.currentTimeMillis()).toString());
            else pw = new PrintWriter(path);
            
            pw.println("Participant Name: " + user);
            pw.println("Starting Time: " + CommonVar.dateFormatFull.format(new Date(time.getTime())) );
            pw.println();

            pw.println(logs);
            
            pw.println();
            pw.println("Ending Time: " + CommonVar.dateFormatFull.format(new Date(System.currentTimeMillis())) );
            
        } catch (Exception ex) {
           System.out.println("I/O error. Logs Writer method");
        } finally {
            pw.close();
        }
    }

}
