package Model

import Controllers.GameState

class Policeman extends Player {
  override def move(gameState: GameState): GameState = {
    val (list1,list2,score1,score2) = GameState.neighbours(gameState)
    val moveTo = list1.head
    GameState.makeMove(gameState, "67890", moveTo)
  }
}
