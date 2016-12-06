/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseModel;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class SensorType{

    int _sensorTypeId;
    String _sensorType;
    String _madeBy;

    /**
     * Constructor
     */
    public SensorType (){
        _sensorTypeId = -1;
        _sensorType = "";
        _madeBy = "";
    }

    /**
     * Constructor from data
     */
    public SensorType (int sensorTypeId, String sensorType, String madeBy){

        _sensorTypeId = sensorTypeId;
        _sensorType = sensorType;
        _madeBy = madeBy;

    }

    //Adds
    public void addSensorTypeId(int value ){ _sensorTypeId = value; }
    public void addSensorType (String value) { _sensorType = value; }
    public void addMadeBy(String value) { _madeBy = value; }
    
    //Gets
    public Integer getSensorTypeId() { return _sensorTypeId; }
    public String getSensorType() { return _sensorType; }
    public String getMadeBy() { return _madeBy; }

 }
