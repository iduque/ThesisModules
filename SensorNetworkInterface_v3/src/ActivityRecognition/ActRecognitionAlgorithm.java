/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ActivityRecognition;

import ActivitiesModel.ActivitiesXMLModel;
import ActivitiesModel.ActivityProperties;
import ActivitiesModel.ContextProperties;
import ActivitiesModel.SensorProperties;
import Controller.CommonVar;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author Ismael
 */
public class ActRecognitionAlgorithm {
    
    //ActivitiesXMLModel _pastActivities; //Activities history (Recognized, activated and finished
    private LinkedList<String> _pastActivitiesKeys;
    private LinkedList<ActivityProperties> _pastActivitiesActivityProperties;

    private final ActivitiesXMLModel _activitiesOriginalData; //Original set of activities and sensor involved

    private ActivitiesXMLModel _activatedActivities; //Currently recognized activities (They fulfill all the conditions)

    private ActivitiesXMLModel _nonActivatedActivities; //Non-recognized activities, but with some of theirs sensor activated

    private String _logs; //Variable in which the logs are recorded 
    /**
     *
     * @param activitiesData
     */
    public ActRecognitionAlgorithm(ActivitiesXMLModel activitiesData){
       _activitiesOriginalData = activitiesData;
       _activatedActivities = new ActivitiesXMLModel();
       _nonActivatedActivities = new ActivitiesXMLModel();
       _pastActivitiesKeys = new LinkedList<String>();
       _pastActivitiesActivityProperties = new LinkedList<ActivityProperties>();
       _logs = "";
    }

