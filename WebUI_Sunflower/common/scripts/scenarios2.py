'''
Created on 20/09/2014

@author: Ismael
'''

from Data.dataAccess import DataAccess
from Robots.robotFactory import Factory

import subprocess
import time
import threading
import sys

class myThreadTray(threading.Thread):
    def __init__(self, threadID, name, robot):
        threading.Thread.__init__(self)
        self._threadID = threadID
	self._robot = robot
        self._name = name

    def run(self):
        print "Starting " + self._name
	self._robot.setComponentState('tray', 'raised')
        print "Exiting " + self._name
	threading.Thread.__init__(self)

class myThreadHead(threading.Thread):
    def __init__(self, threadID, name, robot):
        threading.Thread.__init__(self)
        self._threadID = threadID
	self._robot = robot
        self._name = name
	self._dao = DataAccess()

    def run(self):
	print "Starting " + self._name
	self._robot.setComponentState('head','up')
	time.sleep(0.5)
	self._robot.setComponentState('head', 'up_look_left')
	time.sleep(0.1)
	self._robot.setComponentState('head', 'up_look_right')
	time.sleep(0.1)
	self._robot.setComponentState('head', 'up_look_left')
	time.sleep(0.1)
	self._robot.setComponentState('head', 'up_look_right')
	time.sleep(0.1)
	self._robot.setComponentState('head','up')
	time.sleep(0.5)
	self._robot.setComponentState('head','front')
        print "Exiting " + self._name
	threading.Thread.__init__(self)

class myThreadLights(threading.Thread):
    
    def __init__(self, threadID, name, robot):
        threading.Thread.__init__(self)
        self._threadID = threadID
	self._robot = robot
        self._name = name
	self._signal = True
	self._lightFrequency = 0.550;
	self._times = 0
	self._lightColor = [1,1,0] 
	#self._speak = True

    def stopLights(self, prefLight):
	self._lightFrequency = 0;
	self._signal = False
	self._times = 0
	self._lightColor = prefLight 
	self._speak = False

    def arrivedPlace(self, prefLight):
	self._lightFrequency = 0.250;
	self._signal = False  
	self._times = 11
	self._lightColor = prefLight 
	#self._speak = speak 

    def resetValues(self):
	self._signal = True
	self._lightFrequency = 0.550;
	self._times = 0
	self._lightColor = [1,1,0]
	#self._speak = True

    def run(self):
        print "Starting " + self._name
	counter = 0
        while (self._signal or counter < self._times):
            self._robot.setLight(self._lightColor)
            time.sleep(self._lightFrequency)
            self._robot.setLight([0,0,0])
            time.sleep(self._lightFrequency)
	    if not self._signal:
            	counter += 1
        print "Exiting " + self.name
	
	#if self._speak:
	self._robot.setLight(self._lightColor)
	#else:
	#	self._robot.setLight([0,1,1])

	self.resetValues()
	threading.Thread.__init__(self)

class myThreadBase(threading.Thread):
    def __init__(self, threadID, name, robot):
        threading.Thread.__init__(self)
        self._threadID = threadID
        self._name = name
	self._robot = robot

    def run(self):
        print "Starting " + self._name
	self._robot.setComponentState('head','up')
	
	self._robot.setComponentState('base_direct', [0.2, 0.0])
	self._robot.setComponentState('head', 'up_look_left')
        
	self._robot.setComponentState('base_direct', [-0.4, 0.0])
	self._robot.setComponentState('head', 'up_look_right')

	self._robot.setComponentState('base_direct', [0.3, 0.0])
	self._robot.setComponentState('head', 'up_look_left')
        
	self._robot.setComponentState('base_direct', [-0.2, 0.0])
	self._robot.setComponentState('head', 'up_look_right')

	self._robot.setComponentState('base_direct', [0.1, 0.0])
	self._robot.setComponentState('head','up')
	time.sleep(0.5)
	self._robot.setComponentState('head','front')
        print "Exiting " + self._name
	threading.Thread.__init__(self)

