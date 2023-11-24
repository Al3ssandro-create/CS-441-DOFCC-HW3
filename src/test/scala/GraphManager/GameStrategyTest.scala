package GraphManager

import com.google.common.graph.GraphBuilder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameStrategyTest extends AnyFlatSpec with Matchers {

  "findPath" should "find a path when it exists" in {
    val graph = GraphBuilder.directed().build[Int]()
    graph.putEdge(1, 2)
    graph.putEdge(2, 3)
    graph.putEdge(3, 4)

    GameStrategy.findPath(graph, 4, 1) shouldEqual List(1, 2, 3, 4)
  }

  it should "return an empty list when no path exists" in {
    val graph = GraphBuilder.directed().build[Int]()
    graph.putEdge(1, 2)
    graph.putEdge(3, 4)

    GameStrategy.findPath(graph, 4, 1) shouldBe empty
  }

  it should "return the start node when start equals target" in {
    val graph = GraphBuilder.directed().build[Int]()
    graph.putEdge(1, 2)

    GameStrategy.findPath(graph, 1, 1) shouldEqual List(1)
  }

  it should "handle complex graph structures" in {
    val graph = GraphBuilder.directed().build[Int]()
    // Add edges to create a more complex graph
    graph.putEdge(1, 2)
    graph.putEdge(2, 3)
    graph.putEdge(3, 4)
    graph.putEdge(4, 5)
    graph.putEdge(2, 6)
    graph.putEdge(6, 5)

    GameStrategy.findPath(graph, 5, 1) shouldEqual List(1, 2, 6, 5)
  }
}
