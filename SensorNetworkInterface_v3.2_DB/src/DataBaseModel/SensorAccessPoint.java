/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseModel;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class SensorAccessPoint{

    int _sensorAccessPointId;
    int _locationId;
    String _MACAddress;


    /**
     * Constructor
     */
    public SensorAccessPoint (){
        _sensorAccessPointId = -1;
        _locationId = -1;
        _MACAddress = "";

    }

    /**
     * Constructor from data
     */
    public SensorAccessPoint (int sensorAccessPointId, int locationId, String MAC){
        
        _sensorAccessPointId = sensorAccessPointId;
        _locationId = locationId;
        _MACAddress = MAC;
    }

    //Adds
    public void addSensorAccessPointId(int value ){ _sensorAccessPointId = value; }
    public void addLocationId (int value) { _locationId = value; }
    public void addMACAddress (String value) { _MACAddress = value; }
    
    //Gets
    public Integer getSensorAccessPointId(){ return _sensorAccessPointId; }
    public Integer getLocationId() { return _locationId; }
    public String getMACAddress() { return _MACAddress; }
 }
