/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import DataBase.DataBase;
import DataBase.SessionControlDB;
import DataBase.UserPreferencesDB;
import DataBase.UsersDB;
import DataBaseModel.UserPreferences;
import DataBaseModel.Users;
import XML_IO.QuestionnaireXMLReader;
import Model.Questionnaire;
import Model.Questionnaires;
import Statistics.Statistics;
import View.Interface;
import java.io.File;
import java.util.LinkedList;
import view.InitialView;

/**
 *
 * @author Ismael
 */
public class Controller {

    private Users _user = new Users();
    private InitialView _initialView; 
    private Interface _interface;
    private Questionnaires _questionnaires;
    private Questionnaire persona1;
    private Questionnaire persona2;

    public Controller(){
        
        //DB connection ---------------------------------------------------------
        DataBase.connect();

         _initialView =  new InitialView(this);
         _interface = new Interface(this);
         _questionnaires = new Questionnaires();
         
         persona1 = QuestionnaireXMLReader.readXML(CommonVar.PERSONA1, false);
         persona2 = QuestionnaireXMLReader.readXML(CommonVar.PERSONA2, false);
    }
    
        
    public void setUserDB(Questionnaire preferences, Questionnaire q){

        //Check if the user already exists
        Users userAux = UsersDB.getUserByName(_user.getFirstName(), _user.getLastName());

        //Update DB - Preferences and User
        if(userAux != null){
            //Update user information from DB and match him with Persona
            _user = userAux;    _user.setPersonaId(this.matchPersona(q));
            UsersDB.updatePersonaIdByUserId(_user.getUserId(), _user.getPersonaId());
            UserPreferencesDB.updateUserPreferences(preferences.getUserPreferencesFromQuestionnaire(_user));
            //Set the new user as activate on the database
            SessionControlDB.setActiveUser(_user.getUserId());
        }else{
            //Update the Persona value and insert the new user in the DB
            _user.setPersonaId(this.matchPersona(q));
            UsersDB.insertUser(_user);
            //Set the user id (Autoincrement userID from the DB)
            _user.setUserId(UsersDB.getUserByName(_user.getFirstName(), _user.getLastName()).getUserId());
            UserPreferencesDB.insertUserPreferences(preferences.getUserPreferencesFromQuestionnaire(_user));
            //Set the new user as activate on the database
            SessionControlDB.setActiveUser(
                    UsersDB.getUserByName(_user.getFirstName(),_user.getLastName()).getUserId());
        }


    }

    public void setScreen(String screen, String firstName, String lastName, String nickname){
        
        if(screen.equals(CommonVar.ADMIN)){
            //View with all the questionnaires on the folder
            _interface.showInterface(this.retrieveQuestionnaires(), null, null, CommonVar.ADMIN);
        }else if (screen.equals(CommonVar.USER)){
            //Set user information
            _user.setFirstName(firstName); _user.setLastName(lastName); _user.setNickName(nickname);
            //Read questionnaire
            Questionnaire q = QuestionnaireXMLReader.readXML(CommonVar.QUESTIONNAIRE, false);
            q.setUser(firstName + " " + lastName);
            //Read preferences questionnaire
            Questionnaire prefQ = QuestionnaireXMLReader.readXML(CommonVar.PREFERENCES_QUESTIONNAIRE, true);
            prefQ.setUser(firstName + " " + lastName);
            //Show interface with the questionnaires
           _interface.showInterface(null, q, prefQ, CommonVar.USER);
        }else if (screen.equals(CommonVar.INITIAL_VIEW)){
            _initialView.setVisible(true);
        }
        
    }
    
    /**
     * Retrieve the list of questionnaires in the folder Users
     * @return Questionnaires object
     */
    public Questionnaires retrieveQuestionnaires(){
        
        File[] files = this.listFiles(CommonVar.QUESTIONNAIRES_FOLDER);
        if (_questionnaires.getAllQuestions().size() != files.length){
            for(File f: files){
               Questionnaire q = QuestionnaireXMLReader.readXML(CommonVar.QUESTIONNAIRES_FOLDER + f.getName(), false);
               if(!_questionnaires.containQuestionnaire(q.getUser()))
                   _questionnaires.addQuestionnaire(q.getUser(), q);
            }
        }
        
        return _questionnaires;
    }
               
