package Server

import Model.GameStateActor
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContextExecutor

object WebServer {


  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("policeman-thief-game")
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    val gameStateActor = system.actorOf(Props[GameStateActor], "gameStateActor")
    //below here put to 0.0.0.0 for running with docker or on localhost for running locally
    Http().newServerAt("0.0.0.0", 8080)
      .bindFlow(Routes.apply(gameStateActor))
    println("Server online at http://localhost:8080/")
  }



}
