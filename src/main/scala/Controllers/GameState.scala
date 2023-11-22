package Controllers

case class GameState(nodes: Map[Int, Node], policemanPosition: Int, thiefPosition: Int, sessions: Map[String, String], currentPlayerTurn: Option[String])

object GameState {
  def initialize(numNodes: Int, edges: List[(Int, Int)], valuableDataNode: Int): GameState = {
    val nodes = (1 to numNodes).map { id =>
      val isValuableData = id == valuableDataNode
      val neighbors = edges.filter(edge => edge._1 == id || edge._2 == id)
        .flatMap(edge => List(edge._1, edge._2))
        .distinct
        .filter(_ != id)
      Node(id, isValuableData, neighbors)
    }.map(node => node.id -> node).toMap

    GameState(nodes, 1, numNodes, Map.empty, None) // Assuming Model.Policeman starts at node 1 and Model.Thief at the last node
  }

  def connect(gameState: GameState, sessionId: String, role: String): GameState = {
    //println(s"the gameState is $gameState")
    //println(s"the sessionId is $sessionId")
    gameState.sessions.get(sessionId) match {
      case Some(_) => gameState // No change if the session already exists
      case None => GameState(gameState.nodes, gameState.policemanPosition, gameState.thiefPosition, gameState.sessions + (sessionId -> role), gameState.currentPlayerTurn)
    }

  }

  def checkGameStatus(gameState: GameState): String = {
    if (gameState.policemanPosition == gameState.thiefPosition) {
      "Model.Policeman wins!"
    } else if (gameState.nodes(gameState.thiefPosition).valuableData) {
      "Model.Thief wins!"
    } else {
      "Game continues"
    }
  }

  def makeMove(gameState: GameState, playerId: String, moveTo: Int): GameState = {
    if (gameState.currentPlayerTurn.contains(playerId)) {
      gameState.sessions.get(playerId) match {
        case Some("Model.Policeman") if gameState.policemanPosition != moveTo =>
          val nextTurn = gameState.sessions.find(_._2 == "Model.Thief").map(_._1)
          gameState.copy(policemanPosition = moveTo, currentPlayerTurn = nextTurn)
        case Some("Model.Thief") if gameState.thiefPosition != moveTo =>
          val nextTurn = gameState.sessions.find(_._2 == "Model.Policeman").map(_._1)
          gameState.copy(thiefPosition = moveTo, currentPlayerTurn = nextTurn)
        case _ => gameState // No change if the move is invalid or it's not the player's turn
      }
    } else {
      gameState // No change if it's not the player's turn
    }
  }
}