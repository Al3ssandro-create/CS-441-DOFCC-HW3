package Model

import Controllers.GameState
import Messages._
import akka.actor.Actor
import com.google.common.graph.GraphBuilder
import org.slf4j.LoggerFactory

class GameStateActor extends Actor {
  private val logger = LoggerFactory.getLogger(GameState.getClass)
  private var state: GameState = GameState(GraphBuilder.directed().build[Int](),GraphBuilder.directed().build[Int](), List.empty, 0, 0, Map.empty)
  def receive: Receive = {
    case Start(sessionID: String) =>
      logger.info(s"Starting game session for sessionID: $sessionID")
      val replyTo = sender()
      if (state.sessions.size == 2) {
        logger.debug("Enough players to start the game, initializing game state")
        state = GameState.start(state)
        val (list1,list2,score1,score2) = GameState.neighbours(state)
        replyTo ! NeighbourMessage(state.policemanPosition,state.thiefPosition,list1,list2,score1,score2)
      } else {
        logger.warn("Not enough players to start the game")
        replyTo ! FailureMessage("Not enough players to start the game")
      }
    case Connect(sessionId, role) =>
      logger.info(s"Player with sessionID: $sessionId attempting to connect as $role")
      val replyTo = sender()
      if (state.sessions.size < 2) {
        if (isValidRole(role) && !roleExists(state, role)) {
          val updatedState = GameState.connect(state, sessionId, role)
          if (updatedState == state) {
            replyTo ! FailureMessage(s"Session $sessionId already exists")
          } else {
            state = updatedState
            replyTo ! SuccessMessage(role)
          }
        } else if (!isValidRole(role)) {
          replyTo ! FailureMessage(s"Role $role is not valid")
        } else if (roleExists(state, role)) {
          replyTo ! FailureMessage(s"Role $role is already taken")
        }
      } else {
        replyTo ! FailureMessage("Server is full")
      }
    case Check(sessionId) =>
      logger.debug(s"Checking game status for sessionID: $sessionId")
      val replyTo = sender()
      val status = GameState.checkGameStatus(state)
      if(status == "Policeman wins!" || status == "Thief wins!"){
        replyTo ! WinMessage(status)
      }else if(status == "Game Over"){
        replyTo ! LoseMessage(status)
      }else{
        replyTo ! ContinueMessage(status)
      }
    case MakeMove(sessionId, moveTo) =>
      logger.info(s"Player $sessionId making a move to $moveTo")
      val updatedState = GameState.makeMove(state, sessionId, moveTo)
      state = updatedState
      val (list1, list2, score1, score2) = GameState.neighbours(state)
      sender() ! NeighbourMessage(state.policemanPosition, state.thiefPosition, list1, list2, score1, score2)
    case Hint(sessionId) =>
      logger.debug(s"Generating a hint for sessionID: $sessionId")
      val replyTo = sender()
      if(state.sessions.size==2) {
        val hint = GameState.hint(sessionId, state)
        hint match {
          case Some(value) =>  replyTo ! HintMessage(value.toString)
          case None => replyTo ! FailureMessage("No hint available")
        }
      }else{
        replyTo ! FailureMessage("Game Not Started")
      }



  }

  private def isValidRole(role: String): Boolean = {
    if (role == "Policeman" || role == "Thief") true else false
  }

  private def roleExists(state: GameState, role: String): Boolean = {
    state.sessions.values.exists(_ == role)
  }
}
