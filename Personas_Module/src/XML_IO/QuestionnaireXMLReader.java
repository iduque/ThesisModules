/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XML_IO;

import Model.Answer;
import Model.Question;
import Model.Questionnaire;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author id11ab
 */
public class QuestionnaireXMLReader{
    
    /**
     * Singleton.
     */
    private static QuestionnaireXMLReader __singleton = new QuestionnaireXMLReader();

    /**
     * Constructor
     */
    public QuestionnaireXMLReader(){ }
    
    /**
     * Parse the XML file
     */ 
    public static Questionnaire readXML(String file, Boolean textValue){

        //Create the Questionnaire Model object and Question object
        Questionnaire data = new Questionnaire();
            
        try{
            
            //Parse XML
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            Element n = doc.getDocumentElement();

            data.setUser(n.getAttributes().getNamedItem("User").getNodeValue());
            
            //For each Question find in the XML structure
            for(int i=0; i<n.getChildNodes().getLength(); i++){
                
                Node node = n.getChildNodes().item(i);

                if (node.getNodeType()== Node.ELEMENT_NODE) //Question       
                    //Add each question and its information to the model
                    data.addQuestion(proccessNode(node, textValue));
            }    

        } catch (SAXException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ParserConfigurationException ex) {
            System.out.println(ex.getMessage());
        }
        
        return data; 
    }
    
    private static Question proccessNode(Node node, Boolean textValue){
       
       Question question =  new Question();
       
       //Set question attributes
       question.setId(Integer.valueOf(node.getAttributes().getNamedItem("Id").getNodeValue()));
       question.setTitle(node.getAttributes().getNamedItem("Title").getNodeValue());
       question.setType(node.getAttributes().getNamedItem("Type").getNodeValue());
       question.setRequired(Boolean.valueOf(node.getAttributes().getNamedItem("Required").getNodeValue()));
       question.setStadistics(Boolean.valueOf(node.getAttributes().getNamedItem("Statistics").getNodeValue()));
       
       int counter = 1;
       //Set answers or subquestion details
       for(int j=0; j<node.getChildNodes().getLength(); j++){ //Question nodes

           Node node1 = node.getChildNodes().item(j);
           if (node1.getNodeType()== Node.ELEMENT_NODE){ ////Subquestion or Value

               if(node1.getNodeName().equals("Subquestion")){

                       question.addSubquestion(proccessNode(node1, textValue));

               } else if (node1.getNodeName().equals("Text")) {
                   
                    if(textValue) //Value is a String (Preferences questionnaire)         
                        question.addQuestionAnswer(
                            new Answer(counter++, node1.getAttributes().getNamedItem("Value").getNodeValue()
                            ,node1.getTextContent(), 
                            Boolean.valueOf(node1.getAttributes().getNamedItem("Selected").getNodeValue()),
                            Boolean.valueOf(node1.getAttributes().getNamedItem("TextInput").getNodeValue())));
                    else //Value is a number
                        question.addQuestionAnswer(
                            new Answer(Integer.valueOf(node1.getAttributes().getNamedItem("Value").getNodeValue()), ""
                            ,node1.getTextContent(), 
                            Boolean.valueOf(node1.getAttributes().getNamedItem("Selected").getNodeValue()),
                            Boolean.valueOf(node1.getAttributes().getNamedItem("TextInput").getNodeValue())));
               }
           }
       }          

       return question;
    }
    
}
