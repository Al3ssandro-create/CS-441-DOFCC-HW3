package Model

import Controllers.GameState

import scala.util.Random

class Thief extends Player {
  override def move(gameState: GameState): GameState = {
    val thiefCurrentPosition = gameState.thiefPosition
    val neighbors = gameState.nodes(thiefCurrentPosition).neighbors

    val newThiefPosition = if (neighbors.nonEmpty) {
      // Move to a random neighbor
      neighbors(Random.nextInt(neighbors.length))
    } else {
      // No move possible
      thiefCurrentPosition
    }

    gameState.copy(thiefPosition = newThiefPosition)
  }
}
