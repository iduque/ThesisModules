/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLReaders;

import SensorsModel.Details;
import SensorsModel.Module;
import SensorsModel.SensorsXMLModel;
import java.io.IOException;
import java.util.LinkedHashMap;
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
public class SensorsXMLReader {
    
    
    public SensorsXMLReader(){}
    
    /**
     * Parse the XML file
     * @return SensorsXMLModel - All the read data from the file
     */ 
    public SensorsXMLModel readXML(String file){
        
        SensorsXMLModel _data = new SensorsXMLModel();
        
        try {

            
            
            //Parse XML    
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize(); 
            Element n = doc.getDocumentElement(); 
            
            //For each Room find in the XML structure
            for(int i=0; i<n.getChildNodes().getLength(); i++){
                
                Node node = n.getChildNodes().item(i);
                 
                if (node.getNodeType()== Node.ELEMENT_NODE){ //ROOMS
                                   
                  // System.out.println("--->" + node.getNodeName() + " attrib=" + node.getAttributes().item(0).getNodeValue());

                  LinkedHashMap<String,Module> modules = new LinkedHashMap<String,Module>();
                   
                   for(int j=0; j<node.getChildNodes().getLength(); j++){ //ZigBee or GeoSys Branch
                       
                       
                       Node node1 = node.getChildNodes().item(j);
                       if (node1.getNodeType()== Node.ELEMENT_NODE){ //ZigBee or GeoSys Branch
                                                
                           LinkedHashMap<String, Details> sensors = new LinkedHashMap<String, Details>(); 
                            
                            for(int f=0; f<node1.getChildNodes().getLength(); f++){ //Sensors

                                Node node2 = node1.getChildNodes().item(f);
                                if (node2.getNodeType()== Node.ELEMENT_NODE){ //Channels or GeoSys IDs                                  
                                    
                                    Details details = new Details();
                                    
                                    int cont=0;
                                    for(int k=0; k<node2.getChildNodes().getLength();k++){

                                       Node node3 = node2.getChildNodes().item(k);
                                       if (node3.getNodeType()== Node.ELEMENT_NODE){
                                           
                                           switch (cont){
                                               case 0: details.addId(node3.getTextContent());
                                               case 1: details.addName(node3.getTextContent());
                                               case 2: details.addType(node3.getTextContent());
                                               case 3: details.addRule(node3.getTextContent());
                                           }
                                           cont++;
                                       }
                                    }

                                    //ZigBee Chanel (AD01/DI01) or GeoSystem ID in this format: 1 --> ID01
                                    sensors.put(node2.getNodeName(), details);
                                }
                            }
                            
                            modules.put(node1.getAttributes().item(0).getNodeValue(), //MAC
                                    new Module(node1.getNodeName(), //ZigBee or GeoSystem
                                    sensors)); //Sensors
                       } 
                   }  
                   
                   /** 
                    * Add each room and its modules to the struture
                    */              
                   _data.addKey(node.getNodeName(), modules);
                   
                }

            }
  
        } catch (SAXException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ParserConfigurationException ex) {
            System.out.println(ex.getMessage());
        } 
        
        return _data;
    }
    
}
