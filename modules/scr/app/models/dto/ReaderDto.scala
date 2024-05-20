package models.dto

import models.dao.entities.{Book, Reader}
import play.api.libs.json.Json

case class ReaderDto(id: String, name: String, email: String, books: List[Book])

object ReaderDto {
  def toReaderDto(reader: Reader, books: List[Book]): ReaderDto = ReaderDto(reader.id, reader.name, reader.email, books)
  implicit val writes = Json.writes[ReaderDto]
}
