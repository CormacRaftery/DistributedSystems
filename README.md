# DistributedSystems
## Cormac Raftery G00348802

Github link: https://github.com/CormacRaftery/DistributedSystems

The 2 jars were made using the integrated service in intelliJ as I found the one in eclipse to be quite tedious but the main project was developed in eclipse.

All the code for the main project is derived from https://github.com/john-french/distributed-systems-labs/tree/master/grpc-async-inventory.

To run the service you must first clone the repository and then run the 2 commands in order and in 2 different terminals:
java -jar grpcpasswordservice.jar

java -jar ClientJar.jar

The first command here gets the server running which is why it is important to run first and then the second command gets the client running which connects to the server.
