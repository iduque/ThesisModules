/** Questions*/

	var myTimeout;

	Function.prototype.bind = function(scope) {
	  var _function = this;
	  
	  return function() {
	    return _function.apply(scope, arguments);
	  }
	}

	function uiHelper() {
		
	}
	
	uiHelper.prototype = {
		load : function(questions) {
			var dao = new dataHelper();
			dao.pollResponses(this.fill.bind(this), questions, -1);
		},

		fill : function(root, lastId, data) {
			if (lastId != data['query']) {
				$(root).empty();
				$(root).hide();
				if (data['query'] != 'none') {
					//$('.question')
					//for (index in data['responses']) {
					var response = data['responses'][0]
					var newQuery = $('<div></div>')
					var self = this;
					
					var scenario = new dataHelper().getResponse('/userdata/experimentLocation')['scenario'];
					var username = new dataHelper().getResponse('/userdata/username');
					var dialogType = "";
					var dialog_buttons = {}; 

					newQuery.attr('responseId', response['guiResponseId']);
					newQuery.attr('size', response['size']);
					if (scenario == 1 || scenario == 2){//Delete the user name when speaking and showing dialogs
						newQuery.html(response['message'].replace("User,", username + ","));
					}else{
						newQuery.html(response['message'].replace("User,", ""));
					}
					$(root).append(newQuery);

					if(response['seqName'].indexOf("goLivingRoom") != -1 || response['seqName'].indexOf("fromDiningArea") != -1) {
						if (scenario == 1 || scenario == 3){
							dialogType = "dialogStyleQuest";
							dialog_buttons = [ 
						        	{text: "Ok, Go to the Kitchen",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(1, data['query'], "/home/commands/goto", "Kitchen", "KitchenDrink", scenario, username); }
								}]
						}else{
							dialogType = "dialogStyleQuest";
							dialog_buttons = [ 
						        	{text: "Yes, Go to the Kitchen",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(1, data['query'], "/home/commands/goto", "Kitchen", "KitchenDrink", scenario, username); }
								},
								{text: "No, Thanks",
								 class: 'cancelButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "Starting Position", "None", scenario, username); }
								}]
						}
					}else if(response['seqName'].indexOf("goKitchen") != -1) {
						dialogType = "dialogStyleQuest";
						dialog_buttons = [ 
						        {text: "Yes, Go to Living Room",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(1, data['query'], "/home/commands/transportto", "Living", "TransportDrink", scenario, username); }
								},
								{text: "No, Thanks",
								 class: 'cancelButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "Starting Position", "None", scenario, username); }
								}]
					}else if(response['seqName'].indexOf("transportDrink") != -1) {
						dialogType = "dialogStyleInfo";
						dialog_buttons = [{text: "Thank you",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "Starting Position", "None", scenario, username); }
								}]
					}else if(response['seqName'].indexOf("transportObject") != -1) {
						dialogType = "dialogStyleInfo";
						dialog_buttons = [{text: "Thank you",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "Starting Position", "None", scenario, username); }
								}]
					}else if(response['seqName'].indexOf("readingNewspaper") != -1) {
						dialogType = "dialogStyleQuest";
						dialog_buttons = [ 
							        {text: "Yes, Please",
								 class : 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(1, data['query'], "/home/services", "None", "None", scenario, username); }
								},
								{text: "No, Thanks",
								 class: 'cancelButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "None", "None", scenario, username); }
								}]
					}else if(response['seqName'].indexOf("doorbellAssistance") != -1) {
						dialogType = "dialogStyleQuest";
						dialog_buttons = [ 
						        {text: "Yes, Go to Living Room",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(1, data['query'], "/home/commands/goto", "Living", "TransportObject", scenario, username); }
								},
								{text: "No, Thanks",
								 class: 'cancelButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "Starting Position", "None", scenario, username); }
								}]
					}else if(response['seqName'].indexOf("doorbellAssistLowPro") != -1) {
						dialogType = "dialogStyleQuest";
						dialog_buttons = [ 
						        {text: "Yes, Go to the Hall",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(1, data['query'], "/home/commands/transportto", "Hall", "DoorbellGoToHall", scenario, username); }
								},
								{text: "No, Thanks",
								 class: 'cancelButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "Starting Position", "None", scenario, username); }
								}]
					}else{
						dialogType = "dialogStyleQuest";
						dialog_buttons = [{text: "Yes, Please",
								 class: 'acceptButtonClass',
								 click: function(){ self.answerQuestionDialog(1, data['query'], "/home", "None", "None", scenario, username); }
								 },
								 {text: "No, Thanks",
								 class: 'cancelButtonClass',
								 click: function(){ self.answerQuestionDialog(2, data['query'], "/home", "None", "None", scenario, username); }
								 }]
					}
