/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseModel;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class Users{

    int _userId;
    String _firstName;
    String _lastName;
    String _nickName;
    int _personaId;


    /**
     * Constructor
     */
    public Users (){
        _userId = -1;
        _firstName = "";
        _lastName = "";
        _nickName = "";
        _personaId = -1;
    }

    /**
     * Constructor from data
     */
    public Users (int userId, String firstName, String lastName, String nickName, int personaId){
        _userId = userId;
        _firstName = firstName;
        _lastName = lastName;
        _nickName = nickName;
        _personaId = personaId;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public String getNickName() {
        return _nickName;
    }

    public int getPersonaId() {
        return _personaId;
    }

    public int getUserId() {
        return _userId;
    }

    public void setFirstName(String _firstName) {
        this._firstName = _firstName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }

    public void setNickName(String _nickName) {
        this._nickName = _nickName;
    }

    public void setPersonaId(int _personaId) {
        this._personaId = _personaId;
    }

    public void setUserId(int _userId) {
        this._userId = _userId;
    }

    
 }
