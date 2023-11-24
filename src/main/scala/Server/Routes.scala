package Server

import Messages._
import Model._
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.sys.exit
import scala.util.Success

object Routes {
  def apply(gameStateActor: ActorRef)(implicit system: ActorSystem, ec: ExecutionContext): Route = {
    implicit val timeout: Timeout = Timeout(50.seconds) // For the ask pattern

    val gameRoute: Route = {
      path("connect") {
        post {
          entity(as[String]) { connectionCommand =>
            val parts = connectionCommand.split(" ")
            val sessionId = parts(0)
            val chosenRole = parts(1)
            onComplete(gameStateActor ? Connect(sessionId, chosenRole)) {
              case Success(SuccessMessage(role)) => complete(s"The role assigned is $role")
              case Success(FailureMessage(errorMessage)) => complete(StatusCodes.InternalServerError, errorMessage)
              case _ => complete(StatusCodes.InternalServerError, "An unexpected error occurred")
            }
          }
        }
      } ~
      path("move") {
        post {
          entity(as[String]) { moveCommand =>
            val parts = moveCommand.split(" ")
            val sessionId = parts(0)
            val moveTo = parts(1).toInt
            onComplete(gameStateActor ? MakeMove(sessionId, moveTo)) {
              case Success(NeighbourMessage(policemanPosition: Int, thiefPosition: Int, neighbourPolice: List[Int], neighbourThief: List[Int], score1, score2)) => complete(s"${policemanPosition}, ${thiefPosition}, ${neighbourPolice}, ${neighbourThief}, ${score1}, ${score2}}")
              case _ => complete("Error processing request")
            }
          }
        }
      } ~
      path("start") {
        post{
          entity(as[String]) { startCommand =>
            val parts = startCommand.split(" ")
            val sessionId = parts(0)
            onComplete(gameStateActor ? Start(sessionId)) {
              case Success(NeighbourMessage(policemanPosition:Int,thiefPosition:Int,neighbourPolice:List[Int],neighbourThief: List[Int],score1,score2)) => complete(s"${policemanPosition}, ${thiefPosition}, ${neighbourPolice}, ${neighbourThief}, ${score1}, ${score2}}")
              case _ => complete("Error processing request")
            }
          }
        }
      } ~
      path("hint"){
        post{
          entity(as[String]) { hintCommand =>
            val parts = hintCommand.split(" ")
            val sessionId = parts(0)
            onComplete(gameStateActor ? Hint(sessionId)) {
              case Success(HintMessage(hint)) => complete(hint)
              case _ => complete("Error processing request")
            }
          }
        }
      } ~
      path("check"){
        post{
          entity(as[String]) { checkCommand =>
            val parts = checkCommand.split(" ")
            val sessionId = parts(0)
            onComplete(gameStateActor ? Check(sessionId)) {
              case Success(WinMessage(message)) => complete(s"${message}")
              case Success(LoseMessage(message)) => complete(s"${message}")
              case Success(ContinueMessage(message)) => complete(s"${message}")
              case _ => complete("Error processing request")
            }
          }
        }
      }
    }
  gameRoute
  }
}
