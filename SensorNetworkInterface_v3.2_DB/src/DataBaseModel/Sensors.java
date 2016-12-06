/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseModel;

import DataBase.LocationsDB;
import DataBase.SensorTypeDB;
import java.sql.Timestamp;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class Sensors{

    int _sensorId;
    Float _value;
    int _locationId;
    String _name;
    int _sensorAccessPointID;
    String _sensorRule;
    String _channelDescriptor;
    int _sensorTypeId;
    Timestamp _lastUpdate;
    Timestamp _lastTimeActive;
    Float _lastActiveValue;
    String _status;



    /**
     * Constructor
     */
    public Sensors (){
        _sensorId = -1;
        _value= -999.0f;
        _locationId = -1;
        _name = "";
        _sensorAccessPointID = -1;
        _sensorRule = "";
        _channelDescriptor = "";
        _sensorTypeId = -1;
        _lastUpdate = new Timestamp(System.currentTimeMillis());
        _lastTimeActive = new Timestamp(System.currentTimeMillis());
        _lastActiveValue = -999.0f;
        _status = "";
    }

    /**
     * Constructor from data
     */
    public Sensors (int sensorId, Float value, int locationId, String name,
            int sensorAccessPointID, String sensorRule, String channelDescriptor,
            int sensorTypeId, Timestamp lastUpdate, Timestamp lastTimeActive, Float lastActiveValue,
            String status){
        
        _sensorId = sensorId;
        _value= value;
        _locationId = locationId;
        _name = name;
        _sensorAccessPointID = sensorAccessPointID;
        _sensorRule = sensorRule;
        _channelDescriptor = channelDescriptor;
        _sensorTypeId = sensorTypeId;
        _lastUpdate = lastUpdate;
        _lastTimeActive = lastTimeActive;
        _lastActiveValue = lastActiveValue;
        _status = status;
    }
    
    /**
     * Constructor from data
     */
    public Sensors (int sensorId, Float value, int locationId, String name,
            int sensorAccessPointID, String sensorRule, String channelDescriptor,
            int sensorTypeId, Timestamp lastUpdate, Timestamp lastTimeActive, Float lastActiveValue,
            String status, String sensorType, String locationName, String sensorTypeManufacture){

        this(sensorId, value, locationId, name,
            sensorAccessPointID, sensorRule, channelDescriptor,
            sensorTypeId, lastUpdate, lastTimeActive, lastActiveValue,
            status);

    }

    //Adds
    public void addSensorId(int value ){ _sensorId = value; }
    public void addValue (Float value) { _value=value; }
    public void addLocationId (int value) { _locationId = value; }
    public void addName (String value) { _name = value; }
    public void addSensorAccessPointID (int value) { _sensorAccessPointID = value; }
    public void addSensorRule (String value) { _sensorRule = value; }
    public void addChannelDescriptor(String value) { _channelDescriptor = value; }
    public void addSensorTypeId (int value) { _sensorTypeId = value; }
    public void addLastUpdate (Timestamp value) { _lastUpdate = value; }
    public void addLastTimeActive (Timestamp value) { _lastTimeActive = value; }
    public void addLastActiveValue (Float value) { _lastActiveValue = value; }
    public void addStatus (String status) { _status = status; }

    //Gets
    public Integer getSensorId(){ return _sensorId; }

    public Float getValue() { return _value; }

    public Integer getLocationId() { return _locationId; }

    public String getLocationNameById(Integer locationId){
        return LocationsDB.getLocationById(locationId).getName();
    }
    public String getName() { return _name; }

    public Integer getSensorAccessPointID () { return _sensorAccessPointID; }

    public String getSensorRule() { return _sensorRule; }

    public String getChannelDescriptor() { return _channelDescriptor; }

    public Integer getSensorTypeId() { return _sensorTypeId; }

    public Timestamp getLastUpdate() { return _lastUpdate; }

    public Timestamp getLastTimeActive() { return _lastTimeActive; }

    public Float getLastActiveValue() { return _lastActiveValue; }

    public String getStatus() { return _status; }

 }
