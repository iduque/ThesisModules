/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.LinkedList;

/**
 * Substructure. It is used by SensorsXMLModel class
 * Represent all the sensor associated to one MAC
 * @author id11ab
 */
public class Question{
    
    private Integer _id; //Question ID
    private String _title; //Question title
    private String _type; //Type of question
    private Boolean _required; //Required question
    private LinkedList<Answer> _answers; //List of possible answers
    private Questionnaire _subquestions; //Question inside another question (same structure)
    private Boolean _stadistics; //Flag to know if a question is used for stadistics or not
     

    /**
     * Constructor
     */
    public Question(){
        _id = -999;
        _title = "";
        _type = "";
        _required = false;
        _answers = new LinkedList<Answer>();
        _subquestions = new Questionnaire();
        _stadistics = true;
    }

    /**
     * Contructor
     * @param id Question ID
     * @param text Question title
     * @param type Question type
     * @param values List of possible answers
     * @param subquestion Question that represent several subquestions
     */
    public Question(Integer id, String text, String type, Boolean required,
            LinkedList<Answer> answers, Questionnaire subquestions, Boolean stadistics){
        _id = id;
        _title = text;
        _type = type;
        _required = required;
        _answers = answers;
        _subquestions = subquestions;
        _stadistics = stadistics;

    }
    
    public Integer getId() { 
        return _id; 
    }
    
    public void setId(Integer id) { 
        _id = id; 
    }
    
    public String getTitle() { 
        return _title; 
    }
    
    public void setTitle(String title) { 
        _title = title; 
    }
    
    public String getType() { 
        return _type; 
    }
    
    public void setType(String type) { 
        _type = type; 
    }

    public Boolean isRequired() {
        return _required;
    }

    public void setRequired(Boolean required) {
        _required = required;
    }

    public LinkedList<Answer> getQuestionAnswers() {
        return _answers;
    }

    public void setQuestionAnswers(LinkedList<Answer> questionAnswers) {
        this._answers = questionAnswers;
    }
    
    public void addQuestionAnswer(Answer a) {
        this._answers.add(a);
    }
    
    public Answer getAnswerByValue(Integer value){
        for(Answer a: _answers)
            if(a.getIntValue() ==  value) return a;
        
        return new Answer();
    }
    
    public Questionnaire getSubquestions() {
        return _subquestions;
    }

    public boolean hasSubquestions(){
        return _subquestions.getNumberQuestions() > 0;
    }

    public void setSubquestions(Questionnaire subquestions) {
        this._subquestions = subquestions;
    }

    public void setStadistics(Boolean b){
        this._stadistics = b;
    }
    
    public Boolean stadistics(){
        return this._stadistics;
    }
    
    public void addSubquestion(Question question) {
        this._subquestions.addQuestion(question);
    }
    
    public Question getSubquestionById(Integer id){
        return _subquestions.getQuestionById(id);
    }
    
    public int getNumberSubquestions(){
        return this._subquestions.getNumberQuestions();
    }
    
    public int getNumberValues(){
        return this._answers.size();
    }

    public String getSelectedText(){
        for(Answer a: this._answers){
            if (a.isSelected()) return a.getText();
        }
        return "";
    }

    public String getSelectedTextValue(){
        for(Answer a: this._answers){
            if (a.isSelected()) return a.getTextValue();
        }
        return "";
    }

    public int getSelectedValue(){
        for(Answer a: this._answers){
            if (a.isSelected()) return a.getIntValue();
        }
        return -1;
    }

 }
