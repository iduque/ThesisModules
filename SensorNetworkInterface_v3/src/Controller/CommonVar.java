/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ismael
 */
public class CommonVar {

    public static final DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss.SSS");
    public static final DateFormat dateFormatFull = new SimpleDateFormat("dd.MM.yy HH:mm:ss.SSS");
    
    public static final String ON="on";
    public static final String OFF="off";
    public static final String UNDEFINED="Undefined";
    public static final String NULL="-";
    
    public static final String ACTIVATED="activated";
    public static final String DEACTIVATED="deactivated";
    public static final String ACTIVATED_DEACTIVATED="activated/deactivated";
    public static final String DEACTIVATED_NONACTIVATED="deactivated/nonactivated";
    

}
