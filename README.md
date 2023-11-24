
# Scala Project Overview

This document offers a detailed overview of the classes and objects defined in the Scala project.

## Files and Their Contents

### CombinedClientApp.scala
- **Object: CombinedClientApp**
    - Serves as the main entry point for a client application, integrating various functionalities and modules.

### Routes.scala
- **Object: Routes**
    - Responsible for defining and managing the HTTP routes used in the web server, essential for directing web traffic.

### WebServer.scala
- **Object: WebServer**
    - Implements the core functionality of a web server, handling requests and responses in the networked environment.

### Message.scala
- **Classes:**
    - **SuccessMessage, FailureMessage, NeighbourMessage, WinMessage, LoseMessage, ContinueMessage, HintMessage**: These classes represent different types of messages exchanged in the application, each tailored for specific communication needs and scenarios.

### CustomEdge.scala
- **Class: CustomEdge**
    - Defines a specialized edge for graph structures, possibly including additional properties or methods unique to the application's context.

### CustomGraph.scala
- **Class: CustomGraph**
    - Represents a custom graph structure, extending or modifying standard graph functionalities to suit specific requirements of the application.

### CustomNode.scala
- **Class: CustomNode**
    - Describes a custom node for use in graph structures, potentially enriched with additional attributes or behaviors.

### GameStrategy.scala
- **Object: GameStrategy**
    - Encapsulates various strategies or algorithms used in game mechanics, possibly including decision-making or tactical processes.

### GraphReader.scala
- **Object: GraphReader**
    - Handles the reading, parsing, and processing of graph data from external sources, essential for graph construction and initialization.

### Check.scala
- **Class: Check**
    - Represents an action or a condition check within the application, possibly used for validation or state checking.

### Connect.scala
- **Class: Connect**
    - Manages the logic for establishing connections, likely within a networked or distributed environment.

### Disconnect.scala
- **Class: Disconnect**
    - Handles the disconnection logic, ensuring safe and clean disengagement from networks or services.

### GameStateActor.scala
- **Class: GameStateActor**
    - Acts as a central actor in managing the game state, crucial for maintaining the consistency and progression of the game.

### Hint.scala
- **Class: Hint**
    - Provides a mechanism for generating or displaying hints, potentially aiding in decision-making or problem-solving within the game.

### MakeMove.scala
- **Class: MakeMove**
    - Central to the game's mechanics, this class manages the actions related to making moves or taking steps in the game.

### Player.scala
- **No specific classes/objects**
    - This file may include utility functions, data structures, or implicit definitions related to players in the game.

### Policeman.scala
- **Class: Policeman**
    - Represents the character or role of a policeman within the game, with functionalities and attributes specific to this role.

### Start.scala
- **Class: Start**
    - Initiates various processes or sequences in the application, possibly triggering the beginning of a game or a series of actions.

### Thief.scala
- **Class: Thief**
    - Embodies the character or role of a thief in the game, equipped with unique capabilities or strategies.

### GameState.scala
- **Class & Object: GameState**
    - Represents and manages the overall state of the game, crucial for tracking progress, outcomes, and player interactions.

### Node.scala
- **Class: Node**
    - Serves as a fundamental element in graph-based structures, possibly involved in representing locations, points in a network, or decision nodes.

---