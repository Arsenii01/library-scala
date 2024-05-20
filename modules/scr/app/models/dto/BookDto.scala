package models.dto

import models.dao.entities.Publisher
import play.api.libs.json.{Json, Writes}

case class BookDto(id: String, title: String,
                   publisher: Publisher, year: Int,
                   isbn: Option[String], ownerId: Option[String])


object BookDto {
  implicit val writes: Writes[BookDto] = Json.writes[BookDto]
}