    /**
     *
     * @param activatedSensors
     * @return Return True if some pastActivities has suffered some change
     */
    public String runAlgorithm(String sensor,SensorProperties sensorProp){

        _logs = "";
                
java.util.Date auxDate = new java.util.Date(sensorProp.getTime().getTime());
_logs += "------------------------------------------------------------\n";            
_logs += "Sensor: "+ sensor + " - Status: "+sensorProp.getStatus() + " - Time: " + CommonVar.dateFormatTime.format(auxDate) + "\n";

        //Check if the sensor is involved in some of the defined activities
        Iterator itOriginalActivities = _activitiesOriginalData.getActivities().keySet().iterator();
        while(itOriginalActivities.hasNext()){

            String activity = (String) itOriginalActivities.next(); //Activity name
            ActivityProperties originalActivityProperties = _activitiesOriginalData.getActivityProperties(activity).copy();

//Check activity duration in either ActivatedActivities or NonActivated Activity
            if(_activatedActivities.getActivities().containsKey(activity))
                activityOverDuration(activity,true);

            else if(_nonActivatedActivities.getActivities().containsKey(activity))
                activityOverDuration(activity,false);



            //If the activity doesn´t depend on any sensor just on the context
            if(originalActivityProperties.getSensorsList().isEmpty()){

                //Context Filter
                if(this.contextFilter(activity)){
                    if (!_activatedActivities.getActivities().containsKey(activity)){
                    
_logs += "    SensorList Empty - Context Filter passed.\n";
_logs += "        ACTIVITY DETECTED. "  + activity + " added to activated activities\n";

                        //If the activity passes the context filter, it is added to activated activities
                        _activatedActivities.addActivity(activity,originalActivityProperties.copy());
                        _activatedActivities.getActivityProperties(activity).setStartTime(new Time(System.currentTimeMillis()));
                    }

                //If the activity doesn't fulfil the context constraints and it was in activated activities    
                }else if (_activatedActivities.getActivities().containsKey(activity)){
                    
_logs += "    SensorList Empty - Context Filter failed. " + activity + " from ACTIVATED to PAST ACTIVITIES\n";
                    //Deleting the activiy from activatedActivities and adding it to pastActivities
                    this.fromActivatedToPastActivity(activity,false);
                    
                }//End if (Context Filter)
            
            //The sensor exists
            }else if (originalActivityProperties.existSensor(sensor)){
                //if the status is the same (ON or OFF)
                if(originalActivityProperties.getSensorProperties(sensor).getStatus().equals(sensorProp.getStatus())){

                        //Set the sensor´s weight and obligatory attribute according to the activity model
                        SensorProperties sp = sensorProp.copy();
                        //'Weight' and 'Obligatory' field are not received by GeoSystem or ZigBee Module. We need to copy
                        //the original values and don´t loose the 'Time' and 'Status' that the system gives us
                        sp.setWeight(_activitiesOriginalData.getSensorsPropiertiesActivity(activity, sensor).getWeight());
                        sp.setObligatory(_activitiesOriginalData.getSensorsPropiertiesActivity(activity, sensor).getObligatory());

                        if (_activatedActivities.getActivities().containsKey(activity)){
_logs += "    Same status/Exist Sensor - The " + activity + " activity already exists in the activated activities. Updating threshold\n";

                            _activatedActivities.getActivityProperties(activity).addSensor(sensor, sp);
                            _activatedActivities.getActivityProperties(activity).setThreshold(
                                 this.calculateThreshold(_activatedActivities.getActivityProperties(activity).
                                 getSensorsList()));

                        //If the activity exists in nonActivatedActivities, add the sensor weight to the previous value    
                        }else if(_nonActivatedActivities.getActivities().containsKey(activity)){

_logs += "    Same status/Exist Sensor - The " + activity + " already exists in the nonActivated activities. Updating threshold\n";

                            _nonActivatedActivities.getActivityProperties(activity).addSensor(sensor, sp);
                            _nonActivatedActivities.getActivityProperties(activity).setThreshold(
                                 this.calculateThreshold(_nonActivatedActivities.getActivityProperties(activity).
                                 getSensorsList()));

                            updateNonActivitiesDetected(activity, originalActivityProperties);


                        //Otherwise, add the activity to the nonActivatedActivities vector
                        }else{

_logs += "    Same status/Exist Sensor - The " + activity + " is new. Adding activity to nonActivated activities\n";

                            ActivityProperties ap = new ActivityProperties();
                            ap.setThreshold(sp.getWeight()/100.0);
                            ap.setLocation(originalActivityProperties.getLocation());
                            ap.setDuration(originalActivityProperties.getDuration());
                            ap.setStartTime(sp.getTime());
                            ap.addSensor(sensor, sp);

                            _nonActivatedActivities.addActivity(activity, ap);
                            
                            updateNonActivitiesDetected(activity, originalActivityProperties);
                        }

                }else{ //If the status is different

                    //Obligatgory sensors need to be activated all the time in order to recognize the activity,
                    //otherwise the activity have to be deleted from the activated activities vector
                    if (originalActivityProperties.getSensorProperties(sensor).getObligatory()){
                        
                        //If the activity exists in the non-activated activities vectors
                        if(_nonActivatedActivities.getActivities().containsKey(activity)){
                            //If the sensor exists in the activity
                            if(_nonActivatedActivities.getActivityProperties(activity).existSensor(sensor)){
                                
_logs += "    Different status/Exist Sensor - The " + activity + " (nonActivated) removed the sensor. Updating threshold\n";
                                //Delete the sensor and update the activity's threshold
                                _nonActivatedActivities.getActivityProperties(activity).deleteSensor(sensor);
                                _nonActivatedActivities.getActivityProperties(activity).setThreshold(this.calculateThreshold(
                                        _nonActivatedActivities.getActivityProperties(activity).getSensorsList()));

                                //If the threshold is equal to 0, the activity is deleted from the activities vector
                                if( _nonActivatedActivities.getActivityProperties(activity).getThreshold() == 0.0){
                                    
_logs += "        Activity: " +  activity + " deleted from nonActivated activies (Threshold 0.0)\n";
                                    _nonActivatedActivities.removeActivity(activity);
                                }
                            }
                            
                        //If the activity exists in the activated activities vectors
                        }else if(_activatedActivities.getActivities().containsKey(activity)){
                            //If the activity fulfills the context filter
                            if(this.contextFilter(activity)){
                                //If the sensor exists in the activity
                                if(_activatedActivities.getActivityProperties(activity).existSensor(sensor)){
                                    
_logs += "    Different status/Exist Sensor - Context filter Passed. The " + activity + " (activated) has removed the sensor. Updating threshold\n";
                                    //Delete the sensor and update the activity's threshold
                                    _activatedActivities.getActivityProperties(activity).deleteSensor(sensor);
                                    _activatedActivities.getActivityProperties(activity).setThreshold(this.calculateThreshold(
                                        _activatedActivities.getActivityProperties(activity).getSensorsList()));
              
                                    //If the threshold is equal to 0 or the activity doesn´t fulfill the context,
                                    //the activity is deleted from the activated activities vector and copied into pastActivities
                                    if( (_activatedActivities.getActivityProperties(activity).getThreshold() <
                                                     originalActivityProperties.getThreshold()) ){
                                        
_logs += "        Activity: " +  activity + " moved from ACTIVATED to PAST ACTIVITIES (Threshold <)\n"; 
                                        this.fromActivatedToPastActivity(activity,false);

                                    }
                                }
                                
                            }else{ //The activity is deleted from the activated activities vector and copied into pastActivities
                                
_logs += "    Different status/Exist Sensor - Context filter Failed. " + activity + " moved from ACTIVATED to PAST ACTIVITIES\n";
                                this.fromActivatedToPastActivity(activity,false);
                            }
                        }
                    }
                }
                
            //If the activity doesn´t contain the sensor. We can check its current status
            }else{
                //Update activatedActivities
                if(_activatedActivities.getActivities().containsKey(activity)){
                    //If the activity was detected and it doesn't fulfill the context filter now,
                    //it is added to the pastActivities vector
                    if(!this.contextFilter(activity)){
                        
_logs += "    Else Option - Context Filter failed. " + activity + " moved from ACTIVATED to PAST ACTIVITIES\n";
                        this.fromActivatedToPastActivity(activity, false);
                    }
                    
                //Update nonActivatedActivities    
                } else if (_nonActivatedActivities.getActivities().containsKey(activity)){
_logs += "    Else Option - Checking threshold for  " + activity + " (nonActivated activities)\n";
                        updateNonActivitiesDetected(activity, originalActivityProperties);
                }
                    
            }//End if-else (Exist sensor)
                
        }//Activities while
_logs += "------------------------------------------------------------\n\n";

        return _logs;
    }

