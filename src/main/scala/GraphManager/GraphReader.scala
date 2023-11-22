package GraphManager
import org.json4s._
import org.json4s.native.JsonMethods._
import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source

object GraphReader {
  def initialize(path: String): Option[CustomGraph] = {
    val logger: Logger = LoggerFactory.getLogger(getClass)
    implicit val formats: Formats = DefaultFormats

    val source = if (path.startsWith("http") || path.startsWith("https")) {
      Source.fromURL(path)
    } else {
      Source.fromFile(path)
    }
    try {
      val json = parse(source.mkString)
      source.close()

      json.extractOpt[CustomGraph] match {
        case Some(customGraph) =>
          val nodes = customGraph.nodes.map(n => CustomNode(n.id, n.children, n.props, n.currentDepth, n.propValueRange, n.maxDepth, n.maxBranchingFactor, n.maxProperties, n.storedValue, n.valuableData))
          val edges = customGraph.edges.map(e => CustomEdge(e.fromId, e.toId, e.weight))

          Some (CustomGraph(nodes, edges))

        case None =>
          None
      }
    } catch {
      case e: Exception =>
        logger.error("error in GraphReader.initialize: " + e.getMessage)
        None
    }
  }
}
