# -*- coding: utf-8 -*-
################################################################################
#
# Robot House Data Connector
#
# Configuration file
#
# Last edited: 2010-12-14
# by:          Patrick Neuberger <p.neuberger@herts.ac.uk>
#
################################################################################

################################################################################
#
# The main configuration of the Data Connector program
#
################################################################################
server_config = {
  # The port on which the web server will be running
  # Point your web browser to http://localhost:<http_port>
  # must be above 104 to avoid root privilage!!

  'http_port':       4040,

  # The port on which the program is listening for UDP broadcast messages
  # transmitted by the ZigBee gateway

  'udp_listen_port': 5000,

  # The settings of the Geo-System MySQL server / database / table
  'mysql_geo_server':   '10.0.1.52',
  'mysql_geo_user':     '',
  'mysql_geo_password': '',
  'mysql_geo_db':       'livewiredb',
  'mysql_geo_query':    'CALL expPower',

  # The settings for the channel logging MySQL server / database / table
  'mysql_log_server':   '',
  'mysql_log_user':     '',
  'mysql_log_password': '',
  'mysql_log_db':       'Accompany',
  # table fields: timestamp (int), id (int), room (string), channel (string), value (string), status (string)
  'mysql_log_table':    'SensorLog'
}

################################################################################
#
# The configuration of the ZigBee devices and channels
#
################################################################################
zigbee_devices = {
  '[00:13:a2:00:40:32:de:87]!' : {      # ZigBee device MAC address
    'room': 'Kitchen',                  # Room name
    'AD0': {                            # Channel descriptor
      'id'  : 1,                        # This ID is only relevant for logging
      'name': 'Water Pipe Sink Hot',         # Channel name
      'type': 'TEMPERATURE_MCP9700_HOT' # Channel type
    },
    'AD1': {
      'id'  : 2,
      'name': 'Water Pipe Sink Cold',
      'type': 'TEMPERATURE_MCP9700_COLD'
    },
    'DIO2': {
      'id'  : 3,
      'name': 'Ceiling cupboard door left',
      'type': 'CONTACT_REED'
    },
    'DIO3': {
      'id'  : 4,
      'name': 'Ceiling cupboard door middle',
      'type': 'CONTACT_REED'
    },
    'DIO4': {
      'id'  : 5,
      'name': 'Ceiling cupboard door right',
      'type': 'CONTACT_REED'
    },
    'DIO5': {
      'id'  : 6,
      'name': 'Floor cupboard drawer middle',
      'type': 'CONTACT_REED'
    },
    'DIO7': {
      'id'  : 8,
      'name': 'Floor cupboard door middle',
      'type': 'CONTACT_REED'
    },
    'DIO11': {
      'id'  : 7,
      'name': 'Floor cupboard drawer right',
      'type': 'CONTACT_REED'
    },
    'DIO12': {
      'id'  : 9,
      'name': 'Floor cupboard door right',
      'type': 'CONTACT_REED'
    },
	'DIO10': {
      'id'  : 10,
      'name': 'Floor cupboard door left',
      'type': 'CONTACT_REED'
    }
  },

  '[00:13:a2:00:40:62:a9:52]!' : {
    'room': 'Bathroom',
    'AD0': {
      'id'  : 11,
      'name': 'Water Pipe Washbasin Hot',
      'type': 'TEMPERATURE_MCP9700_HOT'
    },
    'AD1': {
      'id'  : 12,
      'name': 'Water Pipe Washbasin Cold',
      'type': 'TEMPERATURE_MCP9700_COLD'
    },
    'DIO2': {
      'id'  : 13,
      'name': 'Bathroom door',
      'type': 'CONTACT_REED'
    },
    'DIO3': {
      'id'  : 14,
      'name': 'Toilet Flush',
      'type': 'CONTACT_REED'
    }
  },

  '[00:13:a2:00:40:62:a9:4f]!' : {
    'room': 'Bedroom 1',
    'DIO0': {
      'id'  : 31,
      'name': 'Desk drawer bottom',
      'type': 'CONTACT_REED'
    },
    'DIO1': {
      'id'  : 32,
      'name': 'Desk drawer middle',
      'type': 'CONTACT_REED'
    },
    'DIO2': {
      'id'  : 33,
      'name': 'Desk drawer top',
      'type': 'CONTACT_REED'
    },
    'DIO3': {
      'id'  : 34,
      'name': 'Desk door',
      'type': 'CONTACT_REED'
    },
    'DIO4': {
      'id'  : 35,
      'name': 'Office chair',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO5': {
      'id'  : 36,
      'name': 'Bedroom door',
      'type': 'CONTACT_REED'
    },
    'DIO7': {
      'id'  : 37,
      'name': 'Bed contact',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO10': {
      'id'  : 38,
      'name': 'Wardrobe door left',
      'type': 'CONTACT_REED'
    },
    'DIO11': {
      'id'  : 39,
      'name': 'Wardrobe door middle',
      'type': 'CONTACT_REED'
    },
    'DIO12': {
      'id'  : 40,
      'name': 'Wardrobe door right',
      'type': 'CONTACT_REED'
    }
  },

  '[00:13:a2:00:40:62:a9:4d]!' : {
    'room': 'Living Room',
    'DIO0': {
      'id'  : 15,
      'name': 'Sofa Seatplace 0',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO1': {
      'id'  : 16,
      'name': 'Sofa Seatplace 1',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO2': {
      'id'  : 17,
      'name': 'Sofa Seatplace 2',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO3': {
      'id'  : 18,
      'name': 'Sofa Seatplace 3',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO4': {
      'id'  : 19,
      'name': 'Sofa Seatplace 4',
      'type': 'CONTACT_PRESSUREMAT'
    }
  },

  '[00:13:a2:00:40:62:a9:4e]!' : {
    'room': 'Dining Area',
    'DIO0': {
      'id'  : 20,
      'name': 'Chair Seatplace 0',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO1': {
      'id'  : 21,
      'name': 'Chair Seatplace 1',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO2': {
      'id'  : 22,
      'name': 'Table Pressure 2',
      'type': 'CONTACT_PRESSUREMAT'
    },
    'DIO4': {
      'id'  : 23,
      'name': 'Living room door',
      'type': 'CONTACT_REED'
    },
    'DIO5': {
      'id'  : 24,
      'name': 'Big cupboard drawer bottom',
      'type': 'CONTACT_REED'
    },
    'DIO3': {
      'id'  : 25,
      'name': 'Big cupboard drawer top',
      'type': 'CONTACT_REED'
    },
    'DIO7': {
      'id'  : 26,
      'name': 'Small cupboard door left',
      'type': 'CONTACT_REED'
    },
    'DIO6': {
      'id'  : 27,
      'name': 'Small cupboard door right',
      'type': 'CONTACT_REED'
    },
    'DIO10': {
      'id'  : 28,
      'name': 'Small cupboard drawer bottom',
      'type': 'CONTACT_REED'
    },
    'DIO11': {
      'id'  : 29,
      'name': 'Small cupboard drawer middle',
      'type': 'CONTACT_REED'
    },
    'DIO12': {
      'id'  : 30,
      'name': 'Small cupboard drawer top',
      'type': 'CONTACT_REED'
    }
  }
}

