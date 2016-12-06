from Data.dataAccess import DataAccess
from Robots.robotFactory import Factory
from scenarios import Scenario

import datetime
import cherrypy
import json
import time
import subprocess

class Data(object):
    exposed = True
    
    def __init__(self):
        self._dao = DataAccess()
        
    def GET(self, *args, **kwargs):
        if len(args) < 1:
            raise cherrypy.HTTPError(400)
        
        questionNo = args[0]
        if questionNo == 'current':
            ques = self._dao.getActiveQuestion()
            if ques != None:
                questionNo = ques['sequenceName']
            else:
                questionNo = None
        
        if questionNo != None:
            resp = self._dao.getResponses(questionNo)
            obj = {'query': questionNo, 'responses':resp}
        else:
            obj = {'query': 'none'}
        cherrypy.response.headers['Content-Type'] = 'application/json'
        return json.dumps(obj)

    def POST(self, *args, **kwargs):
        request = json.loads(cherrypy.request.body.read())
        if not request.has_key('response'):
            raise cherrypy.HTTPError(400)
        else:
            userresponse = int(request['response'])
            if self._dao.setResponse(args[0], userresponse):
                return 'OK'
            else: 
                raise cherrypy.HTTPError(500)
            
class UserData(object):
    exposed = True
     
    def __init__(self):
        self._dao = DataAccess()
    
    def GET(self, *args, **kwargs):
        
        if len(args) < 1:
            raise cherrypy.HTTPError(400)
        
        questionNo = args[0]
        if questionNo == 'preferences':
            value = self._dao.users.getUserPreferences()
        elif questionNo == 'persona':
            value = self._dao.users.getPersonaValues()
        elif questionNo == 'username': 
            value = self._dao.users.getActiveUser()['nickname']
        elif questionNo == 'experimentLocation':
            value = self._dao.locations.getActiveExperimentLocation()
        
        cherrypy.response.headers['Content-Type'] = 'application/json'
        return json.dumps(value)
    
    def POST(self, *args, **kwargs):
        
        request = json.loads(cherrypy.request.body.read())

        if request.has_key('time'):
            time = datetime.datetime.now().strftime('%H:%M:%S') #strftime('%Y-%m-%d %H:%M:%S') 
            self._dao.users.setSessionControlTime(time)
        elif request.has_key('preferences'):
            for column, value in request.iteritems():
                if column != 'preferences':
                    print column + " " + value
                    self._dao.users.setUserPreferences(column, value)
        else:
            raise cherrypy.HTTPError(400)
        
#Scenarios Types
#
#HallDoorbell - From Living Room to Hall (Doorbell ringing)
#DrinkLiving  - From Starting Position to Living Room Sofa Area (Drink Remainder Scenario)
#KitchenDrink - From Living Room to Kitchen (Drink Remainder Scenario)
#TransportDrink - From Kitchen to Living Room (Drink Remainder Scenario)
#TransportObject - From Hall to Living Room (Doorbell ringing)            
class RobotCommands(object):
    
    exposed = True

    def __init__(self):
        self._dao = DataAccess()
        self._scenarios = Scenario()
        self._host = "nathan@sf1-1-pc1"
        self._robot = Factory.getCurrentRobot()

    def POST(self, *args, **kwargs):
        
        request = json.loads(cherrypy.request.body.read())
    
        # Send the robot to certain location (The behaviour depends on the scenario)
        if request.has_key('location'):

			if request['scenario'] == 1:
				if request['scenarioType'] == 'HallDoorbell':
					self._scenarios.ScenarioDoorbell1(request, self._host)
				else: self._scenarios.Scenario1(request, self._host)

			elif request['scenario'] == 2:
				if request['scenarioType'] == 'HallDoorbell':
					self._scenarios.ScenarioDoorbell2(request, self._host)
				elif request['scenarioType'] == 'LivingDrink':
					self._scenarios.Scenario2(request, self._host)
				else: self._scenarios.Scenario1(request, self._host)

			elif request['scenario'] == 3:
				if request['scenarioType'] == 'HallDoorbell':
					self._scenarios.ScenarioDoorbell1(request, self._host)
				else: self._scenarios.Scenario3(request, self._host)

        # Send voice command to the robot (espeak software required)        
        elif request.has_key('speech') and self._dao.getActiveQuestion()['repeated'] == 0: 
            
			#Block other interfaces to repeat the message		
			self._dao.setResponse(request['messageId'], None, 1);
			self._preferences = self._dao.users.getUserPreferences()	

			if request['scenario'] == 3 or request['messageId'] == 'doorbellAssistLowPro':
				time.sleep(0.5) #Including delay to help transition
			elif request['scenario'] == 1 or request['scenario'] == 2:
				time.sleep(7) #Time to wait for the body movement to finish

			if self._preferences[0]['voice'] == 1:
				self._robot.setComponentState('head','front')
		
				#Compose speech
				if request['scenario'] == 1 or request['scenario'] == 2:
					message = request['speech'].replace("User,", request['username'] + "," )
				else: message = request['speech'].replace("User, ", "")
				message = message.replace("<br/>", "") #Remove end of line if exists

				#subprocess.call(['C:\\Program Files (x86)\\eSpeak\\command_line\\espeak.exe', request['speech']]) #Windows
				#subprocess.call(['espeak', message]) #Linux
				subprocess.Popen(["ssh", "%s" % self._host, "espeak -s 135 '%s'" % message], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE) #Remote Linux

				if request['scenario'] == 1 or request['scenario'] == 2:
					self._robot.setComponentState('head', 'front_bend_left')
					time.sleep(0.150)
					self._robot.setComponentState('head', 'front_bend_right')
					time.sleep(0.150)
					self._robot.setComponentState('head','front')
				else: time.sleep(1)

				## Determine the speech time
				wait = 0.3 * (len(message.split())-1)
				time.sleep(wait)
			
			#It means the robot has already spoken and the dialog can be shown
			self._dao.setResponse(request['messageId'], None, 2);
			
			# Open Tray
			if self._robot.getComponentState('tray')[0] == 'lowered':
				self._robot.setComponentState('tray', 'raised')

			#Finish Action
			self._robot.setComponentState('head','back')
			self._robot.setLight(eval(self._preferences[0]['light']))

			# Open Tray (Sometimes the first attempt is failing)
			if self._robot.getComponentState('tray')[0] != 'raised':
				self._robot.setComponentState('tray', 'raised')
                
        else: raise cherrypy.HTTPError(400)      
            
            


