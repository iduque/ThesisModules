/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ActivitiesUtilities;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Ismael
 */
public class SensorProperties{

    private Date _time;
    private String _status;
    private Boolean _obligatory;
    private Double _weight;

    /**
     * Constructor
     */
    public SensorProperties(){}

    public SensorProperties(String status, Boolean obligatory, Double weight){
        _time = new Date();
        _status = status;
        _obligatory = obligatory;
        _weight = weight;
    }

    public SensorProperties(Date time, String status, Boolean obligatory, Double weight){
        _time = time;
        _status = status;
        _obligatory = obligatory;
        _weight = weight;
    }

    public SensorProperties(SensorProperties copy){
        _time = copy._time;
        _status = copy._status;
        _obligatory = copy._obligatory;
        _weight = copy._weight;
    }

    public SensorProperties copy(){
        return new SensorProperties(this);
    }
    
    public Double getWeight(){ return _weight; }

    public void setWeight(Double weight){ _weight = weight; }

    public Date getTime(){ return _time; }

    public void setTime(Date time){ _time = time; }

    public Boolean getObligatory(){ return _obligatory; }

    public void setObligatory(Boolean obligatory) { _obligatory = obligatory; }

    public String getStatus(){ return _status; }

    public void setStatus(String status) { _status = status; }


}
