package GraphManager

import com.google.common.graph.MutableGraph
import scala.collection.mutable
object GameStrategy {

  def findPath(graph: MutableGraph[Int], targetNode: Int, startNode: Int): List[Int] = {
    if (startNode == targetNode) return List(startNode)

    val visited = mutable.Set[Int]()
    val queue = mutable.Queue[List[Int]]()

    queue.enqueue(List(startNode))
    visited.add(startNode)

    while (queue.nonEmpty) {
      val path = queue.dequeue()
      val lastNode = path.last

      val neighbors = graph.adjacentNodes(lastNode)
      neighbors.forEach { neighbor =>
        if (!visited.contains(neighbor)) {
          visited.add(neighbor)
          val newPath = path :+ neighbor
          if (neighbor == targetNode) {
            return newPath
          }
          queue.enqueue(newPath)
        }
      }
    }

    List.empty // Return an empty list if no path is found
  }



}
