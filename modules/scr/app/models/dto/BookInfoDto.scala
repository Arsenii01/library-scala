package models.dto

import models.dao.entities.{Author, Publisher, Reader}
import org.joda.time.DateTime
import play.api.libs.json.Json.{fromJson, toJson}
import play.api.libs.json.{Format, JsResult, JsValue, Json, Writes}

import java.sql.Timestamp
import java.time.LocalDateTime

case class BookInfoDto(id: String, title: String,
                       publisher: Publisher, year: Int,
                       isbn: Option[String], authors: List[Author],
                       owner: Option[Reader], takenAt: Option[Timestamp],
                       returnPeriod: Option[Int], isExpired: Boolean)

object BookInfoDto {
  def timestampToDateTime(t: Timestamp): DateTime = new DateTime(t.getTime)

  def dateTimeToTimestamp(dt: DateTime): Timestamp = new Timestamp(dt.getMillis)

  implicit val timestampFormat = new Format[Timestamp] {

    def writes(t: Timestamp): JsValue = toJson(timestampToDateTime(t))

    def reads(json: JsValue): JsResult[Timestamp] = fromJson[DateTime](json).map(dateTimeToTimestamp)

  }
  implicit val writes: Writes[BookInfoDto] = Json.writes[BookInfoDto]
}
