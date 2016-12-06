/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Structure to store the activities read from the XML file
 * @author id11ab
 */   
  public class Questionnaires{
      
         //The key corresponds to the activity name
         // The ActivityModel object stores all the information related with the activity
         private LinkedHashMap<String, Questionnaire> _questionnaires;

         /**
          * Constructor
          */
         public Questionnaires(){
             _questionnaires = new LinkedHashMap<String, Questionnaire>();
         }


         /**
          * Add a new questionnaire to the list
          * @param q Questionnaire
          */
         public void addQuestionnaire(String user, Questionnaire q) {
             _questionnaires.put(user, q);
         }
         
         /**
          * Return the list of questionnaires and users
          * @return LinkedList<Question>
          */
         public LinkedHashMap<String, Questionnaire> getAllQuestions(){
             return _questionnaires;
         }
         
         /**
          * Return the questionnaire based on the user
          * @return Questionnaire
          */
         public Questionnaire getQuestionnaire(String user){
             return _questionnaires.get(user);
         }

          /**
          * Delete the selected questionnaire
          * @param user The user whose questionnaire wants to be deleted
          * @return Questionnaire
          */
          public Questionnaire removeQuestion(String user) {
            return _questionnaires.remove(user);
          }
          
          /**
           * Return true if a questionnaire is already in the list of questionnaires
           * @param qUser User key
           * @return Boolean
           */
          public Boolean containQuestionnaire(String qUser){
              return _questionnaires.containsKey(qUser);
          }
    }
