# Non-Standard Calculator Protocol

## Protocol objectives
This protocol will be used to implement a simple client-server calculator application.
The client will transmit mathematical operation through this protocol and the server will compute and give the result to the client. 

## Overall behavior

### Transport Protocol
This protocol will use "TCP" to transmit data between client-server. 

### How to find the server
This protocol will use a hostname and the port 80 to communicate.

### Who speak first
The client will speak first to send data (mathematical expressions) to the server.

### Connection close 
The server will close the connection as soon as he send a valid result to the client.

## Messages

### Syntax
"REQUESTED:Calculus"

Calculus will contain:

- Can't contain more than one calculus (to be defined)
- Any mathematical operators and any number of operators
- Spaces, tab will be ignored
- Extra parenthesis will be ignored
- Floating point must be '.' and not ','
- Must follow the mathematical order of operations to get a valid result


### Flow
It's a simple request between client and server, as TCP guarantees a ACK, we don't need to implement a "handshake" system.
The client send a calculus request and the server return the result if the calculus is valid.

### Semantics
1) The first message from the client will contain the hostname and port number to connect to the server
2) The second message from the client will contain a string with the calculus requested
3) The server will check if the requested calculus is valid
4) The server will return error message code if not valid (check specific elements) 
5) The server will return the result if valid
6) The server will close the connection

## Server outcome

- "UNSUPPORTEDOperator": the client request contain an operator that is not implemeted on the server.
- "REFUSED": the request is not valid (example: contain char)
- "VALID": send the result to the client
- "ERROR": mathematical error (division by 0)


