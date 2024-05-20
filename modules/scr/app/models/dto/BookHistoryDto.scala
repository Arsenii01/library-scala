package models.dto

import models.dao.entities.BookHistory
import play.api.libs.json.Json

case class BookHistoryDto(borrowals: List[BookHistory],
                          ownerCount: Int)

object BookHistoryDto {
  implicit val writes = Json.writes[BookHistoryDto]
}