################################################################################
#
# The configuration of the Geo-System channels
#
################################################################################
geosystem_devices = {
  1: {                         # The "channel ID" delivered by the MySQL function
    'id'  : 41,                # This ID is only relevant for logging
    'room': 'Other',           # Room name
    'name': 'Lights exterior', # Channel name
    'rule': 'p > 10'           # Device is "On", if power consumption is higher than 10 Watts
  },
  2: {
    'id'  : 42,
    'room': 'Other',
    'name': 'Upstairs Lights',
    'rule': 'p > 10'
  },
  3: {
    'id'  : 43,
    'room': 'Other',
    'name': 'Downstairs Lights',
    'rule': 'p > 10'
  },
  7: {
    'id'  : 44,
    'room': 'Kitchen',
    'name': 'Cooker',
    'rule': 'p > 10'
  },
  8: {
    'id'  : 45,
    'room': 'Other',
    'name': 'Garage',
    'rule': 'p > 5'
  },
  9: {
    'id'  : 46,
    'room': 'Other',
    'name': 'Sockets',
    'rule': 'p > 5'
  },
  10: {
    'id'  : 47,
    'room': 'Other',
    'name': 'Sockets Ext. and garden',
    'rule': 'p > 5'
  },
  12: {
    'id'  : 48,
    'room': 'Other',
    'name': 'Mains Supply',
    'rule': 'p > 10'
  },
  13: {
    'id'  : 49,
    'room': 'Living Room',
    'name': '1 TV',
    'rule': 'p > 10'
  },
  14: {
    'id'  : 50,
    'room': 'Kitchen',
    'name': '2 Fridge Freezer',
    'rule': '(p > 10 and p < 50) or (p > 100)'
  },
  15: {
    'id'  : 51,
    'room': 'Kitchen',
    'name': '3 Kettle',
    'rule': 'p > 10'
  },
  16: {
    'id'  : 52,
    'room': 'Bedroom',
    'name': '4 Computer',
    'rule': 'p > 10'
  },
  17: {
    'id'  : 53,
    'room': 'Bedroom',
    'name': '5 Table Lamp',
    'rule': 'p > 10'
  },
  18: {
    'id'  : 54,
    'room': 'Kitchen',
    'name': '6 Microwave',
    'rule': 'p > 10'
  },
  19: {
    'id'  : 55,
    'room': 'Kitchen',
    'name': '7 Dishwasher',
    'rule': 'p > 10'
  },
  20: {
    'id'  : 56,
    'room': 'Kitchen',
    'name': 'Toaster',
    'rule': 'p > 10'
  },
  21: {
    'id'  : 57,
    'room': 'Living room (Sofa)',
    'name': '9 Living room light',
    'rule': 'p > 5'
  },
  22: {
    'id'  : 58,
    'room': 'Other',
    'name': '10 Rumba',
    'rule': 'p > 10'
  },
  24: {
    'id'  : 59,
    'room': 'Other',
    'name': '12 Doorbell',
    'rule': 'p > 1'
  }
}

### EOF ###
