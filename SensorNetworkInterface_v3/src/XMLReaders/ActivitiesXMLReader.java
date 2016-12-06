/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLReaders;

import ActivitiesModel.ActivitiesXMLModel;
import ActivitiesModel.ActivityProperties;
import ActivitiesModel.ContextProperties;
import ActivitiesModel.SensorProperties;
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
public class ActivitiesXMLReader{
    
    ActivitiesXMLModel _data = new ActivitiesXMLModel();
    String _userName;

    /**
     * Constructor
     */
    public ActivitiesXMLReader(){
        _data = new ActivitiesXMLModel();
        _userName = "";
    }
    
    /**
     * Parse the XML file
     */ 
    public void readXML(String file){

        try {

            //Parse XML
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            Element n = doc.getDocumentElement();

            //For each Activity find in the XML structure
            for(int i=0; i<n.getChildNodes().getLength(); i++){

                Node node = n.getChildNodes().item(i);

                if (node.getNodeType()== Node.ELEMENT_NODE){ //Activities

                  ActivityProperties activity = new ActivityProperties();

                  //Each activity details
                   for(int j=0; j<node.getChildNodes().getLength(); j++){ //Activity

                       Node node1 = node.getChildNodes().item(j);
                       if (node1.getNodeType()== Node.ELEMENT_NODE) ////Activity
                           
                           if(node1.getNodeName().equals("Duration")){
                               if(!node1.getTextContent().equals("Nil"))
                                    activity.setDuration(Long.valueOf(node1.getTextContent()));
                               
                           }else  if (node1.getNodeName().equals("Location")){
                               if(!node1.getTextContent().equals("Nil"))
                                    activity.setLocation(node1.getTextContent());
                           
                           }else if (node1.getNodeName().equals("Contexts")){
                               
                               for(int k=0; k < node1.getChildNodes().getLength(); k++){ //Contexts

                                    Node node2 = node1.getChildNodes().item(k);
                                    
                                    if (node2.getNodeType()== Node.ELEMENT_NODE){////Context
                                        ContextProperties cp = new ContextProperties(
                                                Long.valueOf(node2.getAttributes().getNamedItem("Interval").getNodeValue()),
                                                node2.getAttributes().getNamedItem("Status").getNodeValue());
                                        activity.addContext(node2.getTextContent(),cp);
                                   }
                               }

                           }else if (node1.getNodeName().equals("Threshold")){
                               activity.setThreshold(Double.valueOf(node1.getTextContent()));
                           
                           }else  if (node1.getNodeName().equals("Sensors")){

                               for(int k=0; k < node1.getChildNodes().getLength(); k++){ //Sensors

                                    Node node2 = node1.getChildNodes().item(k);
                                    
                                    if (node2.getNodeType()== Node.ELEMENT_NODE){////Sensor

                                        activity.addSensor(node2.getTextContent(),
                                            new SensorProperties(
                                            node2.getAttributes().getNamedItem("Status").getNodeValue(),
                                            Boolean.valueOf(node2.getAttributes().getNamedItem("Obligatory").getNodeValue()),
                                            Double.valueOf(node2.getAttributes().getNamedItem("Weight").getNodeValue())));
                                    }
                               }
                               
                           }     
                   }

                   /**
                    * Add each activity and its sensors to the structure
                    */
                   _data.addActivity( node.getAttributes().getNamedItem("Name").getNodeValue(), activity);

                }
            }

        } catch (SAXException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ParserConfigurationException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public ActivitiesXMLModel getActivitiesData(){ return _data; }
    
    public String getUserName(){ return _userName; }

    
}
