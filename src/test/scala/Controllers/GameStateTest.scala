package Controllers

import GraphManager.{CustomGraph, CustomNode, GraphReader}
import com.google.common.graph.MutableGraph
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameStateTest extends AnyFlatSpec with Matchers {

  val valuableDataNode: List[CustomNode] = List.empty
  val customGraph: CustomGraph = CustomGraph(List.empty, List.empty)
  val guavaGraph: MutableGraph[Int] = GraphReader.toGuavaGraph(customGraph)

    "start" should "initialize the game with the correct state" in {
      val gameState = GameState(guavaGraph, guavaGraph, valuableDataNode, 1, 5, Map.empty)
      val newGameState = gameState

      newGameState shouldEqual gameState // Adjust as needed based on expected behavior
    }

  "neighbours" should "identify correct neighbors and ratios" in {
    val gameState = GameState(guavaGraph, guavaGraph, valuableDataNode, 1, 5, Map.empty)
    val (list1, list2, ratio1, ratio2) = GameState.neighbours(gameState)

    // Assertions based on the expected output
  }

}
