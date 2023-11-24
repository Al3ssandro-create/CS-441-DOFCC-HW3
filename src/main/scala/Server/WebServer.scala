package Server

import Model.GameStateActor
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor

object WebServer {


  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load("homework3.conf")
    val host: String = config.getString("App.server.host")
    val port: Int = config.getInt("App.server.port")

    implicit val system: ActorSystem = ActorSystem("policeman-thief-game")
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    val gameStateActor = system.actorOf(Props[GameStateActor], "gameStateActor")
    //below here put to 0.0.0.0 for running with docker or on localhost for running locally
    Http().newServerAt(s"$host", port)
      .bindFlow(Routes.apply(gameStateActor))
    println(s"Server online at http://$host:$port/")
  }



}
