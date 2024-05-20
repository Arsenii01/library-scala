package auth

import com.google.inject.Inject
import models.dto.AuthDto
import play.api.libs.json.Json
import play.api.mvc.Action

class RegisterController @Inject()(val authService: AuthorizationService) extends Authorization {

  def registerAdmin = authorizeSuperAdmin(parse.json[AuthDto]) { req =>
    val body = req.request.body
    authService.registerAdmin(body) match {
      case Some(user) => Ok(Json.toJson(user))
      case _ => BadRequest("User with this username already exists")
    }
  }

  def registerUser = Action(parse.json[AuthDto]) { request =>
    authService.registerUser(request.body) match {
      case Some(user) => Ok(Json.toJson(user))
      case _ => BadRequest("User with this username already exists")
    }
  }
}
