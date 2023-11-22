package Messages

// Define a base trait for messages
sealed trait Message

// Define a case class for success messages
case class SuccessMessage(details: String) extends Message

// Define a case class for failure messages
case class FailureMessage(error: String) extends Message
