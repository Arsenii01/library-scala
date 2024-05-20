package models.dto

import play.api.libs.json.Json

case class BookSetRequest(readerId: String,
                          returnPeriod: Int)

object BookSetRequest {

  implicit val reads = Json.reads[BookSetRequest]
}