    /**
     * Check if the activity is still valid (StartTime + Duration >= CurrentTime). If it is not, the activity is deleted
     * @param activity The activity name
     * @param activatedActivities Check activatedActivitiesVector or nonActivatedActivites
     */
    private void activityOverDuration(String activity, Boolean activatedActivities){
        
        if(activatedActivities){
            
            if(!_activatedActivities.getActivities().isEmpty()){
//System.out.println("ACTIVATED OVER DURATION - StartTime: " + _activatedActivities.getActivityProperties(activity).getStartTime().getTime()
//        + " Duration: " + _activatedActivities.getActivityProperties(activity).getDuration() +
//         " CurrentTime: " + System.currentTimeMillis());

                if((_activatedActivities.getActivityProperties(activity).getDuration() > 0) &&
                 ((_activatedActivities.getActivityProperties(activity).getStartTime().getTime() +
                        _activatedActivities.getActivityProperties(activity).getDuration()) < System.currentTimeMillis())){

_logs += "  " + activity + " expired. Moved from ACTIVATED to PAST ACTIVITY\n";
                    this.fromActivatedToPastActivity(activity, true);

                }
            }

        }else{
            
            if(!_nonActivatedActivities.getActivities().isEmpty()){

                if((_nonActivatedActivities.getActivityProperties(activity).getDuration() > 0) &&
                  ((_nonActivatedActivities.getActivityProperties(activity).getStartTime().getTime() +
                   _nonActivatedActivities.getActivityProperties(activity).getDuration()) < System.currentTimeMillis())){

_logs += "  " + activity + " expired. Removed from nonActivated activities\n";
                        _nonActivatedActivities.removeActivity(activity);

                }
            } 
        }
    }  
    
