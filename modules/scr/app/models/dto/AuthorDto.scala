package models.dto

import play.api.libs.json.Json

case class AuthorDto(name: String)

object AuthorDto {
  implicit val reads = Json.reads[AuthorDto]
}