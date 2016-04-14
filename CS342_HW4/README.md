#README

CS 342 Spring 2016 

Problem 4

Networked Chat Application

Shen Wang (swang224@uic.edu)

Yunxiao Cui (ycui22@uic.edu)

Yue Yu (yyu31@uic.edu)

Our team created a set of GUI based programs in Java Swing that will allow multiple people to connect together and send messages to specific connected person or to the entire group.  
This program only includes the Java Socket and ServerSocket classes. There are not other networking related classes are using in this program. 

All clients will connect to the central server. When sending a message, the client will first send
the message to the central server which will then forward the message to the desired client or
clients. A clients does not connect directly to another client. We will assume that the central
server's network information (IP address and port number) is "well known".

Each client will display a list of the persons currently connected to the central server. As a new
person connects to the central server, a displayed list of names at each client needs to be updated to reflect the new person joining the group (or chat room). Also as a person disconnects/leaves from the central server, the displayed list of names at each client need to again be updated.

When a message is send by a client, the client is to have the option to send the message to all currently connected clients or to just a single specific client. The client is to show a list of messages received and who sent those messages.

GUI_client: the GUI of client.

GUI_server: the GUI of server.

Client: will show your status such as login successful, login failed and connection failed. You can use Client to send your massage to the server and then to the others. 

Server: the message you received will also be shown. The Server is used to open a port and connection for the clients. It will shows the status of all users and receive all messages then send to clients. 

Message: defines the format of the message, which is send by client.
