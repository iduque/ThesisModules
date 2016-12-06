/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author id11ab
 */
public class Answer {
    
    private Integer _intValue;
    private String _textValue;
    private String _text;
    private Boolean _selected;
    private Boolean _textInput;
    
    
    public Answer(){
        _intValue = -999;
        _textValue = "";
        _text = "";
        _selected = false;
        _textInput = false;
    }
    /**
     * Contructor
     * @param value Answer integer value
     * @param text Answer text
     * @param selected If answer has been selected
     * @param textInput If the user is able to introduce some extra data
     */
    public Answer(Integer intValue, String textValue, String text, Boolean selected, Boolean textInput){
        _intValue = intValue;
        _textValue = textValue;
        _text = text;
        _selected = selected;
        _textInput = textInput;
    }
    
    public Integer getIntValue() { 
        return _intValue; 
    }
    
    public void setIntValue(Integer value) { 
        _intValue = value; 
    }
    
    public String getTextValue() { 
        return _textValue; 
    }
    
    public void setTextValue(String value) { 
        _textValue = value; 
    }
    
    public String getText() { 
        return _text; 
    }
    
    public void setText(String text) { 
        _text = text; 
    }
    
    public Boolean isSelected() { 
        return _selected; 
    }    
    
    public void setSelected(Boolean selected){
        _selected = selected;
    }
    
    public Boolean isTextInput() { 
        return _textInput; 
    }    
    
}
