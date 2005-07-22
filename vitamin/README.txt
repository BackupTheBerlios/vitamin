
     ----- Vitamin -----

      by Martin Pelzer
   Fraunhofer FOKUS.SatCom
           2004


What is Vitamin?
----------------

Vitamin is a software for visualizing network traffic in
IP based networks. The software works as a distributed
system with one master (where the visualisation takes
place) and several slaves which capture the packets from
the network and analyze them.

All protocols Vitamin can visualize are given through XML
based plugins, so that it is easy to extend the software.


System requirements
-------------------

Vitamin has been tested on Fedora Linux and Mandrake Linux,
both with Java 5 installed. As Vitamin does not use the
language extensions of Java 5, it should also run with a
Java 1.4 Runtime Environment.


How to start Vitamin
--------------------

1. Starting the master with the "startMaster"-script.
   Parameters:
     - IP to bind to:
       IP address over which the master communicates
       with the slaves.
     - topology file:
       XML file which describes the topology of the
       network Vitamin is used in.
       See
       "doc/how_to_specify_the_network_topology.pdf"
       for more information about that.

2. Starting all slaves with the "startSlave" script.
   Parameters:
     - master:
       IP address of the master
     - device:
       the device to capture from (e.g eth0)
     - localIP:
       An IP address associated to the given device.
       The slave will bind to this address.
     - multicast (optional)
       When a slave is started with option "multicast"
       it will capture multicast packets.
  

How to use Vitamin
------------------

I am sorry, but at the moment there is only a very short
introduction about how to use Vitamin. Have a look at the
images in the directory "screenshots" and read the txt file
in this directory to get a short overview how to use this
software.


Plugins
-------

All protocols Vitamin can visualize are given as XML based
plugins. All plugins have to be in the directory "plugins"
in the Vitamin directory. See "doc/how_to_write_a_plugin.txt"
for more information about plugins.


Network topology
----------------

Vitamin can not detect the topology of the network it is
used in automatically. Therefore you have to describe the
network you want to visualize in a XML file. See
"doc/how_to_specify_the_network_topology.pdf" for more
information about that.


Known Bugs
----------

none

If you find bugs please feel free to contact me
(martin.pelzer@fokus.fraunhofer.de).