   /**
    *
    * @param activity
    * @return
    */
   private Boolean updateNonActivitiesDetected(String activity, ActivityProperties originalAP){

        //If the activity is detected and it fulfills the context filter, this is added to the activatedActivities vector
       if (this.contextFilter(activity)){
            if( _nonActivatedActivities.getActivityProperties(activity).getThreshold() >= originalAP.getThreshold()){
                
_logs += "        ACTIVITY DETECTED:" + activity + ". Context Filter Passed and Threshold: " +
    _nonActivatedActivities.getActivityProperties(activity).getThreshold() + " - Moved to activated activities \n";

                //Add the activity to activatedActivities and remove it from nonActivatedActivities
                _activatedActivities.addActivity(activity, _nonActivatedActivities.removeActivity(activity).copy());
           
                return true;
           }
       }

       return false;
   }

   /**
    *
    * @param activity
    * @param overDuration
    */
   private void fromActivatedToPastActivity(String activity, Boolean overDuration){

       Time auxTime = new Time(_activatedActivities.getActivityProperties(activity).getStartTime().getTime() +
                        _activatedActivities.getActivityProperties(activity).getDuration());
       ActivityProperties ap = _activatedActivities.removeActivity(activity).copy();

       //The endTime for this activity would be startTime + activity duration
       if(overDuration) ap.setEndTime(auxTime);
       //The endTime for this activity would be current time
       else ap.setEndTime(new Time(System.currentTimeMillis()));

       //Introduce the activity in pastActivities
       _pastActivitiesKeys.add(activity);
       _pastActivitiesActivityProperties.add(ap.copy());
       
       java.util.Date date0 = new java.util.Date(ap.getStartTime().getTime());
                java.util.Date date1 = new java.util.Date(ap.getEndTime().getTime());
_logs += "        Time: " + CommonVar.dateFormatTime.format(date0) + " to " + CommonVar.dateFormatTime.format(date1) +
        " - Duration: " + (ap.getEndTime().getTime() - ap.getStartTime().getTime()) + " milliseconds\n";

   }
    
