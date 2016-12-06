/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import DataBaseModel.UserPreferences;
import DataBaseModel.Users;
import Main.CommonVar;
import Statistics.Statistics;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Structure to store the activities read from the XML file
 * @author id11ab
 */   
  public class Questionnaire{
      
         private String _user;
      
         //The key corresponds to the activity name
         // The ActivityModel object stores all the information related with the activity
         private LinkedHashMap<Integer, Question> _questions;

         /**
          * Constructor
          */
         public Questionnaire(){
             _user = "";
             _questions = new LinkedHashMap<Integer, Question>();
         }

         /**
          * Constructor
          */
         public Questionnaire(Questionnaire m){
             _user = m.getUser();
             _questions = m.getAllQuestions();
         }

         /**
          * Add a new question to the questionnaire
          * @param q Question
          */
         public void addQuestion(Question q) {
             _questions.put(q.getId(), q);
         }
         
         /**
          * Questionnaire's user
          * @return String
          */
         public String getUser(){
             return _user;
         }
         
         /**
          * Set the questionnaire's user
          * @param user String
          */
         public void setUser(String user){
             _user = user;
         }
         
         /**
          * Return the questions list
          * @return  HashMap<Integer, Question>>
          */
         public  LinkedHashMap<Integer, Question> getAllQuestions(){
             return _questions;
         }

          /**
          * Return the number of questions on the list
          * @return Integer
          */
         public int getNumberQuestions(){
             return _questions.size();
         }

          /**
          * Delete the selected question
          * @param index Index of the question to be removed
          * @return Question
          */
          public Question removeQuestion(int index) {
            return _questions.remove(index);
          }
          /**
           * Get the question with the indicated id
           * @param id Question ID
           * @return Question
           */
          public Question getQuestionById(int id){
              return _questions.get(id);
          }
          
          public LinkedList<Double> getPersonalityQuesitions(String user){
              LinkedList<Double> personalityValues = this.getRangeQuesitionsValue(CommonVar.PERSONALITY, false);
              return Statistics.tipi(personalityValues, user);
          }
          
          public LinkedList<Double> getPrevExpRobotsQuesitions(){
              LinkedList<Double> results = this.getRangeQuesitionsValue(CommonVar.PREV_EXPERIENCE_ROBOTS, false);
              printVector(results);
              return results;
          }
          
          public LinkedList<Double> getAttitudeTowardsRobotsQuesitions(){
              return this.getRangeQuesitionsValue(CommonVar.ATTITUDE_TOWARDS_ROBOT, true);
          }
          
          public LinkedList<Double> getComfortableness(){
              LinkedList<Double> results = this.getRangeQuesitionsValue(CommonVar.COMFORTABLENESS, false);
              printVector(results);
              return results;
          }
          
          public LinkedList<Double> getRobotRoleQuesitions(){
              return this.getRangeQuesitionsValue(CommonVar.ROBOT_ROLE, true);
          }
          
          public LinkedList<Double> getAssistanceLevelQuesitions(){
              return this.getRangeQuesitionsValue(CommonVar.ASSISTANCE_LEVEL, true);
          }


          public LinkedList<Double> getRangeQuesitionsValue(LinkedList<Integer> idList, boolean normalization){
              return this.getQuestionsValues(idList, normalization);
          }

          /**
           * Get a vector of double with the value of each answer (optional:normalized)
           * @param data Questionnaire
           * @param normalization If we wish a normalized vector
           * @return Vector of values
           */
          public LinkedList<Double> getAllQuestionsValues(boolean normalization){
              return this.getQuestionsValues(null, normalization);
           }

          /**
           * Get a vector of double with the value of each answer selected (optional:normalized)
           * @param idList List of questions that have to be consider
           * @param normalization If we wish a normalized vector
           * @return Vector of values
           */
          private LinkedList<Double> getQuestionsValues(LinkedList<Integer> idList, boolean normalization){

                LinkedList<Double> result = new LinkedList<Double>();

                for(Map.Entry<Integer, Question> entry : _questions.entrySet()){
                     Question q = entry.getValue();
                    //If the question is taken into account for stadistics
                    if(q.stadistics() && (idList == null || idList.contains(q.getId()))){
                        //Question's answers
                        if(!q.getQuestionAnswers().isEmpty()){
                            for(Answer a: q.getQuestionAnswers()){
                                if(a.isSelected() && normalization)
                                    result.add(Double.valueOf(a.getIntValue())/Double.valueOf(q.getQuestionAnswers().size()));
                                else if (a.isSelected()) result.add(a.getIntValue().doubleValue());
                            }
                        }
                        //Question's subquestions
                        if(q.getSubquestions().getNumberQuestions() > 0){
                            for(Double value: q.getSubquestions().getQuestionsValues(null, normalization))
                                result.add(value);
                        }
                    }
                }

                return result;
           }

          /**
           * 
           * @param q
           * @param user
           * @return
           */
           public UserPreferences getUserPreferencesFromQuestionnaire(Users user){

               UserPreferences up =  new UserPreferences();

               up.setUserId(user.getUserId());
               up.setLocation(""); //User will select this option throw the interface
               up.setApproach(""); //User will select this option throw the interface

               //1 - Weather    5 - Light
               //2 - News       6 - Sound
               //3 - Music      6 - Voice
               //4 - Recipes
               for(Map.Entry<Integer, Question> entry : _questions.entrySet()){
                   Question question = entry.getValue();
                   switch (question.getId()){
                       case 1: up.setWeatherWebsite(question.getSelectedTextValue());
                            break;
                       case 2: up.setNewsWebsite(question.getSelectedTextValue());
                           break;
                       case 3: up.setMusicWebsite(question.getSelectedTextValue());
                           break;
                       case 4: up.setRecipeWebsite(question.getSelectedTextValue());
                            break;
                       case 5: up.setLight(question.getSelectedTextValue());
                           break;
//                       case 6: up.setSound(question.getValueSelectedString());
//                           break;
                       case 6: up.setVoice(question.getSelectedValue());
                           break;
                   }

                }

                return up;
           }


       private void printVector(LinkedList<Double> list){
            for (Double value : list) {
                 System.out.print(value + " - ");
            }
            System.out.println( "" );
        }
    }
