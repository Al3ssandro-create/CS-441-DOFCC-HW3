package Controllers

import GraphManager.{CustomGraph, CustomNode, GameStrategy, GraphReader}
import Model.{Policeman, Thief}
import com.google.common.graph.MutableGraph
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

import scala.collection.convert.ImplicitConversions.`set asScala`

case class GameState(graph_n: MutableGraph[Int],graph_p:MutableGraph[Int], valuableDataNode : List[CustomNode], policemanPosition: Int, thiefPosition: Int, sessions: Map[String, String])

object GameState {
  private val logger = LoggerFactory.getLogger(GameState.getClass)
  private val config: Config = ConfigFactory.load("homework3.conf")
  private val dir: String = config.getString("App.graph.dir")
  private val n_path: String = config.getString("App.graph.normal")
  private val p_path: String = config.getString("App.graph.perturbed")
  private val n_graph: CustomGraph = GraphReader.initialize(s"$dir/$n_path")
  private val p_graph: CustomGraph = GraphReader.initialize(s"$dir/$p_path")
  private val valuableDataNode: List[CustomNode] = n_graph.nodes.filter(node => node.valuableData)
  val policeman = new Policeman()
  val thief = new Thief()
  private val guava_p_graph: MutableGraph[Int] = GraphReader.toGuavaGraph(p_graph)
  private val guava_n_graph: MutableGraph[Int] = GraphReader.toGuavaGraph(n_graph)

  def start(gameState: GameState): GameState = {
    logger.info("Starting game with initial state")
    logger.debug(s"Initial game state: $gameState")
    GameState(guava_n_graph, guava_p_graph, valuableDataNode, 1, 5, gameState.sessions)
  }

  def neighbours(gameState: GameState): (List[Int], List[Int], Double, Double) = {
    logger.debug(s"Fetching neighbors for gameState: $gameState")
    val list1 = guava_n_graph.adjacentNodes(gameState.policemanPosition).toList
    val list2 = guava_n_graph.adjacentNodes(gameState.thiefPosition).toList
    val list3 = guava_p_graph.adjacentNodes(gameState.policemanPosition).toList
    val list4 = guava_p_graph.adjacentNodes(gameState.thiefPosition).toList
    val commonElements = list3.intersect(list1)
    val commonElements2 = list4.intersect(list2)
    (list3, list4, commonElements.size.toDouble / list3.size.toDouble, commonElements2.size.toDouble / list4.size.toDouble)
  }

  def connect(gameState: GameState, sessionId: String, role: String): GameState = {
    gameState.sessions.get(sessionId) match {
      case Some(_) => gameState // No change if the session already exists
      case None => GameState(guava_n_graph, guava_p_graph, valuableDataNode, gameState.policemanPosition, gameState.thiefPosition, gameState.sessions + (sessionId -> role))
    }

  }

  def checkGameStatus(gameState: GameState): String = {
    logger.debug(s"Checking game status for gameState: $gameState")
    val listValuableNode = valuableDataNode.map(node => node.id)
    if (gameState.policemanPosition == gameState.thiefPosition) {
      "Policeman wins!"
    } else if(listValuableNode.contains(gameState.thiefPosition)) {
      "Thief wins!"
    } else if(guava_n_graph.adjacentNodes(gameState.policemanPosition).isEmpty || guava_n_graph.adjacentNodes(gameState.thiefPosition).isEmpty){
      "Game Over"
    } else {
      "Game continues"
    }
  }

  def makeMove(gameState: GameState, playerId: String, moveTo: Int): GameState = {
    logger.info(s"Player $playerId attempting to move to $moveTo")
    if (gameState.sessions(playerId) == "Policeman") {
      if (guava_n_graph.adjacentNodes(gameState.policemanPosition).contains(moveTo)) {
        GameState(guava_n_graph, guava_p_graph, valuableDataNode, moveTo, gameState.thiefPosition, gameState.sessions)
      } else {
        gameState // No change if the move is invalid
      }
    } else if (gameState.sessions(playerId) == "Thief") {
      if (guava_n_graph.adjacentNodes(gameState.thiefPosition).contains(moveTo)) {
        GameState(guava_n_graph, guava_p_graph, valuableDataNode, gameState.policemanPosition, moveTo, gameState.sessions)
      } else {
        gameState // No change if the move is invalid
      }
    } else {
      gameState
    }
  }
  def hint(sessionId: String, gameState: GameState): Option[Int] = {
    logger.debug(s"Providing hint for session: $sessionId")
    if(gameState.sessions(sessionId)== "Thief") {
      val hint = GameStrategy.findPath(guava_n_graph, gameState.thiefPosition, valuableDataNode.head.id)
      if(hint.length == 1){
         None
      }else {
        Some(hint(hint.length - 2))
      }
    }else if(gameState.sessions(sessionId)== "Policeman"){
      val hint = GameStrategy.findPath(guava_n_graph, gameState.policemanPosition, gameState.thiefPosition)
      if (hint.length == 1) {
        None
      } else {
        Some(hint(hint.length - 2))
      }
    }else{
      None
    }
  }
}