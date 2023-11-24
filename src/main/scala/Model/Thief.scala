package Model

import Controllers.GameState

class Thief extends Player {
  override def move(gameState: GameState): GameState = {
    val (list1,list2,score1,score2) = GameState.neighbours(gameState)
    val moveTo = list2.head
    GameState.makeMove(gameState, "12345", moveTo)
  }

}
