/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseModel;

import java.util.Date;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class SensorLog{

    Date _timestamp;
    int _sensorId;
    String _room;
    String _channel;
    String _value;
    String _status;

    /**
     * Constructor
     */
    public SensorLog (){
        _timestamp = new Date();
        _sensorId = -1;
        _room = "";
        _channel = "";
        _value = "";
        _status = "";
    }

    /**
     * Constructor from data
     */
    public SensorLog (Date timestamp, int sensorId, String room, String channel,
            String value, String status){

        _timestamp = timestamp;
        _sensorId = sensorId;
        _room = room;
        _channel = channel;
        _value= value;
        _status = status;

    }

    //Adds
    public void addTimestamp(Date value) { _timestamp = value; }
    public void addSensorId(int value ){ _sensorId = value; }
    public void addRoom (String value) { _room = value; }
    public void addChannel (String value) { _channel = value; }
    public void addValue (String value) { _value = value; }
    public void addStatus (String value) { _status = value; }
    
    //Gets
    public Date getTimestamp() { return _timestamp; }
    public Integer getSensorId() { return _sensorId; }
    public String getRoom() { return _room; }
    public String getChannel() { return _channel; }
    public String getValue() { return _value; }
    public String getStatus() { return _status; }

 }
