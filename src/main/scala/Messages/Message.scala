package Messages

// Define a base trait for messages
sealed trait Message

// Define a case class for success messages
case class SuccessMessage(details: String) extends Message
// Define a case class for failure messages
case class FailureMessage(error: String) extends Message
case class NeighbourMessage(policemanPosition: Int, thiefPosition: Int, list1: List[Int], list2: List[Int], score1: Double, score2: Double) extends Message
case class WinMessage(message: String) extends Message
case class LoseMessage(message: String) extends Message
case class ContinueMessage(message: String) extends Message
case class HintMessage(hint: String) extends Message
