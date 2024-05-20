package models.dto

import play.api.libs.json.Json

case class BookInsertDto(id: String, title: String,
                         publisherId: String, year: Int,
                         isbn: Option[String], authors: List[String])

object BookInsertDto {

  implicit val reads = Json.reads[BookInsertDto]
}

