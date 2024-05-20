package models.dao.entities

import org.joda.time.DateTime
import org.squeryl.KeyedEntity
import play.api.libs.json.Json.{fromJson, toJson}
import play.api.libs.json.{Format, JsResult, JsValue, Json}

import java.sql.Timestamp

case class BookHistory(id: String,
                       bookId: String,
                       readerId: String,
                       takenAt: Timestamp,
                       returnedAt: Timestamp) extends KeyedEntity[String]


object BookHistory {
  def timestampToDateTime(t: Timestamp): DateTime = new DateTime(t.getTime)

  def dateTimeToTimestamp(dt: DateTime): Timestamp = new Timestamp(dt.getMillis)

  implicit val timestampFormat = new Format[Timestamp] {

    def writes(t: Timestamp): JsValue = toJson(timestampToDateTime(t))

    def reads(json: JsValue): JsResult[Timestamp] = fromJson[DateTime](json).map(dateTimeToTimestamp)

  }

  implicit val reads = Json.reads[BookHistory]

  implicit val writes = Json.writes[BookHistory]
}