class Scenario(object):
    
    def __init__(self):
        self._dao = DataAccess()
	self._preferences = self._dao.users.getUserPreferences()
        self._robot = Factory.getCurrentRobot()
	self._threadLights = myThreadLights(1, "Thread-Lights", self._robot)
	self._threadHead = myThreadHead(2, "Thread-Head", self._robot)
	self._threadBase = myThreadBase(3, "Thread-BaseDirect", self._robot)
	self._threadTray = myThreadTray(4, "Thread-Tray", self._robot);	
    
    def Scenario1(self, request, host):
        #Get the location selected by the user
        location = self._dao.locations.getLocationByName(request['location']);
	#Preferred LED colour        

	self._robot.setComponentState('head','front') #Set default head position    

        #If robot voice is activated    
        if self._dao.users.getUserPreferences()[0]['voice'] == 1:    
            #subprocess.call(['C:\\Program Files (x86)\\eSpeak\\command_line\\espeak.exe', request['speech']]) #Windows
            #subprocess.call(['espeak', request['speech']]) #Linux
            subprocess.Popen(["ssh", "%s" % host, "espeak -s 135 '%s'" % request['speech']], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE) #Remote Linux
	
	#time.sleep(1.5)	

	#Open or close tray
	if 'lowered' == request['tray']:
		self._robot.setComponentState('tray', 'lowered')
	else:
        	self._robot.setComponentState('tray', 'raised')
	
	time.sleep(1.5)
	
	self._threadLights.start();
        #Send the robot to the location
	#time.sleep(4)
        self._robot.setComponentState('base', [location['xCoord'], location['yCoord'], location['orientation']])
	
        
        if "Kitchen Entrance" in request['location']:
	    self._threadLights.arrivedPlace(eval(self._preferences[0]['light']))
	    self._threadBase.start()
            self._dao.setResponse('goKitchen', None);

        elif "Living" in request['location']:
	    
	    self._threadLights.arrivedPlace(eval(self._preferences[0]['light']))
            if "Transporting" not in request['speech']:
		self._threadBase.start()
                self._dao.setResponse('goLivingRoom', None);  
            else:
		self._threadHead.start()	
                self._dao.setResponse('transportLivingRoom', None); 
	else: 
	      self._threadLights.stopLights(eval(self._preferences[0]['light']))
	      self._robot.setComponentState('tray', 'raised') 
	      self._robot.setComponentState('head','back') #Set head position  
            
    def Scenario3(self, request, host):
        #Get the location selected by the user
        location = self._dao.locations.getLocationByName(request['location']);
    
	self._robot.setComponentState('head','front') #Set default head position 
    
        #If robot voice is activated    
        if self._dao.users.getUserPreferences()[0]['voice'] == 1:    
            #subprocess.call(['C:\\Program Files (x86)\\eSpeak\\command_line\\espeak.exe', request['speech']]) #Windows
            #subprocess.call(['espeak', request['speech']]) #Linux
            subprocess.Popen(["ssh", "%s" % host, "espeak -s 135 '%s'" % request['speech']], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE) #Remote Linux

	#Open or close tray
	if 'lowered' == request['tray']:
		self._robot.setComponentState('tray', 'lowered')
	else:
        	self._robot.setComponentState('tray', 'raised')
	
	time.sleep(1.5)

        #Send the robot to the location
	self._threadLights.start()
	#time.sleep(4)
        self._robot.setComponentState('base', [location['xCoord'], location['yCoord'], location['orientation']])

	self._threadLights.stopLights(eval(self._preferences[0]['light']))

        if "Kitchen Entrance" in request['location']:
            self._dao.setResponse('goKitchen', None);
            
        elif "Living" in request['location']:
            
            if "Transporting" not in request['speech']:
                self._dao.setResponse('goLivingRoom', None);                
            else:
                self._dao.setResponse('transportLivingRoom', None);

	else: 	
		self._robot.setComponentState('tray', 'raised')   
		self._robot.setComponentState('head','back') #Set head position   
    
    def ScenarioDoorbell3(self, request, host):
        #Get the location selected by the user
        location = self._dao.locations.getLocationByName(request['location']);     
	
	self._threadLights.start();
	time.sleep(2)
	self._robot.setComponentState('head','front') 
	self._threadLights.arrivedPlace(eval(self._preferences[0]['light']))

	#Set base movement (expressive behaviour)
	self._robot.setComponentState('head','up')
	
	self._robot.setComponentState('base_direct', [0.2, 0.0])
	self._robot.setComponentState('head', 'up_look_left')
        

	self._robot.setComponentState('base_direct', [-0.4, 0.0])
	self._robot.setComponentState('head', 'up_look_right')

	self._robot.setComponentState('base_direct', [0.3, 0.0])
	self._robot.setComponentState('head', 'up_look_left')
        
	self._robot.setComponentState('base_direct', [-0.2, 0.0])
	self._robot.setComponentState('head', 'up_look_right')

	self._robot.setComponentState('base_direct', [0.1, 0.0])
	self._robot.setComponentState('head','up')
	time.sleep(0.5)
	self._robot.setComponentState('head','front')

	#Compose speech 
	message = request['speech'].replace("User", self._dao.users.getActiveUser()['firstName'])
	message = message.replace("<br/>", "") #Remove end of line if exists 

        #If robot voice is activated    
        if self._dao.users.getUserPreferences()[0]['voice'] == 1:    
            #subprocess.call(['C:\\Program Files (x86)\\eSpeak\\command_line\\espeak.exe', request['speech']]) #Windows
            #subprocess.call(['espeak', request['speech']]) #Linux
            subprocess.Popen(["ssh", "%s" % host, "espeak -s 135 '%s'" % request['speech']], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE) #Remote Linux
	
        self._robot.setComponentState('head', 'front_bend_left')
	time.sleep(0.150)
	self._robot.setComponentState('head', 'front_bend_right')
	time.sleep(0.150)
	self._robot.setComponentState('head','front')

	#Open or close tray
	if 'lowered' == request['tray']:
		self._robot.setComponentState('tray', 'lowered')
	else:
        	self._robot.setComponentState('tray', 'raised')
	
	#Wait until the tray finishes
	time.sleep(1.5)
        
	#Send the robot to the location #time.sleep(4)
	self._threadLights.start();
	time.sleep(4)
        #self._robot.setComponentState('base', [location['xCoord'], location['yCoord'], location['orientation']])
	#Stop blinking lights and tell user about help to transport
	self._threadLights.stopLights(eval(self._preferences[0]['light']))


	self._dao.setResponse('doorbellAssistance', None);

        
                    
