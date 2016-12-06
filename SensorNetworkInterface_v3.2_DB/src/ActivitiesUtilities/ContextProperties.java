/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ActivitiesUtilities;

/**
 *
 * @author Ismael
 */
public class ContextProperties {

    private Long _interval;
    private String _status;

    /**
     * Constructor
     */
    public ContextProperties(){}

    public ContextProperties(Long interval, String status){
        _interval = interval;
        _status = status;
    }

    public ContextProperties(ContextProperties copy){
        _interval = copy._interval;
        _status = copy._status;
    }

    public ContextProperties copy(){
        return new ContextProperties(this);
    }

    public Long getTime(){ return _interval; }

    public void setTime(String time){ _interval = Long.valueOf(time); }

    public String getStatus(){ return _status; }

    public void setStatus(String status) { _status = status; }


}
