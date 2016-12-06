/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SensorsModel;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class Details{

    String _id;
    String _name;
    String _type;
    String _rule;

    /**
     * Constructor
     */
    public Details (){
        _id = "";
        _name = "";
        _type = "";
        _rule = "";
    }

    /**
     * Constructor from data
     */
    public Details (String id, String name, String type, String rule){
        _id = id;
        _name = name;
        _type = type;
        _rule = rule;
    }

    //Adds
    public void addId(String id ){ _id=id; }
    public void addName(String name) { _name=name; }
    public void addType(String type) { _type=type; }
    public void addRule(String rule) { _rule=rule; }
    
    //Gets
    public String getId(){ return _id; }
    public String getName() { return _name; }
    public String getType() { return _type; }
    public String getRule() { return _rule; }
    
 }
