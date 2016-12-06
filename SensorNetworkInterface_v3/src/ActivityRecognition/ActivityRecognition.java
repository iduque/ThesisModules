/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActivityRecognition;

import ActivitiesModel.ActivitiesXMLModel;
import ActivitiesModel.SensorProperties;
import Controller.Controller;
import java.text.DecimalFormat;
import java.util.Iterator;

/**
 *
 * @author id11ab
 */
public class ActivityRecognition implements Runnable {

    private final Thread t;

    private final Controller _controller;

    private final String _numberFormat;

    //Actitivy Recognizer
    private final ActRecognitionAlgorithm _recognizer;

    //Past activities counter. We are avoiding to repeat previous values
    private int counterPastActivities = 0;

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

        t = new Thread(this,"ActivityRecognition");
    }                

    public void insertSensor(String sensor, SensorProperties sensorProperties) {
        _activatedSensorName = sensor;
        _activatedSensorProperties = sensorProperties.copy();
        t.run();
    }

    @Override
    public void run() {

        //Boolean pastActivitiesChanges =
        String logs = _recognizer.runAlgorithm(_activatedSensorName,_activatedSensorProperties);

        String currentActivities = "";
        Iterator it = _recognizer.getActivatedActivities().getActivities().keySet().iterator();
        while(it.hasNext()){

            String activity = (String) it.next();
            DecimalFormat df = new DecimalFormat(_numberFormat);
            currentActivities += activity + " " + df.format(
                    _recognizer.getActivatedActivities().getActivityProperties(activity).getThreshold())
                    + "\n";
        }
        _controller.writeLogsCurrentActivities(currentActivities);
        
        //if(pastActivitiesChanges){
//            String previousActivities = "";
//            for(int i = counterPastActivities; i< _recognizer.getPastActivitiesKeys().size(); i++){
//                String activity =  _recognizer.getPastActivitiesKeys().get(i);
//                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
//                java.util.Date date0 = new java.util.Date(
//                        _recognizer.getPastActivitiesActivityProperties().get(i).getStartTime().getTime());
//                java.util.Date date1 = new java.util.Date(
//                        _recognizer.getPastActivitiesActivityProperties().get(i).getEndTime().getTime());
//                previousActivities += activity +
//                        "\n    Time: " + dateFormat.format(date0) + " to " + dateFormat.format(date1) + "\n";
//                counterPastActivities++;
//            }
            _controller.writeLogsPreviousActivities(logs);
        //}
   }
    
}
