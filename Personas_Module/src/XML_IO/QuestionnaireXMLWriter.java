/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XML_IO;

import Model.Answer;
import Model.Question;
import Model.Questionnaire;
import java.io.File;
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
public class QuestionnaireXMLWriter{
    
    /**
     * Singleton.
     */
    private static QuestionnaireXMLWriter __singleton = new QuestionnaireXMLWriter();

    /**
     * Constructor
     */
    public QuestionnaireXMLWriter(){ }
    
    /**
     * Write as XML file the data structure
     * @param textValue Depending on the kind of questionnaire the answer value could be a string or integer
     */ 
    public static void writeXML(String fileRoute, Questionnaire data, boolean textValue){
        
        
            try {
 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Questionnaire");
		doc.appendChild(rootElement);
                
                // set attribute to user in root element
                Attr attr = doc.createAttribute("User");
                attr.setValue(data.getUser());
                rootElement.setAttributeNode(attr);
                
                for(Map.Entry<Integer, Question> entry : data.getAllQuestions().entrySet())

                    rootElement.appendChild(proccessNode(entry.getValue(), rootElement, doc, false, textValue));
 
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileRoute));
 
		// Output to console for testing
		//StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
            
    }
    
    private static Element proccessNode(Question q, Element rootElement, Document doc, Boolean subquestion, Boolean textValue){

        // Question elements
        Element questionElement;
        if (!subquestion) questionElement = doc.createElement("Question");
        else questionElement = doc.createElement("Subquestion");
        rootElement.appendChild(questionElement);
       
        // set attribute to question element
        Attr attr1 = doc.createAttribute("Id");
        attr1.setValue(q.getId().toString());
        questionElement.setAttributeNode(attr1);
        
        // set attribute to question element
        Attr attr2 = doc.createAttribute("Title");
        attr2.setValue(q.getTitle());
        questionElement.setAttributeNode(attr2);

        // set attribute to question element
        Attr attr3 = doc.createAttribute("Type");
        attr3.setValue(q.getType());
        questionElement.setAttributeNode(attr3);

        // set attribute to question element
        Attr attr4 = doc.createAttribute("Required");
        attr4.setValue(q.isRequired().toString());
        questionElement.setAttributeNode(attr4);
        
        // set attribute to question element
        Attr attr5 = doc.createAttribute("Statistics");
        attr5.setValue(q.stadistics().toString());
        questionElement.setAttributeNode(attr5);
        
        if(!q.getQuestionAnswers().isEmpty()){
            for(Answer a: q.getQuestionAnswers()){
                // text elements
		Element text = doc.createElement("Text");
		text.appendChild(doc.createTextNode(a.getText()));
		questionElement.appendChild(text);
                
                // set attribute to text element
                Attr attrT1 = doc.createAttribute("Value");
                if(textValue) attrT1.setValue(a.getTextValue().toString());
                else attrT1.setValue(a.getIntValue().toString());
                text.setAttributeNode(attrT1);
                
                // set attribute to text element
                Attr attrT2 = doc.createAttribute("Selected");
                attrT2.setValue(a.isSelected().toString());
                text.setAttributeNode(attrT2);

                // set attribute to text element
                Attr attrT3 = doc.createAttribute("TextInput");
                attrT3.setValue(a.isTextInput().toString());
                text.setAttributeNode(attrT3);

            }
        }
       
        if(q.hasSubquestions()){
            for(Map.Entry<Integer, Question> entry : q.getSubquestions().getAllQuestions().entrySet())
                questionElement.appendChild(proccessNode(entry.getValue(), questionElement, doc, true, textValue));
        }

        return questionElement;
    }
    
}
