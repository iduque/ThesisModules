/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ActivitiesModel;

import java.sql.Time;

/**
 *
 * @author Ismael
 */
public class SensorProperties{

    private Time _time;
    private String _status;
    private Boolean _obligatory;
    private Double _weight;

    /**
     * Constructor
     */
    public SensorProperties(){}

    public SensorProperties(String status, Boolean obligatory, Double weight){
        _time = null;
        _status = status;
        _obligatory = obligatory;
        _weight = weight;
    }

    public SensorProperties(Time time, String status, Boolean obligatory, Double weight){
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

    public Time getTime(){ return _time; }

    public void setTime(String time){ _time = new Time(Long.valueOf(time)); }

    public Boolean getObligatory(){ return _obligatory; }

    public void setObligatory(Boolean obligatory) { _obligatory = obligatory; }

    public String getStatus(){ return _status; }

    public void setStatus(String status) { _status = status; }


}
