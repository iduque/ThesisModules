/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ismael
 */
public class CommonVar {

    public static final DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss.SS");
    public static final DateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat dateFormatDB =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
    
    public static final String ON="on";
    public static final String OFF="off";
    public static final String OPEN = "Open";
    public static final String OCCUPIED = "Occupied";
    public static final String UNDEFINED="Undefined";
    public static final String NULL="-";
    
    public static final String ACTIVATED="activated";
    public static final String NONACTIVATED="nonactivated";
    public static final String DEACTIVATED="deactivated";
    public static final String ACTIVATED_DEACTIVATED="activated/deactivated";
    public static final String DEACTIVATED_NONACTIVATED="deactivated/nonactivated";
    public static final String ACTIVATED_NONACTIVATED="activated/nonactivated";

    

}
