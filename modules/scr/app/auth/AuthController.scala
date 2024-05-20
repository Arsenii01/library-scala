package auth

import com.google.inject.Inject
import models.dto.AuthDto
import play.api.libs.Crypto
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class AuthController @Inject()(val authService: AuthorizationService) extends Controller {

  def getEncypt() = Action(parse.json[AuthDto]) {
    request =>
      Ok(Json.toJson(Crypto.encryptAES(request.body.password)))
  }
  def login() = Action(parse.json[AuthDto]) { request =>
    authService.login(request.body) match {
      case Some(token) => Ok(Json.toJson(token))
      case _ => Unauthorized("Username or password are incorrect")
    }
  }



  def logout() = Action { request =>
    val token = request.headers.get("Authorization")
    token match {
      case Some(t) =>
        if (authService.logout(t).isSuccess) Ok("logout successfull")
        else Unauthorized("token not found")
      case _ => Forbidden
    }
  }

}
