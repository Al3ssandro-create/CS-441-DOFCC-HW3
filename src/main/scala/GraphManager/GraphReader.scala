package GraphManager
import com.google.common.graph.{GraphBuilder, MutableGraph}
import org.json4s._
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.control.NonFatal

object GraphReader {
  private val logger = LoggerFactory.getLogger(GraphReader.getClass)

  def initialize(path: String): CustomGraph = {
    implicit val formats: Formats = DefaultFormats

    try {
      logger.info(s"Attempting to read from path: $path")
      val source = if (path.startsWith("http") || path.startsWith("https")) {
        Source.fromURL(path)
      } else {
        Source.fromFile(path)
      }

      logger.debug("Source acquired, proceeding to parse JSON.")
      val jsonStr = source.mkString
      source.close()

      logger.debug("Parsing JSON.")
      val json = parse(jsonStr)

      logger.debug("Extracting CustomGraph.")
      json.extract[CustomGraph]
    } catch {
      case NonFatal(e) =>
        logger.error(s"An error occurred: ${e.getMessage}")
        throw e
    }
  }

  def toGuavaGraph(customGraph: CustomGraph): MutableGraph[Int] = {
    val guavaGraph: MutableGraph[Int] = GraphBuilder.directed().build[Int]()

    logger.debug("Adding nodes and edges to the Guava graph.")
    // Adding nodes to the Guava graph
    customGraph.nodes.foreach(node => guavaGraph.addNode(node.id))

    // Adding edges to the Guava graph
    customGraph.edges.foreach(edge => guavaGraph.putEdge(edge.fromId, edge.toId))

    guavaGraph
  }
}
