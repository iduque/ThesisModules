#!/usr/bin/python
from Data.dataAccess import DataAccess
from Robots.robotFactory import Factory

import time
import threading
import subprocess
import sys

class myThreadHead(threading.Thread):
    def __init__(self, threadID, name, robot):
        threading.Thread.__init__(self)
        self._threadID = threadID
	self._robot = robot
        self._name = name

    def run(self):
        print "Starting " + self._name
	self._robot.setComponentState('head','back')
        self._robot.setComponentState('head','front')
	self._robot.setComponentState('head', 'front_bend_left')
        time.sleep(0.200)
        self._robot.setComponentState('head', 'front_bend_right')
        time.sleep(0.200)
	self._robot.setComponentState('head','back')
        print "Exiting " + self._name
	threading.Thread.__init__(self)

class myThreadLights(threading.Thread):
    
    def __init__(self, threadID, name, robot):
        threading.Thread.__init__(self)
        self._threadID = threadID
	self._robot = robot
        self._name = name
	self._signal = True
	self._lightFrequency = 500;
	self._times = 0

	self._lightColor = [0,1,0]


    def lightFreq(self, freq):
	self._lightFrequency = freq
	self._signal = False  
	self._times = 10
	self._lightColor = [0,1,0]  

    def stopLights(self):
	self._times = 0
	self._signal = False 

    def run(self):
        print "Starting " + self._name
	counter = 0
        while (self._signal or counter < self._times):
            self._robot.setLight(self._lightColor)
            self._robot.sleep(self._lightFrequency)
            self._robot.setLight([0,0,0])
            self._robot.sleep(self._lightFrequency)
	    if not self._signal:
            	counter += 1
        print "Exiting " + self.name

class myThreadBase(threading.Thread):
    def __init__(self, threadID, name, robot):
        threading.Thread.__init__(self)
        self._threadID = threadID
        self._name = name
	self._robot = robot

    def run(self):
        print "Starting " + self._name
	self._robot.setComponentState('base_direct', [0, -0.20])
	self._robot.setComponentState('base_direct', [0, 0.20])
        print "Exiting " + self._name
	threading.Thread.__init__(self)

	
class Scenario(object):
    
    def __init__(self):
	self._dao = DataAccess()
        self._robot = Factory.getRobot('UH Sunflower');
	self._thread1 = myThreadLights(1, "Thread-1", self._robot)
	self._thread2 = myThreadHead(2, "Thread-2", self._robot)
    
    def Scenario1(self):
	#self._thread1.start()
	time.sleep(25)
	self._dao.setResponse('fromDiningArea', None);
	#self._robot.setLight([0,1,0])
	#self._thread1.stopLights()
	
	#self._thread1.lightFreq(250)
	#self._thread3.start()


print "Starting Main Thread"
scenario = Scenario().Scenario1()
print "Exiting Main Thread"

