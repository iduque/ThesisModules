/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseModel;

/**
 * Substructure. It is used by Module class
 * @author id11ab
 */
 public class UserPreferences{

    int _userId;
    String _newsWebsite;
    String _weatherWebsite;
    String _musicWebsite;
    String _recipeWebsite;
    String _light;
    String _sound;
    String _location;
    String _approach;
    int _voice;


    /**
     * Constructor
     */
    public UserPreferences (){
        _userId = -1;
        _newsWebsite = "";
        _weatherWebsite = "";
        _musicWebsite = "";
        _recipeWebsite = "";
        _light = "";
        _sound = "Sound 1";
        _location = "";
        _approach = "";
        _voice = 1;
    }

    /**
     * Constructor from data
     */
    public UserPreferences (int userId, String newsWebsite, String weatherWebsite,
            String musicWebsite, String recipeWebsite, String light, String sound,String location, 
            String approach, int voice){

        _userId = userId;
        _newsWebsite = newsWebsite;
        _weatherWebsite = weatherWebsite;
        _musicWebsite = musicWebsite;
        _recipeWebsite = "";
        _light = light;
        _sound = sound;
        _location = location;
        _approach = approach;
        _voice = voice;
    }

    
    //Setters
    
    public void setApproach(String _approach) {
        this._approach = _approach;
    }

    public void setLight(String _light) {
        this._light = _light;
    }

    public void setLocation(String _location) {
        this._location = _location;
    }

    public void setMusicWebsite(String _musicWebsite) {
        this._musicWebsite = _musicWebsite;
    }

    public void setNewsWebsite(String _newsWebsite) {
        this._newsWebsite = _newsWebsite;
    }

    public void setRecipeWebsite(String _recipeWebsite) {
        this._recipeWebsite = _recipeWebsite;
    }

    public void setSound(String _sound) {
        this._sound = _sound;
    }

    public void setUserId(int _userId) {
        this._userId = _userId;
    }

    public void setWeatherWebsite(String _weatherWebsite) {
        this._weatherWebsite = _weatherWebsite;
    }

    public void setVoice(int _voice) {
        this._voice = _voice;
    }
    //Getters
    
    public String getApproach() {
        return _approach;
    }

    public String getLight() {
        return _light;
    }

    public String getLocation() {
        return _location;
    }

    public String getMusicWebsite() {
        return _musicWebsite;
    }

    public String getNewsWebsite() {
        return _newsWebsite;
    }

    public String getRecipeWebsite() {
        return _recipeWebsite;
    }

    public String getSound() {
        return _sound;
    }

    public int getUserId() {
        return _userId;
    }

    public String getWeatherWebsite() {
        return _weatherWebsite;
    }

    public int getVoice() {
        return _voice;
    }

 }
