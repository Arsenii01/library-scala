package models.dto

import play.api.libs.json.Json

case class AuthDto(username: String, password: String)

object AuthDto {

  implicit val authDtoReads = Json.reads[AuthDto]
}
