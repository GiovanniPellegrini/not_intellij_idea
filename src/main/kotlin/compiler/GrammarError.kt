package compiler

class GrammarError(location: SourceLocation, message:String):RuntimeException(message)