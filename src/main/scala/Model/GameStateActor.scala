package Model

import Controllers.GameState
import GraphManager.GraphReader
import Messages.{FailureMessage, SuccessMessage}
import akka.actor.Actor
import com.typesafe.config.{Config, ConfigFactory}

class GameStateActor extends Actor {
  private var state: GameState = GameState(Map.empty, 0, 0, Map.empty, None)
  private val config: Config = ConfigFactory.load("App")
  private val dir: String = config.getString("App.graph.dir")
  private val n_path: String = config.getString("App.graph.normal")
  private val p_path: String = config.getString("App.graph.perturbed")
  val n_graph = GraphReader.initialize(s"$dir$n_path")
  println(s"the n_graph is $n_graph")
  val p_graph = GraphReader.initialize(s"$dir$p_path")
  println(s"the p_graph is $p_graph")

  def receive: Receive = {
    case Connect(sessionId, role) =>
      println(state.sessions.size)
      val replyTo = sender()
      if (state.sessions.size < 2) {
        if (isValidRole(role) && !roleExists(state, role)) {
          val updatedState = GameState.connect(state, sessionId, role)
          if (updatedState == state) {
            replyTo ! FailureMessage(s"Session $sessionId already exists")
          } else {
            state = updatedState
            replyTo ! SuccessMessage(role)
            println(s"the updatedState is $updatedState")
            println(s"the state is $state")
          }
        } else if (!isValidRole(role)) {
          replyTo ! FailureMessage(s"Role $role is not valid")
        } else if (roleExists(state, role)) {
          replyTo ! FailureMessage(s"Role $role is already taken")
        }
      } else {
        replyTo ! FailureMessage("Server is full")
      }
    case Disconnect(sessionId) =>
      if (state.sessions.contains(sessionId)) {
        // Remove the player with the given sessionId
        val updatedState = state.copy(sessions = state.sessions - sessionId)
        state = updatedState
        sender() ! s"Player $sessionId disconnected"
      } else {
        sender() ! s"No player found with sessionId: $sessionId"
      }
    case MakeMove(sessionId, moveTo) =>
      val updatedState = GameState.makeMove(state, sessionId, moveTo)
      state = updatedState
      sender() ! GameState.checkGameStatus(updatedState)
  }

  private def isValidRole(role: String): Boolean = {
    if (role == "Policeman" || role == "Thief") true else false
  }

  private def roleExists(state: GameState, role: String): Boolean = {
    state.sessions.values.exists(_ == role)
  }
}
