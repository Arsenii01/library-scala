package auth

import play.api.mvc.Request

case class RequestContext[C](request: Request[C], token: Option[String])
