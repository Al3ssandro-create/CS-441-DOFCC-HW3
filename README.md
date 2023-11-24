
# Scala Project Overview
Alessandro Martinolli  
E-mail: [amart409@uic.edu](amart409@uic.edu)  
Video: []()
This document offers a detailed overview of the classes and objects defined in the Scala project.
## Features
- All the base features of the game are implemented.
- TODO: Add lambda functions to the game with GRPC.
## Overview
This Scala project provides a raw implemntation of a distributed game of cops and robbers. It is designed to facilitate the execution of the game in a distributed computing environment.
## Prerequisites

- SBT (Scala Build Tool) or similar build tool
- Scala

## Installation

To set up the project, clone the repository to your local machine or server where Spark is installed:

```sh
git clone https://github.com/Al3ssandro-create/CS-484-HW3
cd your-project-directory
```
Build the project using SBT:
```sh
sbt clean compile
```
Generate the jar file
```sh
sbt assembly
```
## Usage

To run the application, submit the job to Spark using the `spark-submit` command:

```sh
sbt run //run the webserver
sbt "runMain CombinedClientApp" //to run the automatic client make sure to create a new sbt server
```


Compile and test the program using SBT
```sh
sbt clean test
```
## Structure

- **CombinedClientApp.scala:** Main entry for client application.
- **Routes.scala:** Defines HTTP routes for web traffic.
- **WebServer.scala:** Core web server functionality.
- **Message.scala:** Classes for various communication messages.
- **GameStrategy.scala:** Encapsulates game strategies and algorithms.
- **GraphReader.scala:** Reads and processes graph data.
- **GameStateActor.scala:** Manages game state.
- **Player.scala:** Utilities related to players.
- **Policeman.scala/Thief.scala:** Represents specific roles in the game.
- **GameState.scala:** Manages overall game state.
- **Node.scala:** Fundamental element in graph structures.

## Additional Information
- Detailed logging via slf4j.
- Commented sections for alternative workflows.
- For JSON file conversion, refer to: [https://github.com/Al3ssandro-create/NGSconverter].
