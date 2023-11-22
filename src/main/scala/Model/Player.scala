package Model

import Controllers.GameState

trait Player {
  def move(gameState: GameState): GameState
}
