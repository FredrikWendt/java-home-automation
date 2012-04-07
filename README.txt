BUS
===============
Topics
------
bluetooth-link: device is now present/gone
presence: person is now present/gone
wifi-link: device is now present/gone
sound: play this file

Queues
------
configuration: key/value pairs (String, String)

TECHNOLOGIES
===============

Bluetooth
---------
Station
[o] reads configuration from bus (hard coded) and puts MAC in DevicesToMonitor
[x] monitors MAC addresses, and sends connection link level (0-255) to bluetooth
[x] allows MAC addresses to fall out of range on one reading before publishing "device lost"

Presence
[x] publishes the device' "owner" as present/gone


Wifi
----
Station
[ ] reads configuration from bus (hard coded) and puts MAC in DevicesToMonitor