   /**
   * Return a list of questionnaires in the passed folder
   * @param dir
   * @return 
   */
   private File[] listFiles(String dir){

     File folder = new File(dir);
     return folder.listFiles();

   }  
   
   
   public int matchPersona(Questionnaire q){
        
        //---------------Personality
//        persona1.getPersonalityQuesitions();
//        persona2.getPersonalityQuesitions();
//        q.getPersonalityQuesitions();
        System.out.println("User: " + q.getUser());

        System.out.println("--Personality--");
        
        this.executeAllTest(q.getPersonalityQuesitions(q.getUser()), persona1.getPersonalityQuesitions(persona1.getUser()));
        this.executeAllTest(q.getPersonalityQuesitions(q.getUser()), persona2.getPersonalityQuesitions(persona2.getUser()));
        
        System.out.println("--Previous Experience--");
        
        this.executeAllTest(q.getPrevExpRobotsQuesitions(), persona1.getPrevExpRobotsQuesitions());
        this.executeAllTest(q.getPrevExpRobotsQuesitions(), persona2.getPrevExpRobotsQuesitions());

        System.out.println("--Comfortableness--");

        this.executeAllTest(q.getComfortableness(), persona1.getComfortableness());
        this.executeAllTest(q.getComfortableness(), persona2.getComfortableness());
        
        return 1;
    }
   
   private void executeAllTest(LinkedList<Double> userValues, LinkedList<Double> persona){
        
        
        //Cosine Similarity Persona 1

        System.out.println("Cosine Similarity: " + 
                 Statistics.cosineSimilarity(userValues, persona) );
        
        System.out.println("Pearson Correlation: " + 
                 Statistics.pearsonCorrelation(userValues, persona) );

        System.out.println("Spearman Correlation: " +
                 Statistics.SpearmanCorrelation(userValues, persona) );
        
        //EuclideanDistance
//        System.out.println("Euclidena Distance Similarity Normalized (Persona1): " + 
//                Statistics.euclideanDistanceSimilarity(
//                                persona1.getQuestionnaireValues(persona1.getAllQuestions(), true),
//                                qNormalized)
//                           );
        
//        System.out.println("Euclidena Distance Similarity No Normalized: " +
//                Statistics.euclideanDistanceSimilarity(
//                                persona1.getQuestionnaireValues(persona1.getAllQuestions(), false),
//                                qNoNormalized)
//                           );
        
//        System.out.println("Euclidena Distance Similarity Normalized (Persona2): " + 
//                Statistics.euclideanDistanceSimilarity(
//                                persona2.getQuestionnaireValues(persona2.getAllQuestions(), true),
//                                qNormalized)
//                           );
        
//        System.out.println("Euclidena Distance Similarity No Normalized: " +
//                Statistics.euclideanDistanceSimilarity(
//                                persona2.getQuestionnaireValues(persona2.getAllQuestions(), false),
//                                qNoNormalized)
//                           );
        System.out.println("-------------------------------------------------------");
        
    }
   
   private void executeAllTest(Questionnaire q, Questionnaire persona){
        
        LinkedList<Double> qNormalized = q.getAllQuestionsValues(true);
        
        System.out.println("User: " + q.getUser());
        
        //Cosine Similarity Persona 1
        System.out.println("Cosine Similarity Normalized: " + 
                 Statistics.cosineSimilarity(
                    persona.getAllQuestionsValues(true),
                    qNormalized)
                            );
        
        System.out.println("Pearson Correlation: " + 
                 Statistics.pearsonCorrelation(
                    persona.getAllQuestionsValues( true),
                    qNormalized)
                            );

        System.out.println("Spearman Correlation: " +
                 Statistics.SpearmanCorrelation(
                    persona.getAllQuestionsValues(true),
                    qNormalized)
                            );
        
        //EuclideanDistance
//        System.out.println("Euclidena Distance Similarity Normalized (Persona1): " + 
//                Statistics.euclideanDistanceSimilarity(
//                                persona1.getQuestionnaireValues(persona1.getAllQuestions(), true),
//                                qNormalized)
//                           );
        
//        System.out.println("Euclidena Distance Similarity No Normalized: " +
//                Statistics.euclideanDistanceSimilarity(
//                                persona1.getQuestionnaireValues(persona1.getAllQuestions(), false),
//                                qNoNormalized)
//                           );
        
//        System.out.println("Euclidena Distance Similarity Normalized (Persona2): " + 
//                Statistics.euclideanDistanceSimilarity(
//                                persona2.getQuestionnaireValues(persona2.getAllQuestions(), true),
//                                qNormalized)
//                           );
        
//        System.out.println("Euclidena Distance Similarity No Normalized: " +
//                Statistics.euclideanDistanceSimilarity(
//                                persona2.getQuestionnaireValues(persona2.getAllQuestions(), false),
//                                qNoNormalized)
//                           );
        System.out.println("-------------------------------------------------------");
        
    }

}
