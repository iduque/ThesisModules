/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseModel;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class Locations{

    int _locationId;
    String _name;
    int _where;
    int _xCoord;
    int _yCoord;
    int _validRobotLocation;
    int _validUserLocation;


    /**
     * Constructor
     */
    public Locations (){
        _locationId = -1;
        _name = "";
        _where = -1;
        _xCoord = -1;
        _yCoord = -1;
        _validRobotLocation = -1;
        _validUserLocation = -1;
    }

    /**
     * Constructor from data
     */
    public Locations (int locationId, String name, int where, int xCoord, int yCoord,
            int validRobotLocation, int validUserLocation){
        
        _locationId = locationId;
        _name = name;
        _where = where;
        _xCoord = xCoord;
        _yCoord = yCoord;
        _validRobotLocation = validRobotLocation;
        _validUserLocation = validUserLocation;
    }

    //Adds
    public void addLocationId (int value) { _locationId = value; }
    public void addName (String value) { _name = value; }
    public void addWhere (int value) { _where = value; }
    public void addXCoord (int value) { _xCoord = value; }
    public void addYCoord(int value) { _yCoord = value; }
    public void addValidRobotLocation (int value) { _validRobotLocation = value; }
    public void addValidUserLocation (int value) { _validUserLocation = value; }
    
    //Gets
    public Integer getLocationId() { return _locationId; }
    public String getName() { return _name; }
    public Integer getWhere () { return _where; }
    public Integer getXCoord() { return _xCoord; }
    public Integer getYCoord() { return _yCoord; }
    public Integer getValidRobotLocation() { return _validRobotLocation; }
    public Integer getValidUserLocation() { return _validUserLocation; }

 }
