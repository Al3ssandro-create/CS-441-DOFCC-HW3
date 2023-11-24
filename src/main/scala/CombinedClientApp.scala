import scalaj.http._

import scala.concurrent.{ExecutionContext, Future}
import scala.sys.exit
import scala.util.{Failure, Success}

object CombinedClientApp{
  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.global

    def parse(inputString: String): (List[Int], List[Int]) = {
      // Extracting the list parts from the string
      val listPattern = "List\\((.*?)\\)".r
      val listStrings = listPattern.findAllIn(inputString).toList

      // Parsing each lisnt string into a List[Int]
      val parsedLists = listStrings.map { listString =>
        listString.stripPrefix("List(").stripSuffix(")").split(", ").map(_.toInt).toList
      }

      // Extracted lists
      val list1 = parsedLists.headOption.getOrElse(List.empty[Int])
      val list2 = if (parsedLists.length > 1) parsedLists(1) else List.empty[Int]
      (list1, list2)
    }

    def hint(sessionId: String): Future[String] = Future {
      Http("http://localhost:8080/hint")
        .postData(s"$sessionId")
        .asString
        .body
    }

    def check(sessionId: String): Future[String] = Future {
      Http("http://localhost:8080/check")
        .postData(s"$sessionId")
        .asString
        .body
    }

    def connectAsRole(sessionId: String, role: String): Future[String] = Future {
      Http("http://localhost:8080/connect")
        .postData(s"$sessionId $role")
        .asString
        .body
    }

    def makeMove(sessionId: String, moveTo: Int): Future[String] = Future {
      Http("http://localhost:8080/move")
        .postData(s"$sessionId $moveTo")
        .asString
        .body
    }

    def start(sessionId: String): Future[String] = Future {
      Http("http://localhost:8080/start")
        .postData(s"$sessionId")
        .asString
        .body
    }

    val thiefFuture = connectAsRole("12345", "Thief")
    val policemanFuture = connectAsRole("67890", "Policeman")
    thiefFuture.onComplete {
      case Success(response) => println(s"Thief client response: $response")
      case Failure(exception) => println(s"Thief client failed: ${exception.getMessage}")
    }
    Thread.sleep(2000)
    policemanFuture.onComplete {
      case Success(response) => println(s"Policeman client response: $response")
      case Failure(exception) => println(s"Policeman client failed: ${exception.getMessage}")
    }
    Thread.sleep(2000)
    var list = (List.empty[Int], List.empty[Int])
    var PoliceMan_Hint = -1
    var Thief_Hint = -1


    val futureMoves = start("start")
    futureMoves.onComplete {
      case Success(response) =>
        println(s"Start client response: $response")
        list = parse(response)
      case Failure(exception) => println(s"Start client failed: ${exception.getMessage}")
    }
    Thread.sleep(2000)
    while (list._1.nonEmpty || list._2.nonEmpty) {
      Thief_Hint = -1
      PoliceMan_Hint = -1
      Thread.sleep(2000)
      val requestHint = hint("12345")
      requestHint.onComplete {
        case Success(response) =>
          println(s"Thief hint response: $response")
          Thief_Hint = response.toInt
        case Failure(exception) => println(s"Thief hint failed: ${exception.getMessage}")
      }
      Thread.sleep(2000)
      val actualMove = makeMove("12345", if (Thief_Hint != -1) Thief_Hint else list._2.head)
      actualMove.onComplete {
        case Success(response) => println(s"Thief client moved response: $response")
        case Failure(exception) => println(s"Thief client moved failed: ${exception.getMessage}")
      }
      Thread.sleep(2000)
      val checkFuturePre = check("attempt")
      checkFuturePre.onComplete {
        case Success(response) => println(s"Check client response: $response")
        case Failure(exception) => println(s"Check client failed: ${exception.getMessage}")
      }
      Thread.sleep(2000)
      val requestHint2 = hint("67890")
      requestHint2.onComplete {
        case Success(response) =>
          println(s"Policeman hint response: $response")
          PoliceMan_Hint = response.toInt
        case Failure(exception) => println(s"Policeman hint failed: ${exception.getMessage}")
      }
      Thread.sleep(2000)
      val actualMove2 = makeMove("67890", if (PoliceMan_Hint != -1) PoliceMan_Hint else list._1.head)
      actualMove2.onComplete {
        case Success(response) =>
          println(s"Policeman client moved response: $response")
          list = parse(response)
        case Failure(exception) => println(s"Policeman client moved failed: ${exception.getMessage}")
      }
      Thread.sleep(2000)
      val checkFuture = check("attempt")
      checkFuture.onComplete {
        case Success(response) =>
          println(s"Check client response: $response")
          if (response == "Policeman wins!" || response == "Thief wins!" || response == "Game Over") {
            exit(0)
          }
        case Failure(exception) => println(s"Check client failed: ${exception.getMessage}")
      }
      Thread.sleep(2000)
    }
  }
}
