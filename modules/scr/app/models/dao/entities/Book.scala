package models.dao.entities

import models.dao.schema.LibrarySchema
import org.joda.time.DateTime
import org.squeryl.KeyedEntity
import play.api.libs.json.Json.{fromJson, toJson}
import play.api.libs.json.{Format, JsResult, JsValue, Json, Reads, Writes}

import java.sql.{Date, Timestamp}
import java.time.LocalDateTime

case class Book(id: String,
                title: String,
                publisherId: String,
                year: Int,
                isbn: Option[String],
                ownerId: Option[String],
                takenAt: Option[Timestamp],
                returnPeriod: Option[Int]) extends KeyedEntity[String] {
  lazy val authors = LibrarySchema.bookToAuthor.left(this)
  lazy val owner = LibrarySchema.readerToBooks.right(this)
  lazy val publisher = LibrarySchema.publisherToBooks.right(this)

//  def this() = this("0", "", "", 0, Some(""), Some(""), Some(Date.now()), Some(0))
}

object Book {
  def timestampToDateTime(t: Timestamp): DateTime = new DateTime(t.getTime)

  def dateTimeToTimestamp(dt: DateTime): Timestamp = new Timestamp(dt.getMillis)

  implicit val timestampFormat = new Format[Timestamp] {

    def writes(t: Timestamp): JsValue = toJson(timestampToDateTime(t))

    def reads(json: JsValue): JsResult[Timestamp] = fromJson[DateTime](json).map(dateTimeToTimestamp)

  }
//  implicit val readsDate = Reads.jodaDateReads("yyyy-MM-dd")
//  implicit val writesDate = Writes.jodaDateWrites("yyyy-MM-dd")
  implicit val reads = Json.reads[Book]
  implicit val writes = Json.writes[Book]
}
