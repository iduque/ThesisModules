/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author Ismael
 */
public class CommonVar {

    public static final DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss.SS");
    public static final DateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat dateFormatDB =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
    
    public static final String ADMIN="Admin";
    public static final String USER="User";
    public static final String INITIAL_VIEW="InitialView";
    public static final String PREFERENCES = "Preferences";
    
    public static final String QUESTIONNAIRES_FOLDER="Users/";
    
    public static final String TYPE_SINGLECHOICE="SingleChoice";
    public static final String TYPE_MULTIPLECHOICE="MultipleChoice";
    public static final String TYPE_TEXT="Text";
    
    public static final String QUESTIONNAIRE="Questionnaire.xml";
    public static final String PREFERENCES_QUESTIONNAIRE="PreferencesQuestionnaire.xml";
    
    public static final String PERSONA1="Personas/Questionnaire-Persona1-E2.xml";
    public static final String PERSONA2="Personas/Questionnaire-Persona2-E2.xml";
    
    public static final LinkedList<Integer> EXCLUDED_QUESTIONS = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 6, 8));
    public static final LinkedList<Integer> PERSONAS_QUESTIONS = new LinkedList<Integer>(Arrays.asList(14, 15, 16, 17));
    public static final LinkedList<Integer> EDUCATION = new LinkedList<Integer>(Arrays.asList(5));
    public static final LinkedList<Integer> NUM_HOUR_COMPUTER = new LinkedList<Integer>(Arrays.asList(7));
    public static final LinkedList<Integer> PERSONALITY = new LinkedList<Integer>(Arrays.asList(9));
    public static final LinkedList<Integer> PREV_EXPERIENCE_ROBOTS = new LinkedList<Integer>(Arrays.asList(10));
    public static final LinkedList<Integer> ATTITUDE_TOWARDS_ROBOT = new LinkedList<Integer>(Arrays.asList(11));
    public static final LinkedList<Integer> COMFORTABLENESS = new LinkedList<Integer>(Arrays.asList(12));
    public static final LinkedList<Integer> ROBOT_ROLE = new LinkedList<Integer>(Arrays.asList(13));
    public static final LinkedList<Integer> ASSISTANCE_LEVEL = new LinkedList<Integer>(Arrays.asList(18));
    
}