//					$(root).dialog('close');
//					myTimeout = setTimeout(function(){self.openDialog(root, 'open', response['message']);}, 30000); }  

					var w = $(window).width() * 0.85 ;
					$(root).dialog({
						dialogClass: dialogType,
						closeOnEscape: false,
						autoOpen : false,
						draggable: false,
						modal: true,
						width : w,
						title : "Robot Request",
						resizable: false,
						buttons: dialog_buttons
					});
					//$('.ui-button-text-only .ui-button-text').css('font-size', 30);
					this.openDialog(root, 'open', data['query'], response['message'], scenario, username);
					
					//$(root).dialog('widget').animate({'left': '+=' + w + 'px'}, 1000);	
					
				} else {
					
					if($(root).hasClass('ui-dialog-content')) {

						//$(root).dialog('widget').animate({'left': '-=' + w + 'px'}, 1000);
						$(root).hide();
						$(root).dialog('close');
						clearTimeout(myTimeout);
					}
					
				}
				
			}		

			return data['query']
		},
		openDialog: function(dialog, status, messageId, speech, scenario, username) {
			
			var url = '/command';
			var result = {};
			var obj = {
				'speech' : speech,
				'messageId' :  messageId,
				'scenario' : scenario,
				'username' : username
			};
			$.ajax({
				url : url,
				data : JSON.stringify(obj),
				async : false,
				contentType : 'application/json',
				error : function(jqXHR, status, error) {
					result = {
						status : status,
						error : jqXHR.responseText
					};
				},
				success : function(data, status, jqXHR) {
					result = {
						status : status,
						data : data
					};
				},
				type : 'POST'
			});

			
			
			//if(result.status == 'success'){
			var dao = new dataHelper();
			var repeated = dao.getResponse('/data/current')['responses'][0]['repeated'];
			while (repeated != 2){
				repeated = dao.getResponse('/data/current')['responses'][0]['repeated'];
			}
			$(dialog).dialog(status);
			$(dialog).show();
			//}
			
		},
		answerQuestionDialog : function(id, question, newpage, location, scenarioType, scenario, username) {
			
			if(newpage != ""){
				window.open (newpage,'_self',false);
			}
			
			var url = '/data/' + question;
			var result = {};
			var obj = {
				'response' : id
			};
			$.ajax({
				url : url,
				data : JSON.stringify(obj),
				async : false,
				contentType : 'application/json',
				error : function(jqXHR, status, error) {
					result = {
						status : status,
						error : jqXHR.responseText
					};
				},
				success : function(data, status, jqXHR) {
					result = {
							status : status,
							data : data
					};					
				},
				type : 'POST'
			});

			if (result.status == 'success') {
				var ui = new uiHelper();
				if(location == "Kitchen"){
					ui.sendCommand('Kitchen Entrance', 'lowered', 'Going to the Kitchen', scenario, scenarioType, username);
				}else if (location == "Living"){
					ui.sendCommand('Living Room Sofa Area', 'raised', 'Transporting object to the Living Room', scenario, scenarioType, username);
				}else if (location == "Starting Position"){
					ui.sendCommand('Starting Position', 'lowered', 'Coming back to my home position', scenario, scenarioType, username);
				}else if (location == "Hall"){
					ui.sendCommand('Hall Entrance', 'lowered', 'Lets go to the Hall', scenario, scenarioType, username);
				}
			} else {
				//Handle errors?
				alert(result.status);
			}
		},
		setTime : function(){
			var url = '/userdata/time';
			var object = { 'time' : '' };
			$.ajax({
				url : url,
				data : JSON.stringify(object),
				async : false,
				contentType : 'application/json',
				error : function(jqXHR, status, error) {
					result = {
						status : status,
						error : jqXHR.responseText
					};
				},
				success : function(data, status, jqXHR) {
					result = {
						status : status,
						data : data
					};
				},
				type : 'POST'
			});
		},
		
		setUserPrefs : function() {
			var dao = new dataHelper().getResponse('/userdata/persona');
			$('.myButton').css('font-size', dao[0]['fontSize']); //40 maximum
			$('.myButtonRoom').css('font-size', dao[0]['fontSize']); //40 maximum
			$('.ui-dialog .ui-dialog-content').css('font-size', dao[0]['fontSize']); //35 maximum
			$('.ui-button-text-only .ui-button-text').css('font-size', dao[0]['fontSize']); //35 maximum
			$('.welcome').css('font-size' , dao[0]['fontSize']); //40 maximum
		},
		
		sendCommand: function(room, tray, speech, scenario, scenarioType, username){
			
			//Set the approach to social			
			if (scenario != 1 ){
			    if(room.indexOf("Kitchen") != -1) room = 'Kitchen Entrance Social';
			    else if(room.indexOf("Living Room") != -1) room = 'Living Room Sofa Area Social';
			    else if(room.indexOf("Dining Area") != -1) room = 'Dining Room Table';
			    else if(room.indexOf("Hall Entrance") != -1) room = 'Hall Entrance Social';
			}

			var url = '/command'
			var result = {};
			var obj = {
				'location' : room,
				'tray' : tray,
				'speech' : speech,
				'scenario' : scenario,
				'scenarioType' : scenarioType,
				'username' : username
			};
			$.ajax({
				url : url,
				data : JSON.stringify(obj),
				async : false,
				contentType : 'application/json',
				error : function(jqXHR, status, error) {
					result = {
						status : status,
						error : jqXHR.responseText
					};
				},
				success : function(data, status, jqXHR) {
					result = {
						status : status,
						data : data
					};
				},
				type : 'POST'
			});

			if (result.status == 'success') {
				//location.reload(true);
			} else {
				//Handle errors?
				alert(result.status);
			}
			
		},
		
		getActiveExperimentLocation: function(){
			return new dataHelper().getResponse('/userdata/experimentLocation');
		},
		
		getPreferences: function(){
	        	return new dataHelper().getResponse('/userdata/preferences');
		},
		
		getInterface: function(){
			return new dataHelper().getResponse('/userdata/persona');
		},
		
		getUsername: function() {
			return new dataHelper().getResponse('/userdata/username');
		},
		changePreferences: function(object){
			var url = '/userdata/preferences';
			$.ajax({
				url : url,
				data : JSON.stringify(object),
				async : false,
				contentType : 'application/json',
				error : function(jqXHR, status, error) {
					result = {
						status : status,
						error : jqXHR.responseText
					};
				},
				success : function(data, status, jqXHR) {
					result = {
						status : status,
						data : data
					};
				},
				type : 'POST'
			});
		}
		
	}
	
	function dataHelper() {
	}

	dataHelper.prototype = {
		getResponse : function(url) {
			var response = null;
			$.ajax({
				url : url,
				dataType : 'json',
				async : false,
				timeout: 3000,
				success : function(data, textStatus, jqXHR) {
					response = data
				},
			});

			return response;
		},
		pollResponses : function(callback, root, lastId) {
			var self = this;
			var data = self.getResponse('/data/current');
			var id = callback(root, lastId, data);
			setTimeout(function() {
				self.pollResponses(callback, root, id);
			}, 2000);
		}
	}

	$(function() {		
		var ui = new uiHelper();
		//Welcome messages
        $('#welcomeMessage').text("Welcome, " + ui.getUsername());

		//Question dialog
		if($('#dialog').length == 0) {
			$('body').append('<div id="dialog"/></div>');						
		}
		
		ui.load('#dialog');
		
		//Font preferences
		ui.setUserPrefs();
	});