    /**
     *
     * @param activity
     * @return
     */
    private Boolean contextFilter(String activity){
        
        Boolean filterPassed = true;
        
        //If the activity has some context restrictions
        if(!_activitiesOriginalData.getActivityProperties(activity).getContext().isEmpty()){

            Iterator aIterator = _activitiesOriginalData.getActivityProperties(activity).getContext().keySet().iterator();
            while((aIterator.hasNext()) && (filterPassed)){

                String activityContext = (String) aIterator.next();
                ContextProperties activityContextProperties  = _activitiesOriginalData.getActivityProperties(activity).
                                                getContext().get(activityContext);

                Time time =  new Time(System.currentTimeMillis());

                //If context status is activated. We have to check the activity in the _activatedActivities
                if(activityContextProperties.getStatus().equals(CommonVar.ACTIVATED)){
                   //If the activity doesn't exist in the activatedActivities vector
                   if(!_activatedActivities.getActivities().containsKey(activityContext))
                       filterPassed = false;

                //If not, we have to check the activity in the _pastActivities.Considering the time restriction
                }else if (activityContextProperties.getStatus().equals(CommonVar.DEACTIVATED)){

                    if ((_pastActivitiesKeys.contains(activityContext)) &&
                            !(_activatedActivities.getActivities().containsKey(activityContext))){
                        
                        int activityPosition = _pastActivitiesKeys.lastIndexOf(activityContext);

                        //The gap between the current time and the previos activity time is not lower than the indicated duration
//System.out.println("DEACTIVATED - Time pastActivity: " + _pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime() + " Time now: " + time.getTime() +
//         " Result: " + String.valueOf(time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) +
//         " Maximum: " + activityContextProperties.getTime());
                        
                        if(activityContextProperties.getTime() <= 0){ //Negative or 0
                            if(!((time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) 
                                        <= Math.abs(activityContextProperties.getTime())))
                                filterPassed = false;
                        }else{ //Positive
                            if(!((time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) 
                                        > activityContextProperties.getTime()))
                                filterPassed = false;
                        }
                        
                    }else filterPassed = false;
                   
                //If the status is activated/deactivated, we have to check the activity in the _pastActivities or _activatedActivities.
                //Considering the time restriction in the past activities
                }else if (activityContextProperties.getStatus().equals(CommonVar.ACTIVATED_DEACTIVATED)){

                    //if the activity exists in pastActivities vector, the gap between the activity deactivation time and
                    //the current time have to be lower (-) or bigger (+) than the defined duration
                    
                    if (_pastActivitiesKeys.contains(activityContext)){
                        
                        int activityPosition = _pastActivitiesKeys.lastIndexOf(activityContext);

//System.out.println("ACTIVATED_DEACTIVATED - Time pastActivity: " + _pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime() + " Time now: " + time.getTime() +
//         " Result: " + String.valueOf(time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) +
//         " Maximum: " + activityContextProperties.getTime());
                        
                        if(activityContextProperties.getTime() <= 0){ //Negative or 0
                            //The gap between the current time and the previos activity time is not bigger than defined duration
                            if(!((time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) 
                                    <= Math.abs(activityContextProperties.getTime())))
                                filterPassed = false;
                        }else{ //Positive
                            //The gap between the current time and the previos activity time is not lower than defined duration
                            if(!((time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) 
                                        > activityContextProperties.getTime()))
                                filterPassed = false;
                        }
                        
                    //If the activity doesn't exist in the activatedActivities vector
                    }else if(!_activatedActivities.getActivities().containsKey(activityContext))
                        filterPassed = false;

                //If the status is deactivated/nonactivated. We check that the activity is deactivated or it is not in activated activities vector
                }else if(activityContextProperties.getStatus().equals(CommonVar.DEACTIVATED_NONACTIVATED)){
                    if (_pastActivitiesKeys.contains(activityContext)){

                        int activityPosition = _pastActivitiesKeys.lastIndexOf(activityContext);

//System.out.println("DEACTIVATED_NONACTIVATED - Time pastActivity: " + _pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime() + " Time now: " + time.getTime() +
//         " Result: " + String.valueOf(time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) +
//         " Maximum: " + activityContextProperties.getTime());

                        if(activityContextProperties.getTime() <= 0){ //Negative or 0
                            //The gap between the current time and the previos activity time is not bigger than defined duration
                            if(!((time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) 
                                    <= Math.abs(activityContextProperties.getTime())))
                                filterPassed = false;
                        }else{ //Positive
                            //The gap between the current time and the previous activity time is not lower than defined duration
                            if(!((time.getTime()-_pastActivitiesActivityProperties.get(activityPosition).getEndTime().getTime()) 
                                        > activityContextProperties.getTime()))
                                filterPassed = false;
                        }

                    //If the activity exists in the activatedActivities vector - return false
                    }else if(_activatedActivities.getActivities().containsKey(activityContext))
                        filterPassed = false;

                }//End if-else
                
            }//End while

            return filterPassed;

        //If the activity has not context restrictions
        }else return filterPassed;      
    }
       
    /**
     *
     * @param sensorsList
     * @return
     */
    private Double calculateThreshold(LinkedHashMap <String,SensorProperties> sensorsList){

        Double threshold = 0.0;

        Iterator itSensorActivity = sensorsList.keySet().iterator();
        while(itSensorActivity.hasNext()){
            String sensorActivity = (String) itSensorActivity.next();
            threshold += sensorsList.get(sensorActivity).getWeight();
        }

        return (threshold/100.0);

    }

    /**
     * Return the currently activatedActivities vector
     * @return ActivitiesXMLModel
     */
    public ActivitiesXMLModel getActivatedActivities() {
        return _activatedActivities;
    }
   
     /**
     * Return the vector of previous activated activities 
     * @return LinkedList<String>
     */
    public LinkedList<String> getPastActivitiesKeys() {
        return _pastActivitiesKeys;
    }
    
    /**
     * Return the vector of ActivityProperties associated with pastActivitiesKeys
     * @return LinkedList<ActivityProperties>
     */
    public LinkedList<ActivityProperties> getPastActivitiesActivityProperties() {
        return _pastActivitiesActivityProperties;
    }
}
 