package Model

import Controllers.GameState

class Policeman extends Player {
  override def move(gameState: GameState): GameState = {
    val thiefPosition = gameState.thiefPosition
    val policemanCurrentPosition = gameState.policemanPosition
    val thiefNeighbors = gameState.nodes(thiefPosition).neighbors

    val newPolicemanPosition = if (thiefNeighbors.contains(policemanCurrentPosition)) {
      // Policeman catches the Thief
      thiefPosition
    } else {
      // Move towards one of the Thief's neighbors, if possible
      thiefNeighbors.headOption.getOrElse(policemanCurrentPosition)
    }

    gameState.copy(policemanPosition = newPolicemanPosition)
  }
}
