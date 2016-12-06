/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActivityRecognition;

import ActivitiesUtilities.ActivitiesXMLModel;
import ActivitiesUtilities.ActivityProperties;
import ActivitiesUtilities.SensorProperties;
import Main.CommonVar;
import Main.Controller;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author id11ab
 */
public class ActivityRecognition extends Thread {

    private final Controller _controller;

    private final String _numberFormat;

    //Actitivy Recognizer
    private final ActRecognitionAlgorithm _recognizer;

    //Activated sensor data
    private String _activatedSensorName;
    private SensorProperties _activatedSensorProperties;
    
    //Constructor
    public ActivityRecognition(Controller c, ActivitiesXMLModel activities, String nf){

        _controller = c;
        _numberFormat = nf;
        _recognizer = new ActRecognitionAlgorithm(activities);
        
        //Intialize variables
        _activatedSensorName = "";
        _activatedSensorProperties = new SensorProperties();

        //t = new Thread(this, "Activity Recognition");
    }                

    public void insertSensor(String sensor, SensorProperties sensorProperties) {
        _activatedSensorName = sensor;
        _activatedSensorProperties = sensorProperties.copy();
    }

    @Override
    public synchronized void run() {

        //Boolean pastActivitiesChanges =
        _recognizer.runAlgorithm(_activatedSensorName,_activatedSensorProperties);
        _controller.writeLogsPreviousActivities(_recognizer.getLogs());
        
        String currentActivities = "";
        String activitiesHistory = "";
        for(Map.Entry<String, ActivityProperties> entry:_recognizer.getActivatedActivities().getActivities().entrySet()){
            String activity = entry.getKey();
            currentActivities += "==> " + activity + "\n";
            //Update Activities History
            _controller.writeLatestActivities(entry.getValue().getStartTime(), activity);
        }

        _controller.writeLogsCurrentActivities(currentActivities);
   }
    
}
