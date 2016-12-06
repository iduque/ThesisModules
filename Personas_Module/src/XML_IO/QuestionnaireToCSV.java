/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XML_IO;

import Model.Answer;
import Model.Question;
import Model.Questionnaire;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;

/**
 *
 * @author id11ab
 */
public class QuestionnaireToCSV{
    
    /**
     * Singleton.
     */
    private static QuestionnaireToCSV __singleton = new QuestionnaireToCSV();

    private static final String columns[] = {"User", "Age", "Gender", "Education", "Field", "Hours", "Techologies",
                    "Extraverted", "Critical", "Dependable", "Anxious", "Open_New", "Reserved", "Sympathetic",
                    "Disorganized", "Calm", "Conventional", "Experience", "Attitude", "Approach", "Close", "Room",
                    "Friend", "Servant", "College", "Pet", "Tool", "TV", "Computer", "Reading", "Food", "Drink",
                    "Meal", "Laying", "Cleaing", "Doorbell", "Remainders", "Distance", "Direction", "Facing"};

    private static final String CHARACTER = ";";
    private static final String FORMAT = ".csv";

    /**
     * Constructor
     */
    public QuestionnaireToCSV(){ }
    
    /**
     * Write as CSV file the questionnarie data
     * @param fileRoute File name and destinatio
     * @param data List of questionnaires to be converted to csv format
     */ 
    public static void writeCSV(String fileRoute, List<Questionnaire> data){
        
            PrintWriter pw = null;

            try {
                File f = new File(fileRoute + ".csv");
                pw = new PrintWriter(f);
            } catch (FileNotFoundException ex) {
               System.out.println("Error al generar el fichero de resultados");
            }
            //Set columns
            for(String s: columns){
                pw.print(s);
                pw.print(CHARACTER);
            }
            pw.println();

            for(Questionnaire questionnarie: data){
                pw.print(questionnarie.getUser());
                pw.print(CHARACTER);

                for(Map.Entry<Integer, Question> entry : questionnarie.getAllQuestions().entrySet()){
                    Question q = entry.getValue();
                    if(q.getSubquestions().getNumberQuestions() > 0){
                        for(Map.Entry<Integer, Question> entrySubquestion : q.getSubquestions().getAllQuestions().entrySet()){
                            pw.print(entrySubquestion.getValue().getSelectedValue());
                            pw.print(CHARACTER);
                        }
                    }else{
                        String value = q.getSelectedText();
                        if(value.split(":").length > 1){ //"Hours:10 -> 10"
                            pw.print(value.split(":")[1]);
                        }else{
                            pw.print(value);
                        }
                        pw.print(CHARACTER);
                    }
                }

                pw.println();
            }

            pw.close();
    }
}
